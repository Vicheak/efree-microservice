package com.efree.product.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageResponse {

    String imageId;
    String imageBaseUrl;
    String imageDownloadUrl;
    String descriptionEn;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String descriptionKh;
    Boolean isBased;

}