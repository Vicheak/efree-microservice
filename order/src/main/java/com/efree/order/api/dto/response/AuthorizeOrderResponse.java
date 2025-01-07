package com.efree.order.api.dto.response;

import com.efree.order.api.external.paymentservice.dto.PaymentResponse;
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
public class AuthorizeOrderResponse {

    String orderId;
    LocalDateTime orderDate;
    BigDecimal totalAmount;
    String orderStatus;
    Boolean isPrepared;
    String orderContact;
    List<AuthorizeCartResponse> orderDetails;
    PaymentResponse paymentIntent;

}
