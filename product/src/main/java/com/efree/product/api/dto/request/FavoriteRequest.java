package com.efree.product.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteRequest {

    @NotBlank(message = "User ID must not be blank!")
    String userId;
    @NotNull
    @Size(min = 1, message = "Please add to favorite at least a product!")
    List<@NotBlank(message = "Product ID must not be blank!") String> productIds;

}