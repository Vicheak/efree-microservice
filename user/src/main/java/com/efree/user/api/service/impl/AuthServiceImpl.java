package com.efree.user.api.service.impl;

import com.efree.user.api.dto.mapper.AuthMapper;
import com.efree.user.api.dto.request.*;
import com.efree.user.api.entity.Authority;
import com.efree.user.api.entity.Role;
import com.efree.user.api.entity.User;
import com.efree.user.api.entity.UserAuthority;
import com.efree.user.api.external.mail.Mail;
import com.efree.user.api.external.mail.MailService;
import com.efree.user.api.repository.AuthRepository;
import com.efree.user.api.repository.RoleRepository;
import com.efree.user.api.repository.UserRepository;
import com.efree.user.api.repository.UserAuthorityRepository;
import com.efree.user.api.service.AuthService;
import com.efree.user.api.util.RandomUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final RoleRepository roleRepository;
    private final UserServiceImpl userService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${MAIL_USERNAME}")
    private String adminMail;

    @Value("${MAIL_SUBJECT}")
    private String subjectMail;

    @Transactional
    @Override
    public void register(RegisterDto registerDto) throws MessagingException {
        //map from registerDto to transactionUserDto
        TransactionUserDto transactionUserDto =
                authMapper.fromRegisterDtoToTransactionUserDto(registerDto);

        //validate the dto
        userService.validateTransactionUserDto(transactionUserDto);

        //map from dto to entity
        User newUser = userService.setupNewUser(transactionUserDto);

        List<UserAuthority> userAuthorities = new ArrayList<>();
        Set<Authority> requestAuthorities = new HashSet<>();

        //register as customer
        Optional<Role> requestRoleOptional = roleRepository.findById(2);
        if (requestRoleOptional.isPresent()) {
            Role requestRole = requestRoleOptional.get();
            requestAuthorities.addAll(requestRole.getAuthorities());
        }

        //for ROLE_CUSTOMER
        requestAuthorities.add(Authority.builder()
                .id(35)
                .build());

        requestAuthorities.forEach(authority -> userAuthorities.add(UserAuthority.builder()
                .user(newUser)
                .authority(authority)
                .build()));

        //set up user authorities
        newUser.setUserAuthorities(userAuthorities);

        authRepository.save(newUser);

        userAuthorityRepository.saveAll(userAuthorities);

        updateVerifiedCodeAndSendMail(newUser, subjectMail);
    }

    @Override
    public void verify(VerifyDto verifyDto) {
        //load the unverified user by email and verified code
        User verifiedUser = authRepository.findByEmailAndVerifiedCodeAndIsVerifiedFalseAndIsEnabledFalse(
                        verifyDto.email(), verifyDto.verifiedCode())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Email verification has been failed!")
                );

        //make the user verified
        verifiedUser.setIsVerified(true);
        verifiedUser.setIsEnabled(true);
        verifiedUser.setVerifiedCode(null);

        authRepository.save(verifiedUser);
    }

    @Transactional
    @Override
    public PasswordTokenDto forgetPassword(ForgetPasswordDto forgetPasswordDto) throws MessagingException {
        User user = userRepository.findByEmail(forgetPasswordDto.email())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with email, %s has not been found in the system!"
                                        .formatted(forgetPasswordDto.email()))
                );

        String passwordToken = RandomUtil.randomTokenGenerator(30);

        user.setIsVerified(false);
        user.setIsEnabled(false);
        user.setPasswordToken(passwordToken);
        authRepository.save(user);

        updateVerifiedCodeAndSendMail(user, subjectMail);

        return PasswordTokenDto.builder()
                .message("Please check your email for verification code and verify your account to reset password!")
                .token(passwordToken)
                .build();
    }

    @Transactional
    @Override
    public void sendVerificationCode(String email) throws MessagingException {
        //load user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with email, %s has not been found in the system!"
                                        .formatted(email))
                );

        updateVerifiedCodeAndSendMail(user, subjectMail);
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        //load user by email
        User user = userRepository.findByEmailAndIsVerifiedTrueAndIsEnabledTrue(resetPasswordDto.email())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Email has been not found or unauthorized to reset password!")
                );

        if(!resetPasswordDto.token().equals(user.getPasswordToken()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Password token is invalid, unauthorized access!");

        if (!resetPasswordDto.password().equals(resetPasswordDto.passwordConfirmation()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Password and password confirmation must be matched!");

        user.setEncryptedPassword(passwordEncoder.encode(resetPasswordDto.password()));
        user.setPasswordToken(null);
        authRepository.save(user);
    }

    private void updateVerifiedCodeAndSendMail(User user, String subject) throws MessagingException {
        //generate six random digit for verification code
        String verifiedCode = RandomUtil.getRandomNumber();

        //update verification code
        authRepository.updateVerifiedCode(user.getEmail(), verifiedCode);

        buildAndSendMail(user.getEmail(), subject, verifiedCode);
    }

    private void buildAndSendMail(String email, String subject, String verifiedCode) throws MessagingException {
        Mail<String> verifiedMail = new Mail<>();
        verifiedMail.setSender(adminMail);
        verifiedMail.setReceiver(email);
        verifiedMail.setSubject(subject);
        verifiedMail.setTemplate("auth/verify-mail.html");
        verifiedMail.setMetaData(verifiedCode);

        mailService.sendMail(verifiedMail);
    }

}
