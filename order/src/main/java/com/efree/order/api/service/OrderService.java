package com.efree.order.api.service;

import com.efree.order.api.dto.request.OrderRequestDTO;
import com.efree.order.api.dto.response.OrderResponseDTO;
import com.efree.order.api.entity.Order;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO requestDTO);
}
