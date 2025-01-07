package com.efree.order.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AuthorizeCartRequest(@NotBlank(message = "Product ID must not be blank!")
                                   String productId,
                                   @NotNull
                                   String promotionId,
                                   @NotNull
                                   @Min(value = 1, message = "Quantity must not be less than 1")
                                   Long quantity,
                                   @NotNull
                                   @DecimalMin(value = "0.001")
                                   BigDecimal unitPrice,
                                   @NotNull
                                   @DecimalMin(value = "0.001")
                                   BigDecimal totalPrice) {
}
