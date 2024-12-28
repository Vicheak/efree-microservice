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
    @NotBlank
    @Length(max = 50)
    String weightType;
    @NotNull
    @Positive
    BigDecimal weight;
    @NotBlank
    String dimension;
    @NotBlank
    @Length(max = 100)
    String brand;
    @NotBlank
    @Length(max = 100)
    String warrantyPeriod;
    @NotNull
    Boolean isFeatured;
    @NotNull
    Boolean isNewArrival;
    @NotNull
    Boolean isBestSeller;
    @NotBlank
    @Length(max = 100)
    String shippingClass;
    @NotBlank
    String returnPolicy;
    @NotBlank
    String metaTitle;
    @NotBlank
    String metaDescription;
    @NotNull
    Boolean isSecondHand;
    @NotNull
    String secondHandDescription;

    //for mapping
    CategoryResponseDto categoryResponseDto;

}