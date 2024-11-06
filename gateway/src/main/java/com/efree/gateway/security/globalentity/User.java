package com.efree.gateway.security.globalentity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    String uuid;
    String username;
    String email;
    String encryptedPassword;
    String gender;
    String phoneNumber;
    LocalDate dateOfBirth;
    LocalDateTime joinDate;
    String otpCode;
    Boolean isVerified;
    String verifiedCode;
    String passwordToken;
    Boolean accountNonExpired;
    Boolean accountNonLocked;
    Boolean isEnabled;
    List<UserRole> userRoles;

}
