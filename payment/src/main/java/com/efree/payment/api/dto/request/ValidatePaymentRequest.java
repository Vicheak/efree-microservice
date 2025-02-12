package com.efree.payment.api.dto.request;

import lombok.Builder;

@Builder
public record ValidatePaymentRequest(Boolean isPaymentSuccess,
                                     String orderId,
                                     String orderRef,
                                     String paymentId,
                                     String paymentToken) {
}
