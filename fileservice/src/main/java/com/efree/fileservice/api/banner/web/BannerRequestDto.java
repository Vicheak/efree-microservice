package com.efree.fileservice.api.banner.web;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BannerRequestDto(@NotNull String descriptionEn,
                               @NotNull String descriptionKh,
                               @NotNull String referenceUrl,
                               @NotNull Boolean isUsed) {
}
