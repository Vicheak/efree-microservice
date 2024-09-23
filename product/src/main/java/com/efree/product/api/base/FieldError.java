package com.efree.product.api.base;

import lombok.Builder;

@Builder
public record FieldError(String fieldName,
                         String message) {
}