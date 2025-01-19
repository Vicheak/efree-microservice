package com.efree.order.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {

    String paymentId;
    BigDecimal amount;
    LocalDateTime paymentDate;
    String paymentMethod;
    String platform;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String transactionId;
    String paymentStatus;

}
