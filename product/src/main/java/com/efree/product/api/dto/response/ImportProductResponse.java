package com.efree.product.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportProductResponse {

    String productId;
    String nameEn;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String nameKh;
    BigDecimal price;
    Long stockQty;
    Boolean status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
