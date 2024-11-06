package com.efree.gateway.service.impl;

import com.efree.gateway.base.BaseApi;
import com.efree.gateway.dto.mapper.AuthMapper;
import com.efree.gateway.dto.request.LoginDto;
import com.efree.gateway.dto.request.RefreshTokenDto;
import com.efree.gateway.dto.request.RegisterDto;
import com.efree.gateway.dto.request.VerifyDto;
import com.efree.gateway.dto.response.AuthDto;
import com.efree.gateway.external.mail.MailServiceClient;
import com.efree.gateway.external.userservice.UserServiceRestClientConsumer;
import com.efree.gateway.external.userservice.dto.TransactionUserDto;
import com.efree.gateway.external.userservice.dto.UpdateVerifiedCodeDto;
import com.efree.gateway.service.AuthService;
import com.efree.gateway.util.RandomUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MailServiceClient mailServiceClient;
    private final UserServiceRestClientConsumer userServiceRestClientConsumer;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${mail.subject}")
    private String mailSubject;

    @Override
    public AuthDto login(LoginDto loginDto) {
        return null;
    }

    @Override
    public AuthDto refreshToken(RefreshTokenDto refreshTokenDto) {
        return null;
    }

    @Override
    public void register(RegisterDto registerDto) throws MessagingException {
        //map from registerDto to transactionUserDto
        TransactionUserDto transactionUserDto =
                authMapper.fromRegisterDtoToTransactionUserDto(registerDto);
        //encrypt the raw password
        transactionUserDto.setPassword(passwordEncoder.encode(transactionUserDto.getPassword()));

        BaseApi<String> baseUserServiceResponse =
                userServiceRestClientConsumer.createNewUser(transactionUserDto);
        if (baseUserServiceResponse.isSuccess()) {
            //update verification code
            String userUuid = baseUserServiceResponse.message();
            //generate six random digit for verification code
            String verifiedCode = RandomUtil.getRandomNumber();
            UpdateVerifiedCodeDto updateVerifiedCodeDto = new UpdateVerifiedCodeDto(transactionUserDto.getEmail(), verifiedCode);
            boolean isStatusSuccess =
                    userServiceRestClientConsumer.updateVerifiedCodeUser(userUuid, updateVerifiedCodeDto);

            //send verified code to user's email asynchronously
            if (isStatusSuccess) {
                mailServiceClient.buildAndSendMailAsync(transactionUserDto.getEmail(), mailSubject, verifiedCode);
            }
        }
    }

    @Override
    public void verify(VerifyDto verifyDto) {
        boolean isVerifiedSuccess = userServiceRestClientConsumer.verify(verifyDto);
        if (!isVerifiedSuccess) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Email verification has been failed!");
        }
    }

}
