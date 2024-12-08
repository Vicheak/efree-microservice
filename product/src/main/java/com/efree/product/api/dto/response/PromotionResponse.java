package com.efree.product.api.dto.response;

import com.efree.product.api.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PromotionResponse {

    UUID id;
    String type;
    Double discount;

    String promoCode;

    Double maxDiscount;

    LocalDateTime startDate;
    LocalDateTime endDate;

    Long usageLimit;
    Long usageCount;
    String applicableCustomer;
    Boolean status;
    ProductResponse product;
}
