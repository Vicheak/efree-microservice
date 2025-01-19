package com.efree.product.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateResponse {

    String rateId;
    String userId;
    String productId;
    Integer ratingValue;

}
