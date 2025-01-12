package com.efree.product.api.dto.request;

import com.efree.product.api.external.categoryservice.dto.CategoryResponseDto;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

    @NotBlank(message = "Category ID must not be blank!")
    String categoryId;
    @NotBlank(message = "Product Name must not be blank!")
    String nameEn;
    @NotNull
    String nameKh;
    @NotBlank
    String descriptionEn;
    @NotNull
    String descriptionKh;
    @NotNull
    @DecimalMin(value = "0.001")
    BigDecimal price;
    @NotNull
    @PositiveOrZero
    Long stockQty;
    @NotNull
    Boolean status;
    @Length(max = 50)
    String weightType;
    @Positive
    BigDecimal weight;
    String dimension;
    @Length(max = 100)
    String brand;
    @Length(max = 100)
    String warrantyPeriod;
    @NotNull
    Boolean isFeatured;
    @NotNull
    Boolean isNewArrival;
    @NotNull
    Boolean isBestSeller;
    @Length(max = 100)
    String shippingClass;
    String returnPolicy;
    String metaTitle;
    String metaDescription;
    Boolean isSecondHand;
    String secondHandDescription;

    //for mapping
    CategoryResponseDto categoryResponseDto;

}