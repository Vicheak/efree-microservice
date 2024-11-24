package com.efree.user.api.repository;

import com.efree.user.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("UPDATE User AS u SET u.verifiedCode = :verifiedCode WHERE u.email = :email")
    void updateVerifiedCode(String email, String verifiedCode);

    Optional<User> findByEmailAndVerifiedCodeAndIsVerifiedFalseAndIsEnabledFalse(String email, String verifiedCode);

}
