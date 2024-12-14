package com.efree.user.api.service;

import com.efree.user.api.dto.request.PermissionRequestDto;
import com.efree.user.api.dto.request.TransactionUserDto;
import com.efree.user.api.dto.response.AuthProfileUserDto;
import com.efree.user.api.dto.response.AuthUserDto;
import com.efree.user.api.dto.response.AuthorityResponseDto;
import com.efree.user.api.dto.response.UserDto;
import com.efree.user.api.external.fileservice.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    /**
     * This method is used to load all users from the system
     * @return List<UserDto>
     */
    List<UserDto> loadAllUsers();

    /**
     * This method is used to load specific user by uuid
     * @param uuid is the path parameter from client
     * @return UserDto
     */
    UserDto loadUserByUuid(String uuid);

    /**
     * This method is used to create new user (ADMIN authority)
     * @param transactionUserDto is the request from client
     */
    void createNewUser(TransactionUserDto transactionUserDto);

    /**
     * This method is used to update specific user by uuid
     * @param authUserUuid is the request header from auth client
     * @param uuid is the path parameter from client
     * @param transactionUserDto is the request from client
     */
    void updateUserByUuid(String authUserUuid, String uuid, TransactionUserDto transactionUserDto);

    /**
     * This method is used to delete specific user by uuid from the system
     * @param uuid is the path parameter from client
     */
    void deleteUserByUuid(String uuid);

    /**
     * This method is used to disable specific user by uuid from the system
     * @param uuid is the path parameter from client
     * @param isEnabled is the request from client
     */
    void updateUserIsEnabledByUuid(String uuid, Boolean isEnabled);

    /**
     * This method is used to upload single image resource to server
     * @param authUserUuid is the request header from auth client
     * @param uuid is the path parameter from client
     * @param fileRequest fileRequest is the request from client
     * @return FileDto
     */
    FileDto uploadUserProfile(String authUserUuid, String uuid, MultipartFile fileRequest);

    List<AuthorityResponseDto> loadUserPermission(String uuid);

    List<AuthorityResponseDto> setUserPermission(String uuid, PermissionRequestDto permissionRequestDto);

    List<AuthorityResponseDto> removeUserPermission(String uuid, PermissionRequestDto permissionRequestDto);

    /**
     * This method is used to load user profile by authenticated user
     * @param email is the path parameter from client
     * @return AuthProfileUserDto
     */
    AuthProfileUserDto loadUserProfile(String email);


    AuthUserDto loadAuthUserByEmail(String email);

}
