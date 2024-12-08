package com.efree.product.api.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseApi<T>(Boolean isSuccess,
                         Integer code,
                         String message,
                         LocalDateTime timestamp,
                         T payload) {
}