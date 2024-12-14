package com.efree.fileservice.api.clientcontent.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientContentRequestDto(@NotBlank(message = "Description must not be blank!") String descriptionEn,
                                      @NotNull String descriptionKh,
                                      @NotNull String reference,
                                      @NotNull Boolean isUsed) {
}
