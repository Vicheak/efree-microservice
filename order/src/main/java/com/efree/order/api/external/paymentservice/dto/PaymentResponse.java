package com.efree.order.api.external.paymentservice.dto;

import lombok.Builder;

@Builder
public record PaymentResponse(String status,
                              String message,
                              String paymentSessionId,
                              String paymentSessionUrl,
                              Long expiresAt,
                              String paymentIntent,
                              String currency,
                              Boolean allowPromotionCode,
                              String clientReferenceId,
                              String customer,
                              String customerEmail,
                              String invoice,
                              String billingAddress,
                              String shippingDetail,
                              Long shippingCost,
                              String submitType,
                              String mode) {
}