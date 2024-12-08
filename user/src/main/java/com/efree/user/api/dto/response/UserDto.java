package com.efree.user.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record UserDto(String uuid,
                      String username,
                      String email,
                      String gender,
                      String phoneNumber,
                      LocalDate dateOfBirth,
                      LocalDateTime joinDate,
                      Boolean isVerified,
                      Boolean accountNonExpired,
                      Boolean accountNonLocked,
                      Boolean isEnabled,
                      @JsonInclude(JsonInclude.Include.NON_NULL)
                      String imageUrl,
                      @JsonInclude(JsonInclude.Include.NON_NULL)
                      String downloadUrl,
                      List<UserRoleDto> userRoles) {
}
