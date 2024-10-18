package com.efree.user.api.base;

import lombok.Builder;

@Builder
public record FieldError(String fieldName,
                         String message) {
}