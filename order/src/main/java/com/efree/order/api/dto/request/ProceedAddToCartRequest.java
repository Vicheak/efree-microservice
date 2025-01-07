package com.efree.order.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record ProceedAddToCartRequest(@NotNull
                                      List<@Valid AddToCartRequest> shoppingCart) {
}
