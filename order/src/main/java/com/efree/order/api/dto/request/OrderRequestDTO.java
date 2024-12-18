package com.efree.order.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private String customerId;
    private String productId;
    private List<ProductRequestDTO> products;  // List of products and quantities
    private String shippingAddress;
    private String billingAddress;
}