package com.efree.product.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteResponse {

    String favoriteId;
    String userId;
    List<ProductResponse> products;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDateTime createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDateTime updatedAt;

}