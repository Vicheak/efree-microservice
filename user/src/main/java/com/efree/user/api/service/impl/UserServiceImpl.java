package com.efree.user.api.service.impl;

import com.efree.user.api.dto.mapper.UserMapper;
import com.efree.user.api.dto.request.TransactionUserDto;
import com.efree.user.api.dto.request.UpdateVerifiedCodeDto;
import com.efree.user.api.dto.request.VerifyDto;
import com.efree.user.api.dto.response.UserDto;
import com.efree.user.api.entity.Role;
import com.efree.user.api.entity.User;
import com.efree.user.api.entity.UserRole;
import com.efree.user.api.repository.RoleRepository;
import com.efree.user.api.repository.UserRepository;
import com.efree.user.api.repository.UserRoleRepository;
import com.efree.user.api.service.UserService;
import com.efree.user.api.util.FormatUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public List<UserDto> loadAllUsers() {
        return userMapper.fromUserToUserDto(userRepository.findAll());
    }

    @Override
    public UserDto loadUserByUuid(String uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        return userMapper.fromUserToUserDto(user);
    }

    @Transactional
    @Override
    public String createNewUser(TransactionUserDto transactionUserDto) {
        if (Objects.isNull(transactionUserDto.roleIds()) || transactionUserDto.roleIds().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User must have at least one role!");

        validateTransactionUserDto(transactionUserDto);

        //check roles if exist
        boolean allExisted = transactionUserDto.roleIds().stream()
                .allMatch(roleRepository::existsById);

        if (!allExisted)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Role is not valid in the system! please check!");

        User user = setupNewUser(transactionUserDto);

        //disable the permission
        user.setIsVerified(false);
        user.setIsEnabled(false);

        List<UserRole> userRoles = new ArrayList<>();

        transactionUserDto.roleIds().forEach(roleId ->
                userRoles.add(UserRole.builder()
                        .user(user)
                        .role(Role.builder().id(roleId).build())
                        .build()));

        //set up user roles
        user.setUserRoles(userRoles);

        userRepository.save(user);

        userRoleRepository.saveAll(userRoles);

        return user.getUuid();
    }

    @Transactional
    @Override
    public void updateUserByUuid(String uuid, TransactionUserDto transactionUserDto) {
        //load the user by uuid
        User user = userRepository.findById(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        //check username if already exist
        if (Objects.nonNull(transactionUserDto.username()))
            if (!transactionUserDto.username().equalsIgnoreCase(user.getUsername()) &&
                    userRepository.existsByUsernameIgnoreCase(transactionUserDto.username()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Username conflicts resource in the system!");

        //check email if already exist
        if (Objects.nonNull(transactionUserDto.email()))
            if (!transactionUserDto.email().equalsIgnoreCase(user.getEmail()) &&
                    userRepository.existsByEmailIgnoreCase(transactionUserDto.email()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Email conflicts resource in the system!");

        //check phone number format and existence
        if (Objects.nonNull(transactionUserDto.phoneNumber())) {
            if (!FormatUtil.checkPhoneFormat(transactionUserDto.phoneNumber()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Phone Number is not in a valid format!");

            if (!transactionUserDto.phoneNumber().equals(user.getPhoneNumber()) &&
                    userRepository.existsByPhoneNumber(transactionUserDto.phoneNumber()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Phone Number conflicts resource in the system!");
        }

        //check roles if exist
        if (Objects.nonNull(transactionUserDto.roleIds()))
            if (transactionUserDto.roleIds().isEmpty())
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "User must have at least one role!");
            else {
                boolean allExisted = transactionUserDto.roleIds().stream()
                        .allMatch(roleRepository::existsById);

                if (!allExisted)
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Role is not valid in the system! please check!");

                //process to remove previous user roles
                userRoleRepository.deleteAll(user.getUserRoles());

                List<UserRole> userRoles = updateUserRolesTransaction(user, transactionUserDto.roleIds());

                userRoleRepository.saveAll(userRoles);
            }

        //map from dto to entity but except the null value from dto
        userMapper.fromTransactionUserDtoToUser(user, transactionUserDto);

        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUserByUuid(String uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        //remove from user roles
        userRoleRepository.deleteAll(user.getUserRoles());

        userRepository.delete(user);
    }

    @Transactional
    @Override
    public void updateUserIsEnabledByUuid(String uuid, Boolean isEnabled) {
        User user = userRepository.findById(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        user.setIsEnabled(isEnabled);

        userRepository.save(user);
    }

    @Transactional
    @Override
    public Boolean updateVerifiedCodeByUuid(String uuid, UpdateVerifiedCodeDto updateVerifiedCodeDto) {
        //update verification code
        userRepository.updateVerifiedCode(uuid, updateVerifiedCodeDto.email(), updateVerifiedCodeDto.verifiedCode());
        return true;
    }

    @Transactional
    @Override
    public Boolean verify(VerifyDto verifyDto) {
        //load the unverified user by email and verified code
        Optional<User> verifiedUserOptional = userRepository.findByEmailAndVerifiedCodeAndIsVerifiedFalseAndIsEnabledFalse(
                        verifyDto.email(), verifyDto.verifiedCode());

        if(verifiedUserOptional.isPresent()){
            User verifiedUser = verifiedUserOptional.get();

            //make the user verified
            verifiedUser.setIsVerified(true);
            verifiedUser.setIsEnabled(true);
            verifiedUser.setVerifiedCode(null);

            userRepository.save(verifiedUser);
        }

        return verifiedUserOptional.isPresent();
    }

    private @NonNull List<UserRole> updateUserRolesTransaction(@NonNull User user, @NonNull Set<Integer> roleIds) {
        List<UserRole> userRoles = new ArrayList<>();

        roleIds.forEach(roleId ->
                userRoles.add(UserRole.builder()
                        .user(user)
                        .role(Role.builder().id(roleId).build())
                        .build()));

        //set up new user roles
        user.setUserRoles(userRoles);

        return userRoles;
    }

    public void validateTransactionUserDto(TransactionUserDto transactionUserDto) {
        //check username if already exist
        if (userRepository.existsByUsernameIgnoreCase(transactionUserDto.username()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Username conflicts resource in the system!");

        //check email if already exist
        if (userRepository.existsByEmailIgnoreCase(transactionUserDto.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email conflicts resource in the system!");

        //check phone number format
        if (!FormatUtil.checkPhoneFormat(transactionUserDto.phoneNumber()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Phone Number is not in a valid format!");

        //check phone number is already exist
        if (userRepository.existsByPhoneNumber(transactionUserDto.phoneNumber()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Phone Number conflicts resource in the system!");
    }

    public User setupNewUser(TransactionUserDto transactionUserDto) {
        //map from dto to entity
        User user = userMapper.fromTransactionUserDtoToUser(transactionUserDto);
        user.setUuid(UUID.randomUUID().toString());
        user.setIsVerified(false);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setIsEnabled(false);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEncryptedPassword(transactionUserDto.password());
        return user;
    }

}
