package com.efree.order.api.controller;

import com.efree.order.api.base.BaseApi;
import com.efree.order.api.dto.request.OrderRequestDTO;
import com.efree.order.api.dto.response.OrderResponseDTO;
import com.efree.order.api.entity.Order;
import com.efree.order.api.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<BaseApi> createOrder(@RequestBody OrderRequestDTO requestDTO) {
        // Call service to create the order
        OrderResponseDTO createdOrder = orderService.createOrder(requestDTO);

        // Return response with created order information
        BaseApi api = BaseApi.builder()
                .code(201)
                .isSuccess(true)
                .message("Order created successfully")
                .payload(createdOrder)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(api, HttpStatus.CREATED);
    }
}