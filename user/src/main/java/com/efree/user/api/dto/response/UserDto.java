package com.efree.user.api.dto.response;

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
                      List<UserRoleDto> userRoles) {
}
