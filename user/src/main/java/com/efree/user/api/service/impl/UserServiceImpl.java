package com.efree.user.api.service.impl;

import com.efree.user.api.dto.mapper.UserMapper;
import com.efree.user.api.dto.request.TransactionUserDto;
import com.efree.user.api.dto.response.AuthProfileUserDto;
import com.efree.user.api.dto.response.AuthUserDto;
import com.efree.user.api.dto.response.UserDto;
import com.efree.user.api.entity.Role;
import com.efree.user.api.entity.User;
import com.efree.user.api.entity.UserRole;
import com.efree.user.api.external.fileservice.FileServiceRestClientConsumer;
import com.efree.user.api.external.fileservice.dto.FileDto;
import com.efree.user.api.repository.RoleRepository;
import com.efree.user.api.repository.UserRepository;
import com.efree.user.api.repository.UserRoleRepository;
import com.efree.user.api.service.UserService;
import com.efree.user.api.util.FormatUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
    private final FileServiceRestClientConsumer fileServiceRestClientConsumer;
    private final PasswordEncoder passwordEncoder;

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
    public void createNewUser(TransactionUserDto transactionUserDto) {
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

        //enable the permission
        user.setIsVerified(true);
        user.setIsEnabled(true);

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

    @Override
    public FileDto uploadUserProfile(String authUserUuid, String uuid, MultipartFile fileRequest) {
        //decode auth user uuid
        authUserUuid = decodeAuthUuid(authUserUuid);
        User authUser = userRepository.findById(authUserUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Resource denied request to upload!")
                );

        User user = userRepository.findById(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        List<String> authRoles = authUser.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getName())
                .toList();
        if (!authRoles.contains("ADMIN") && !user.getUuid().equals(authUserUuid)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Resource denied request to upload! forbidden!");
        }

        //invoke file service to upload to server
        FileDto fileDto = fileServiceRestClientConsumer.singleUpload(fileRequest);
        user.setImageUrl(fileDto.name());
        userRepository.save(user);

        return fileDto;
    }

    @Override
    public void loadUserPermission(String uuid) {

    }

    @Override
    public void setUserPermission(String uuid, String permissions) {

    }

    @Override
    public void removeUserPermission(String uuid, String permissions) {

    }

    private String decodeAuthUuid(String authUserUuid) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(authUserUuid);
            authUserUuid = new String(decodedBytes);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Error decoding request auth!");
        }
        return authUserUuid;
    }

    @Override
    public AuthProfileUserDto loadUserProfile(String email) {
        //load authenticated user object
        User authenticatedUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Email has not been authenticated!")
                );

        AuthProfileUserDto authProfileUserDto = userMapper.fromUserToAuthProfileUserDto(authenticatedUser);

        //set up granted authorities
        authProfileUserDto.setGrantedAuthorities(buildGrantedAuthorities(authenticatedUser));

        return authProfileUserDto;
    }

    @Override
    public AuthUserDto loadAuthUserByEmail(String email) {
        Optional<User> authUserOptional = userRepository.findByEmailAndIsVerifiedTrueAndIsEnabledTrue(email);

        if (authUserOptional.isEmpty()) return null;
        User authUser = authUserOptional.get();
        AuthUserDto authUserDto = userMapper.fromUserToAuthUserDto(authUser);

        //set up granted authorities
        authUserDto.setGrantedAuthorities(buildGrantedAuthorities(authUser));

        return authUserDto;
    }

    private List<String> buildGrantedAuthorities(User authUser) {
        List<String> authorities = new ArrayList<>();
        authUser.getUserRoles().forEach(userRole -> {
            authorities.add("ROLE_" + userRole.getRole().getName());
            userRole.getRole().getAuthorities().forEach(authority ->
                    authorities.add(authority.getName()));
        });
        return authorities;
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
        user.setEncryptedPassword(passwordEncoder.encode(transactionUserDto.password()));
        return user;
    }

}
