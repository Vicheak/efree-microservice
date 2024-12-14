package com.efree.fileservice.base;

import lombok.Builder;

@Builder
public record FieldError(String fieldName,
                         String message) {
}