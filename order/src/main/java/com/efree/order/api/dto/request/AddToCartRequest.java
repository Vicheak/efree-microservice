package com.efree.order.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddToCartRequest(@NotBlank(message = "Product ID must not be blank!")
                               String productId,
                               @NotNull
                               @Min(value = 1, message = "Quantity must not be less than 1")
                               Long quantity,
                               @NotNull
                               String promoCode) {
}
