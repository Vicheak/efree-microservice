package com.efree.product.api.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionRequest {

    @NotBlank(message = "Promotion Type must not be blank!")
    String typeEn;
    @NotNull
    String typeKh;
    @NotNull
    @Positive
    @DecimalMax(value = "100.00")
    Double discount; //as percentage
    @NotNull
    @Positive
    Double maxDiscount; //as decimal price
    @NotNull
    LocalDateTime endDate;
    @NotNull
    Long usageLimit;
    @NotBlank
    String applicableCustomer;
    @NotNull
    Boolean status;

}