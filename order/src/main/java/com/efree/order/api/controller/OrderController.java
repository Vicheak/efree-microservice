package com.efree.order.api.controller;

import com.efree.order.api.base.BaseApi;
import com.efree.order.api.dto.request.AuthorizeOrderRequest;
import com.efree.order.api.dto.request.OrderStatusRequest;
import com.efree.order.api.dto.request.ProceedAddToCartRequest;
import com.efree.order.api.dto.request.SaveOrderUnauthRequest;
import com.efree.order.api.dto.response.*;
import com.efree.order.api.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@EnableSpringDataWebSupport(pageSerializationMode =
        EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/detail/{orderId}")
    public BaseApi<?> loadOrderByOrderId(@RequestHeader("XUUID") String authUserUuid,
                                         @PathVariable String orderId) {

        OrderResponse orderResponse = orderService.loadOrderByOrderId(authUserUuid, orderId);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("Order has been loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(orderResponse)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/history")
    public BaseApi<?> loadAuthOrderHistory(@RequestHeader("XUUID") String authUserUuid,
                                           @RequestParam(defaultValue = "0") int byLastDay,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "orderDate") String sortBy,
                                           @RequestParam(defaultValue = "desc") String direction) {

        Page<OrderResponse> orderResponsePage = orderService.loadAuthOrderHistory(authUserUuid, byLastDay,
                page, size, sortBy, direction);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("Order has been loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(orderResponsePage)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/status/{orderId}")
    public BaseApi<?> setPrepareOrder(@PathVariable String orderId,
                                      @RequestBody @Valid OrderStatusRequest orderStatusRequest) {

        OrderStatusResponse orderStatusResponse = orderService.setPrepareOrder(orderId, orderStatusRequest);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("Order has been prepared and updated status successfully!")
                .timestamp(LocalDateTime.now())
                .payload(orderStatusResponse)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/detail-unauth/{orderId}")
    public BaseApi<?> loadOrderUnauthByOrderId(@RequestHeader("XUUID") String authUserUuid,
                                               @PathVariable String orderId) {

        OrderUnauthResponse orderUnauthResponse = orderService.loadOrderUnauthByOrderId(authUserUuid, orderId);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("Order unauth has been loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(orderUnauthResponse)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/history-unauth")
    public BaseApi<?> loadAuthOrderUnauthHistory(@RequestHeader("XUUID") String authUserUuid,
                                                 @RequestParam(defaultValue = "0") int byLastDay,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "orderDate") String sortBy,
                                                 @RequestParam(defaultValue = "desc") String direction) {

        Page<OrderUnauthResponse> orderUnauthResponsePage = orderService.loadAuthOrderUnauthHistory(authUserUuid, byLastDay,
                page, size, sortBy, direction);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("Order unauth has been loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(orderUnauthResponsePage)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/remove-unauth/{orderId}")
    public BaseApi<?> setPrepareOrder(@RequestHeader("XUUID") String authUserUuid,
                                      @PathVariable String orderId) {

        orderService.deleteOrderUnauthByOrderId(authUserUuid, orderId);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("Order unauth has been removed successfully!")
                .timestamp(LocalDateTime.now())
                .payload("No response payload")
                .build();
    }

}
