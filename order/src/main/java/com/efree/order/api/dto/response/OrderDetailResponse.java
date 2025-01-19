package com.efree.order.api.dto.response;

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
public class OrderDetailResponse {

    String productId;
    Long quantity;
    BigDecimal unitPrice;
    BigDecimal totalPrice;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
