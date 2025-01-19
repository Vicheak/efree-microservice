package com.efree.order.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUnauthDetailResponse {

    String productId;
    Long quantity;
    BigDecimal unitPrice;
    BigDecimal totalPrice;

}
