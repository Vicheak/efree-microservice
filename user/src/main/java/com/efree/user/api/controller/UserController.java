package com.efree.user.api.controller;

import com.efree.user.api.base.BaseApi;
import com.efree.user.api.dto.request.IsEnabledDto;
import com.efree.user.api.dto.request.TransactionUserDto;
import com.efree.user.api.dto.request.UpdateVerifiedCodeDto;
import com.efree.user.api.dto.request.VerifyDto;
import com.efree.user.api.dto.response.UserDto;
import com.efree.user.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    //FOR CALL INTERNAL SERVICE
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BaseApi<?> createNewUser(@RequestBody @Valid TransactionUserDto transactionUserDto) {

        String userUuid = userService.createNewUser(transactionUserDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message(userUuid) //USE FOR REF
                .timestamp(LocalDateTime.now())
                .payload("No response payload")
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{uuid}")
    public BaseApi<?> updateUserByUuid(@PathVariable String uuid,
                                       @RequestBody TransactionUserDto transactionUserDto) {

        userService.updateUserByUuid(uuid, transactionUserDto);

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

    //FOR CALL INTERNAL SERVICE
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verifiedCode/{uuid}")
    public Boolean updateVerifiedCodeByUuid(@PathVariable String uuid,
                                            @RequestBody @Valid UpdateVerifiedCodeDto updateVerifiedCodeDto){
        return userService.updateVerifiedCodeByUuid(uuid, updateVerifiedCodeDto);
    }

    //FOR CALL INTERNAL SERVICE
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verifyUser")
    public Boolean verify(@RequestBody @Valid VerifyDto verifyDto){
        return userService.verify(verifyDto);
    }

}
