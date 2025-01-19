package com.efree.order.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderStatusRequest(@NotNull Boolean isPrepared,
                                 @NotBlank String orderStatus) {
}
