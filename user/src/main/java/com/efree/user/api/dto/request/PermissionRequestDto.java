package com.efree.user.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PermissionRequestDto(@NotBlank(message = "User permission must not be blank!")
                                   String permission) {
}
