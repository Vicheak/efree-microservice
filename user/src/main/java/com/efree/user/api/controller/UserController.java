package com.efree.user.api.controller;

import com.efree.user.api.base.BaseApi;
import com.efree.user.api.dto.request.IsEnabledDto;
import com.efree.user.api.dto.request.PermissionRequestDto;
import com.efree.user.api.dto.request.TransactionUserDto;
import com.efree.user.api.dto.response.AuthProfileUserDto;
import com.efree.user.api.dto.response.AuthUserDto;
import com.efree.user.api.dto.response.AuthorityResponseDto;
import com.efree.user.api.dto.response.UserDto;
import com.efree.user.api.external.fileservice.dto.FileDto;
import com.efree.user.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public BaseApi<?> loadAllUsers() {

        List<UserDto> userDtoList = userService.loadAllUsers();

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("All users loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(userDtoList)
                .build();
    }

    @GetMapping("/{uuid}")
    public BaseApi<?> loadUserByUuid(@PathVariable String uuid) {

        UserDto userDto = userService.loadUserByUuid(uuid);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("User with uuid, %s loaded successfully!".formatted(uuid))
                .timestamp(LocalDateTime.now())
                .payload(userDto)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BaseApi<?> createNewUser(@RequestBody @Valid TransactionUserDto transactionUserDto) {

        userService.createNewUser(transactionUserDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("A user has been created successfully!")
                .timestamp(LocalDateTime.now())
                .payload("No response payload")
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{uuid}")
    public BaseApi<?> updateUserByUuid(@RequestHeader("XUUID") String authUserUuid,
                                       @PathVariable String uuid,
                                       @RequestBody TransactionUserDto transactionUserDto) {

        userService.updateUserByUuid(authUserUuid, uuid, transactionUserDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("A user has been updated successfully!")
                .timestamp(LocalDateTime.now())
                .payload("No response payload")
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{uuid}")
    public BaseApi<?> deleteUserByUuid(@PathVariable String uuid) {

        userService.deleteUserByUuid(uuid);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("A user has been deleted successfully!")
                .timestamp(LocalDateTime.now())
                .payload("No response payload")
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}")
    public BaseApi<?> updateUserIsEnabledByUuid(@PathVariable String uuid,
                                                @RequestBody @Valid IsEnabledDto isEnabledDto) {

        userService.updateUserIsEnabledByUuid(uuid, isEnabledDto.isEnabled());

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("A user status has been updated successfully!")
                .timestamp(LocalDateTime.now())
                .payload("No response payload")
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/upload/profile/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseApi<?> uploadUserProfile(@RequestHeader("XUUID") String authUserUuid,
                                        @PathVariable String uuid,
                                        @RequestPart MultipartFile file) {

        FileDto fileDto = userService.uploadUserProfile(authUserUuid, uuid, file);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("User profile image has been uploaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(fileDto)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/authorities/{uuid}")
    public BaseApi<?> loadUserPermission(@PathVariable String uuid) {

        List<AuthorityResponseDto> authorityResponses = userService.loadUserPermission(uuid);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("User permission loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(authorityResponses)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/authorities/{uuid}")
    public BaseApi<?> setUserPermission(@PathVariable String uuid,
                                        @RequestBody @Valid PermissionRequestDto permissionRequestDto) {

        List<AuthorityResponseDto> authorityResponses = userService.setUserPermission(uuid, permissionRequestDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("User permission updated successfully!")
                .timestamp(LocalDateTime.now())
                .payload(authorityResponses)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/authorities/{uuid}")
    public BaseApi<?> removeUserPermission(@PathVariable String uuid,
                                           @RequestBody @Valid PermissionRequestDto permissionRequestDto) {

        List<AuthorityResponseDto> authorityResponses = userService.removeUserPermission(uuid, permissionRequestDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("User permission updated successfully!")
                .timestamp(LocalDateTime.now())
                .payload(authorityResponses)
                .build();
    }

    //FOR CALL INTERNAL SERVICE
    @GetMapping("/me/{email}")
    public AuthProfileUserDto loadUserProfile(@PathVariable String email) {
        return userService.loadUserProfile(email);
    }

    //FOR CALL INTERNAL SERVICE
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/authUser/{email}")
    public AuthUserDto loadAuthUserByEmail(@PathVariable String email) {
        return userService.loadAuthUserByEmail(email);
    }

}
