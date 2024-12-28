package com.efree.product.api.dto.response;

import com.efree.product.api.external.categoryservice.dto.CategoryResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("category")
    CategoryResponseDto categoryResponseDto;
    String nameEn;
    String nameKh;
    String descriptionEn;
    String descriptionKh;
    BigDecimal price;
    Long stockQty;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    BigDecimal averageRating;
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDateTime createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDateTime updatedAt;

}