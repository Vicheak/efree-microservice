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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String weightType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    BigDecimal weight;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String dimension;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String brand;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String warrantyPeriod;
    Boolean isFeatured;
    Boolean isNewArrival;
    Boolean isBestSeller;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String shippingClass;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String returnPolicy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String metaTitle;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String metaDescription;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean isSecondHand;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String secondHandDescription;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDateTime createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDateTime updatedAt;

}