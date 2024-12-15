package com.efree.product.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    String productId;
    String categoryId;
    String nameEn;
    String nameKh;
    String descriptionEn;
    String descriptionKh;
    BigDecimal price;
    Long stockQty;
    BigDecimal averageRating;
    Long totalReview;
    Boolean status;
    String weightType;
    BigDecimal weight;
    String dimension;
    String brand;
    String warrantyPeriod;
    Boolean isFeatured;
    Boolean isNewArrival;
    Boolean isBestSeller;
    String shippingClass;
    String returnPolicy;
    String metaTitle;
    String metaDescription;
    Boolean isSecondHand;
    String secondHandDescription;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}