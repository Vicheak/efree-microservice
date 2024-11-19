package com.efree.gateway.external.userservice.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthUserDto {

    String uuid;
    String username;
    String email;
    String encryptedPassword;
    String gender;
    String phoneNumber;
    LocalDate dateOfBirth;
    LocalDateTime joinDate;
    Boolean isVerified;
    Boolean accountNonExpired;
    Boolean accountNonLocked;
    Boolean isEnabled;
    List<String> grantedAuthorities;

}
