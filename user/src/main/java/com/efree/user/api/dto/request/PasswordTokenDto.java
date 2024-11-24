package com.efree.user.api.dto.request;

import lombok.Builder;

@Builder
public record PasswordTokenDto(String message,
                               String token) {
}
