package com.efree.order.api.controller;

import com.efree.order.api.base.BaseApi;
import com.efree.order.api.dto.request.AuthorizeOrderRequest;
import com.efree.order.api.dto.request.ProceedAddToCartRequest;
import com.efree.order.api.dto.request.SaveOrderUnauthRequest;
import com.efree.order.api.dto.response.AuthorizeOrderResponse;
import com.efree.order.api.dto.response.ProceedAddToCartResponse;
import com.efree.order.api.dto.response.SaveOrderUnauthResponse;
import com.efree.order.api.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/proceed-add-to-cart")
    public BaseApi<?> proceedAddToCart(@RequestBody @Valid ProceedAddToCartRequest proceedAddToCartRequest) {

        ProceedAddToCartResponse proceedAddToCartResponse =
                orderService.proceedAddToCart(proceedAddToCartRequest);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("Proceed add to cart successfully!")
                .timestamp(LocalDateTime.now())
                .payload(proceedAddToCartResponse)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/authorize")
    public BaseApi<?> authorizeOrder(@RequestHeader("XUUID") String authUserUuid,
                                     @RequestBody @Valid AuthorizeOrderRequest authorizeOrderRequest) {

        AuthorizeOrderResponse authorizeOrder =
                orderService.authorizeOrder(authUserUuid, authorizeOrderRequest);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("Order has been authorized successfully!")
                .timestamp(LocalDateTime.now())
                .payload(authorizeOrder)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/save-order-unauth")
    public BaseApi<?> saveOrderUnauth(@RequestHeader("XUUID") String authUserUuid,
                                      @RequestBody @Valid SaveOrderUnauthRequest saveOrderUnauthRequest) {

        SaveOrderUnauthResponse saveOrderUnauthResponse =
                orderService.saveOrderUnauth(authUserUuid, saveOrderUnauthRequest);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("You order has been saved successfully!")
                .timestamp(LocalDateTime.now())
                .payload(saveOrderUnauthResponse)
                .build();
    }

}
