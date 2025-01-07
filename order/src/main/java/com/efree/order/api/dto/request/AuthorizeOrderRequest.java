package com.efree.order.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record AuthorizeOrderRequest(@NotNull
                                    @DecimalMin(value = "0.001")
                                    BigDecimal totalAmount,
                                    @NotBlank(message = "Shipping address must not be blank!")
                                    String shippingAddress,
                                    @NotBlank(message = "Billing address must not be blank!")
                                    String billingAddress,
                                    @NotNull
                                    List<@Valid AuthorizeCartRequest> cartList) {
}
