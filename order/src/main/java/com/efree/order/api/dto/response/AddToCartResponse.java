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
public class AddToCartResponse {

    String productId;
    Long quantity;
    BigDecimal unitPrice;
    Boolean isApplyPromotion;
    String promotionId;
    String promoCode;
    String discount;
    BigDecimal subTotalPrice;
    BigDecimal totalDiscount;
    BigDecimal finalSubTotalPrice;

}
