package com.efree.category.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record CategoryResponseDto(String id,
                                  String nameEn,
                                  String nameKh,
                                  @JsonInclude(JsonInclude.Include.NON_NULL)
                                  String descriptionEn,
                                  @JsonInclude(JsonInclude.Include.NON_NULL)
                                  String descriptionKh,
                                  @JsonInclude(JsonInclude.Include.NON_NULL)
                                  String imageUrl,
                                  @JsonInclude(JsonInclude.Include.NON_NULL)
                                  String downloadUrl,
                                  Boolean isActive) {
}
