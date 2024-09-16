package com.efree.category.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record CategoryResponseDto(Long id,
                                  String name,
                                  String description,
                                  @JsonInclude(JsonInclude.Include.NON_NULL)
                                  String imageUrl,
                                  Boolean isActive) {
}
