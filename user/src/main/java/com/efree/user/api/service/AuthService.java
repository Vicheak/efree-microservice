package com.efree.user.api.service;

import com.efree.user.api.dto.request.*;
import jakarta.mail.MessagingException;

public interface AuthService {

    /**
     * This method is used to register a user for the role SUBSCRIBER
     * @param registerDto is the request from client
     */
    void register(RegisterDto registerDto) throws MessagingException;

    /**
     * This method is used to verify the email via verification code
     * @param verifyDto is the request from client
     */
    void verify(VerifyDto verifyDto);

    /**
     * This method is used when the client forgets the password to the system
     * @param forgetPasswordDto is the request from client
     * @return PasswordTokenDto
     * @throws MessagingException
     */
    PasswordTokenDto forgetPassword(ForgetPasswordDto forgetPasswordDto) throws MessagingException;

    /**
     * This method is used to send verification code to client's email
     * @param email is the request from client
     * @throws MessagingException
     */
    void sendVerificationCode(String email) throws MessagingException;

    /**
     * This method is used to reset client's password after verify the account
     * @param resetPasswordDto is the request from client
     */
    void resetPassword(ResetPasswordDto resetPasswordDto);

}
