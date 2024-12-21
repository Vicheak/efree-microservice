package com.efree.product.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record CategoryResponseDto(String id,
                                  String nameEn,
                                  String nameKh,
                                  String descriptionEn,
                                  String descriptionKh,
                                  @JsonInclude(JsonInclude.Include.NON_NULL)
                                  String imageUrl
) {
}
