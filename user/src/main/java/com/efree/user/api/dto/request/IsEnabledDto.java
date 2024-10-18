package com.efree.user.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record IsEnabledDto(@NotNull(message = "User status must not be null!")
                           Boolean isEnabled) {
}
