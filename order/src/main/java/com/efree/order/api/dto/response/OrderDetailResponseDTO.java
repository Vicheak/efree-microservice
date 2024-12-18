package com.efree.order.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponseDTO {
    private String productId;
    private String productNameEN;
    private String productNameKH;
    private Integer quantity;
    private BigDecimal unitPrice;  // Changed to BigDecimal to match product's price type
    private BigDecimal totalPrice;  // Changed to BigDecimal to match order's total price type
}
