package com.efree.gateway.api.auth;

import com.efree.gateway.dto.request.LoginDto;
import com.efree.gateway.dto.request.RefreshTokenDto;
import com.efree.gateway.dto.response.AuthDto;
import com.efree.gateway.dto.response.DashboardMenuDto;
import com.efree.gateway.external.userservice.dto.AuthProfileUserDto;
import com.efree.gateway.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/dashboard")
    public Mono<List<DashboardMenuDto>> loadDashboardMenu(){
        return authService.loadDashboardMenu();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/profile/me")
    public Mono<AuthProfileUserDto> loadUserProfile() {
        return authService.loadUserProfile();
    }

    @GetMapping("/ping")
    public String ping(){
        return "Ping to gateway successfully!";
    }

}
