package com.efree.user.api.controller;

import com.efree.user.api.dto.request.*;
import com.efree.user.api.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody @Valid RegisterDto registerDto) throws MessagingException {
        authService.register(registerDto);
        return Map.of("message", "Please check your email for verification code!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify")
    public Map<String, String> verify(@RequestBody @Valid VerifyDto verifyDto) {
        authService.verify(verifyDto);
        return Map.of("message", "Congratulation! Your email has been verified...!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/forget-password")
    public PasswordTokenDto forgetPassword(@RequestBody @Valid ForgetPasswordDto forgetPasswordDto) throws MessagingException {
        return authService.forgetPassword(forgetPasswordDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/send-verification-code")
    public Map<String, String> sendVerificationCode(@RequestBody @Valid VerificationCodeDto verificationCodeDto) throws MessagingException {
        authService.sendVerificationCode(verificationCodeDto.email());
        return Map.of("message", "Please check your email for verification code!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto){
        authService.resetPassword(resetPasswordDto);
        return Map.of("message", "Your password has been reset successfully!");
    }

}
