package com.efree.gateway.service;

import com.efree.gateway.dto.request.LoginDto;
import com.efree.gateway.dto.request.RefreshTokenDto;
import com.efree.gateway.dto.request.RegisterDto;
import com.efree.gateway.dto.request.VerifyDto;
import com.efree.gateway.dto.response.AuthDto;
import jakarta.mail.MessagingException;

public interface AuthService {

    /**
     * This method is used to authenticate a valid user
     * @param loginDto is the request from client
     * @return AuthDto
     */
    AuthDto login(LoginDto loginDto);

    /**
     * This method is used to generate new access token after expiration
     * @param refreshTokenDto is the request from client
     * @return AuthDto
     */
    AuthDto refreshToken(RefreshTokenDto refreshTokenDto);

    /**
     * This method is used to register a user for the default role CUSTOMER
     * @param registerDto is the request from client
     */
    void register(RegisterDto registerDto) throws MessagingException;

    /**
     * This method is used to verify the email via verification code
     * @param verifyDto is the request from client
     */
    void verify(VerifyDto verifyDto);

}
