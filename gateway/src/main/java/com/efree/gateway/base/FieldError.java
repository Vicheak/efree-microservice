package com.efree.gateway.base;

import lombok.Builder;

@Builder
public record FieldError(String fieldName,
                         String message) {
}