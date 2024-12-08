package com.efree.gateway.service;

import com.efree.gateway.dto.request.LoginDto;
import com.efree.gateway.dto.request.RefreshTokenDto;
import com.efree.gateway.dto.response.AuthDto;
import com.efree.gateway.external.userservice.dto.AuthProfileUserDto;
import reactor.core.publisher.Mono;

public interface AuthService {

    /**
     * This method is used to authenticate a valid user
     * @param loginDto is the request from client
     * @return Mono<AuthDto>
     */
    Mono<AuthDto> login(LoginDto loginDto);

    /**
     * This method is used to generate new access token after expiration
     * @param refreshTokenDto is the request from client
     * @return Mono<AuthDto>
     */
    Mono<AuthDto> refreshToken(RefreshTokenDto refreshTokenDto);

    /**
     * This method is used to load user profile by valid authentication
     * @return Mono<AuthProfileUserDto>
     */
    Mono<AuthProfileUserDto> loadUserProfile();

}
