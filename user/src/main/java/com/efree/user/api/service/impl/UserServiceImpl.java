package com.efree.user.api.service.impl;

import com.efree.user.api.dto.mapper.AuthorityMapper;
import com.efree.user.api.dto.mapper.UserMapper;
import com.efree.user.api.dto.request.PermissionRequestDto;
import com.efree.user.api.dto.request.TransactionUserDto;
import com.efree.user.api.dto.response.AuthProfileUserDto;
import com.efree.user.api.dto.response.AuthUserDto;
import com.efree.user.api.dto.response.AuthorityResponseDto;
import com.efree.user.api.dto.response.UserDto;
import com.efree.user.api.entity.Authority;
import com.efree.user.api.entity.Role;
import com.efree.user.api.entity.User;
import com.efree.user.api.entity.UserAuthority;
import com.efree.user.api.external.fileservice.FileServiceRestClientConsumer;
import com.efree.user.api.external.fileservice.dto.FileDto;
import com.efree.user.api.repository.AuthorityRepository;
import com.efree.user.api.repository.RoleRepository;
import com.efree.user.api.repository.UserAuthorityRepository;
import com.efree.user.api.repository.UserRepository;
import com.efree.user.api.service.UserService;
import com.efree.user.api.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;
    private final RoleRepository roleRepository;
    private final UserAuthorityRepository userAuthorityRepository;
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

        List<UserAuthority> userAuthorities = new ArrayList<>();
        Set<Authority> requestAuthorities = new HashSet<>();

        transactionUserDto.roleIds().forEach(roleId -> {
            Optional<Role> requestRoleOptional = roleRepository.findById(roleId);
            if (requestRoleOptional.isPresent()) {
                Role requestRole = requestRoleOptional.get();
                requestAuthorities.addAll(requestRole.getAuthorities());
            }
        });

        requestAuthorities.forEach(authority -> userAuthorities.add(UserAuthority.builder()
                .user(user)
                .authority(authority)
                .build()));

        //set up user authorities
        user.setUserAuthorities(userAuthorities);

        userRepository.save(user);

        userAuthorityRepository.saveAll(userAuthorities);
    }

    @Transactional
    @Override
    public void updateUserByUuid(String authUserUuid, String uuid, TransactionUserDto transactionUserDto) {
        if (Objects.isNull(authUserUuid) || authUserUuid.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Resource denied request from auth user!");
        }

        //decode auth user uuid
        authUserUuid = decodeAuthUuid(authUserUuid);
        User authUser = userRepository.findById(authUserUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Resource denied request to upload!")
                );

        //load the user by uuid
        User user = userRepository.findById(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        List<String> authAuthorities = authUser.getUserAuthorities().stream()
                .map(userAuthority -> userAuthority.getAuthority().getName())
                .toList();
        if (!authAuthorities.contains("ROLE_ADMIN") && !user.getUuid().equals(authUserUuid)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Resource denied request to upload! forbidden!");
        }

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

        //map from dto to entity but except the null value from dto
        userMapper.fromTransactionUserDtoToUser(user, transactionUserDto);

        if (Objects.nonNull(transactionUserDto.password()) && !transactionUserDto.password().isEmpty()) {
            user.setEncryptedPassword(passwordEncoder.encode(transactionUserDto.password()));
        }

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

        //remove from user authorities
        userAuthorityRepository.deleteAll(user.getUserAuthorities());

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
    public FileDto uploadUserProfile(String authUserUuid, String uuid, MultipartFile fileRequest) {
        if (Objects.isNull(authUserUuid) || authUserUuid.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Resource denied request from auth user!");
        }

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

        List<String> authAuthorities = authUser.getUserAuthorities().stream()
                .map(userAuthority -> userAuthority.getAuthority().getName())
                .toList();
        if (!authAuthorities.contains("ROLE_ADMIN") && !user.getUuid().equals(authUserUuid)) {
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
    public List<AuthorityResponseDto> loadUserPermission(String uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        List<Authority> authorities = new ArrayList<>();
        user.getUserAuthorities().forEach(userAuthority -> authorities.add(userAuthority.getAuthority()));
        return authorityMapper.fromAuthorityToAuthorityResponseDto(authorities);
    }

    @Transactional
    @Override
    public List<AuthorityResponseDto> setUserPermission(String uuid, PermissionRequestDto permissionRequestDto) {
        User user = userRepository.findById(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        //build request authorities
        Set<Authority> requestAuthorities = buildRequestAuthoritiesWithPermission(permissionRequestDto.permission());

        //check existing user authorities
        List<Authority> authorities = new ArrayList<>(user.getUserAuthorities().stream()
                .map(UserAuthority::getAuthority)
                .toList());

        Set<Authority> missingAuthorities = requestAuthorities.stream()
                .filter(requestAuthority -> {
                    boolean isExisted = authorityRepository.existsByIdAndName(requestAuthority.getId(), requestAuthority.getName());
                    if (!isExisted) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Invalid request authorities!");
                    }
                    return authorities.stream()
                            .noneMatch(existingAuthority ->
                                    existingAuthority.getId() == requestAuthority.getId() &&
                                            existingAuthority.getName().equals(requestAuthority.getName()));
                })
                .collect(Collectors.toSet());
        authorities.addAll(missingAuthorities);

        //set up user authorities
        List<UserAuthority> userAuthorities = user.getUserAuthorities();
        buildUserAuthoritiesForUser(userAuthorities, missingAuthorities, user);
        user.setUserAuthorities(userAuthorities);
        userAuthorityRepository.saveAll(userAuthorities);
        userAuthorityRepository.flush();
        return authorityMapper.fromAuthorityToAuthorityResponseDto(authorities);
    }

    @Transactional
    @Override
    public List<AuthorityResponseDto> removeUserPermission(String uuid, PermissionRequestDto permissionRequestDto) {
        User user = userRepository.findById(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        //build request authorities
        Set<Authority> requestAuthorities = buildRequestAuthoritiesWithPermission(permissionRequestDto.permission());

        //check existing user authorities
        List<Authority> authorities = new ArrayList<>(user.getUserAuthorities().stream()
                .map(UserAuthority::getAuthority)
                .toList());

        requestAuthorities.forEach(requestAuthority -> {
            boolean isExisted = authorityRepository.existsByIdAndName(requestAuthority.getId(), requestAuthority.getName());
            if (!isExisted) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid request authorities!");
            }
        });

        Set<Authority> authoritiesToRemove = authorities.stream()
                .filter(existingAuthority -> requestAuthorities.stream()
                        .anyMatch(requestAuthority ->
                                requestAuthority.getId() == existingAuthority.getId() &&
                                        requestAuthority.getName().equals(existingAuthority.getName())))
                .collect(Collectors.toSet());
        authorities.removeAll(authoritiesToRemove);

        //remove all user authorities for set up new
        userAuthorityRepository.deleteAll(user.getUserAuthorities());
        userAuthorityRepository.flush();

        //set up user authorities
        List<UserAuthority> userAuthorities = new ArrayList<>();
        buildUserAuthoritiesForUser(userAuthorities, authorities, user);
        user.setUserAuthorities(userAuthorities);
        userAuthorityRepository.saveAll(userAuthorities);
        userAuthorityRepository.flush();
        return authorityMapper.fromAuthorityToAuthorityResponseDto(authorities);
    }

    private void buildUserAuthoritiesForUser(List<UserAuthority> userAuthorities, Collection<Authority> authorities, User user) {
        authorities.forEach(authority -> userAuthorities.add(UserAuthority.builder()
                .user(user)
                .authority(Authority.builder()
                        .id(authority.getId())
                        .build())
                .build()));
    }

    private Set<Authority> buildRequestAuthoritiesWithPermission(String permission) {
        Set<Authority> requestAuthorities = new HashSet<>();
        try {
            String[] requestPermissions = permission.split(",");
            Arrays.stream(requestPermissions).forEach(requestPermission -> {
                String[] concatAuthority = requestPermission.split("\\^");
                int authorityId = Integer.parseInt(concatAuthority[0]);
                String authorityName = concatAuthority[1];
                requestAuthorities.add(Authority.builder()
                        .id(authorityId)
                        .name(authorityName)
                        .build());
            });
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to request authorities!");
        }
        return requestAuthorities;
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
        authUser.getUserAuthorities().forEach(userAuthority -> {
            authorities.add(userAuthority.getAuthority().getName());
        });
        return authorities;
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
