package com.efree.gateway.external.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionUserDto {

    @NotBlank(message = "Username must not be blank!")
    @Size(max = 50, message = "Username must not be greater than 50 characters")
    String username;

    @NotBlank(message = "Email must not be blank!")
    @Email(message = "Email must be a valid email address!")
    @Size(max = 50, message = "Email must not be greater than 50 characters")
    String email;

    @NotBlank(message = "Password must not be blank!")
    @Size(min = 8, message = "Password must be at least 8 characters!")
    String password;

    @NotBlank(message = "Gender must not be blank!")
    @Size(max = 20, message = "Gender must not be greater than 20 characters")
    String gender;

    @NotBlank(message = "Phone Number must not be blank!")
    @Size(max = 30, message = "Phone Number must not be greater than 30 characters")
    String phoneNumber;

    @NotNull(message = "Date of Birth must not be null!")
    @Past(message = "Date of Birth must be in the past!")
    LocalDate dateOfBirth;

    Set<Integer> roleIds;

}
