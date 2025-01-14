package com.efree.user.api.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterDto(@NotBlank(message = "Username must not be blank!")
                          String username,

                          @NotBlank(message = "Email must not be blank!")
                          @Email(message = "Email must be a valid email address!")
                          String email,

                          @NotBlank(message = "Password must not be blank!")
                          @Size(min = 8, message = "Password must be at least 8 characters!")
                          String password,

                          @NotBlank(message = "Gender must not be blank!")
                          String gender,

                          @NotBlank(message = "Phone Number must not be blank!")
                          String phoneNumber,

                          @NotNull(message = "Date of Birth must not be null!")
                          @Past(message = "Date of Birth must be in the past!")
                          LocalDate dateOfBirth) {
}
