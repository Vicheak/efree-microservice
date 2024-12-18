package com.efree.category.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CategoryRequestDto(@NotBlank(message = "Category's name must not be blank!")
                                 @Size(max = 100, message = "Category's name must not be greater than 100 characters!")
                                 String nameEn,
                                 String nameKh,
                                 String descriptionEn,
                                 String descriptionKh,
                                 @NotNull(message = "Active field cannot be null!")
                                 Boolean isActive) {
}
