package com.efree.product.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionResponse {

    String promotionId;
    String typeEn;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String typeKh;
    Double discount;
    String promoCode;
    Double maxDiscount;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Long usageLimit;
    Long usageCount;
    String applicableCustomer;
    Boolean status;

}