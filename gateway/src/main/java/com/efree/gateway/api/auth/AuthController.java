package com.efree.gateway.api.auth;

import com.efree.gateway.dto.request.LoginDto;
import com.efree.gateway.dto.request.RefreshTokenDto;
import com.efree.gateway.dto.request.RegisterDto;
import com.efree.gateway.dto.request.VerifyDto;
import com.efree.gateway.dto.response.AuthDto;
import com.efree.gateway.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/gateway/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/login")
    public Mono<AuthDto> login(@RequestBody @Valid LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/refreshToken")
    public Mono<AuthDto> refreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto) {
        return authService.refreshToken(refreshTokenDto);
    }

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

    @GetMapping("/ping")
    public String ping(){
        return "Ping to gateway successfully!";
    }

}
