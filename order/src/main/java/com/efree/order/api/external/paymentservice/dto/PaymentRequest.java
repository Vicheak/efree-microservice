package com.efree.order.api.external.paymentservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentRequest(@NotBlank(message = "Order ID must not be blank!")
                             String orderId,
                             @NotBlank(message = "Payment ID must not be blank!")
                             String paymentId,
                             @NotBlank(message = "Payment Token must not be blank!")
                             String paymentToken,
                             @NotBlank(message = "Order Ref must not be blank!")
                             String orderRef,
                             @NotNull(message = "Please include valid payment amount!'")
                             @DecimalMin(value = "0.001")
                             BigDecimal paymentAmount,
                             @NotBlank(message = "Payment currency must not be blank!")
                             String paymentCurrency) {
}
