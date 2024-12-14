package com.efree.fileservice.api.banner.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record BannerResponseDto(String bannerId,
                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                String baseImageUrl,
                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                String baseImageDownloadUrl,
                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                String mobileImageUrl,
                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                String mobileImageDownloadUrl,
                                String descriptionEn,
                                String descriptionKh,
                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                String baseDimension,
                                @JsonInclude(JsonInclude.Include.NON_NULL)
                                String mobileDimension,
                                String referenceUrl,
                                Boolean isUsed) {
}
