package com.efree.order.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private String id;
    private String customerId;
    private String orderDate;
    private BigDecimal totalAmount;  // Changed to BigDecimal to match order's totalAmount type
    private String orderStatus;
    private String paymentStatus;
    private String shippingAddress;
    private String billingAddress;
    private List<OrderDetailResponseDTO> orderDetails;
}
