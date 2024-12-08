package com.efree.product.api.dto.response;

import com.efree.product.api.entity.ProductImage;
import com.efree.product.api.entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    UUID productId;
    String categoryId;
    String userId;
    String name;
    String description;

    BigDecimal price;
    Long stockQty;

    BigDecimal averageRating;

    Long totalReview;
    Boolean status;
    String weightType;

    BigDecimal weight;
    String dimension;

    Long totalSale;
    String brand;

    String warrantyPeriod;
    Boolean isFeatured;
    Boolean isNewArrival;
    Boolean isBestSeller;

    String shippingClass;
    BigDecimal shippingFee;

    String returnPolicy;
    Integer handlingTime;
    String metaTitle;

    String metaDescription;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String tags;
    Boolean isSecondHand;
    String secondHandDescription;
    List<ProductImageResponse> productImages;
    List<PromotionResponse> promotions;
}
