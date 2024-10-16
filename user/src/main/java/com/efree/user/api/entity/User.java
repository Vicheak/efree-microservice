package com.efree.user.api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    String id;

    @Column(name = "user_username", length = 50, unique = true, nullable = false)
    String username;

    @Column(name = "user_email", length = 50, unique = true, nullable = false)
    String email;

    @Column(name = "user_password", nullable = false)
    String encryptedPassword;

    @Column(name = "user_gender", length = 20, nullable = false)
    String gender;

    @Column(name = "user_phone_number", length = 30, unique = true, nullable = false)
    String phoneNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "user_date_of_birth")
    LocalDate dateOfBirth;

    @Column(name = "user_photo")
    String photo;

    @CreationTimestamp
    @Column(name = "user_join_date")
    LocalDateTime joinDate;

    @Column(name = "otp_code", length = 6)
    String otpCode;

    Boolean isVerified;

    String verifiedCode;

    String passwordToken;

    Boolean accountNonExpired;

    Boolean accountNonLocked;

    Boolean isEnabled;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<UserRole> userRoles;

}
