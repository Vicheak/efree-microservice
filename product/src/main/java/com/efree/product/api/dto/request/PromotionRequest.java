package com.efree.product.api.dto.request;


import com.efree.product.api.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionRequest {

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
    String productId;
}
