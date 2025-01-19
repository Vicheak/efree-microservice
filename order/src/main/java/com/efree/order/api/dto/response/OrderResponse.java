package com.efree.order.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {

    String orderId;
    String userId;
    LocalDateTime orderDate;
    BigDecimal totalAmount;
    String paymentStatus;
    String orderStatus;
    String shippingAddress;
    String billingAddress;
    Boolean isPrepared;
    String orderContact;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<OrderDetailResponse> orderDetails;
    List<PaymentResponse> payments;

}
