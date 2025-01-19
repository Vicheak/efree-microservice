package com.efree.order.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.*;

@Builder
public record SaveOrderUnauthRequest(@NotNull
                                     @DecimalMin(value = "0.001")
                                     BigDecimal totalAmount,
                                     @NotNull
                                     List<@Valid SaveOrderUnauthCartRequest> cartList) {
}
