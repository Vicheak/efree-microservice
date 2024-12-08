package com.efree.product.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteResponse {
    UUID id;
    String userId;
    List<ProductResponse> products;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
