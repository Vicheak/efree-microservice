package com.efree.user.api.repository;

import com.efree.user.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("UPDATE User AS u SET u.verifiedCode = :verifiedCode WHERE u.uuid = :uuid AND u.email = :email")
    void updateVerifiedCode(String uuid, String email, String verifiedCode);

    Optional<User> findByEmailAndVerifiedCodeAndIsVerifiedFalseAndIsEnabledFalse(String email, String verifiedCode);

    Optional<User> findByEmailAndIsVerifiedTrueAndIsEnabledTrue(String email);

}
