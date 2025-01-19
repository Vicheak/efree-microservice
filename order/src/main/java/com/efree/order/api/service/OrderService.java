package com.efree.order.api.service;

import com.efree.order.api.dto.request.AuthorizeOrderRequest;
import com.efree.order.api.dto.request.OrderStatusRequest;
import com.efree.order.api.dto.request.ProceedAddToCartRequest;
import com.efree.order.api.dto.request.SaveOrderUnauthRequest;
import com.efree.order.api.dto.response.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    /**
     * For add list of product to shopping cart
     * @param proceedAddToCartRequest is the request from client
     * @return ProceedAddToCartResponse
     */
    ProceedAddToCartResponse proceedAddToCart(ProceedAddToCartRequest proceedAddToCartRequest);

    /**
     * For authorize order process in checkout step
     * @param authUserUuid is the request header from authenticated client
     * @param authorizeOrderRequest is the request from client
     * @return AuthorizeOrderResponse
     */
    AuthorizeOrderResponse authorizeOrder(String authUserUuid, AuthorizeOrderRequest authorizeOrderRequest);

    /**
     * For save order unauth to process order later
     * @param authUserUuid is the request header from authenticated client
     * @param saveOrderUnauthRequest is the request from client
     * @return SaveOrderUnauthResponse
     */
    SaveOrderUnauthResponse saveOrderUnauth(String authUserUuid, SaveOrderUnauthRequest saveOrderUnauthRequest);

    /**
     * This method is used to load order by specific order id
     * @param authUserUuid is the request auth user
     * @param orderId is the path parameter from client
     * @return OrderResponse
     */
    OrderResponse loadOrderByOrderId(String authUserUuid, String orderId);

    /**
     * This method is used to load auth order history by pagination
     * @param authUserUuid is the request auth user
     * @param byLastDay is the number of day of order
     * @param page is the page number
     * @param size is the amount of resource in a page
     * @param sortBy is the field to be sorted of the resource
     * @param direction is the direction of sorting (ASC or DESC)
     * @return Page<OrderResponse>
     */
    Page<OrderResponse> loadAuthOrderHistory(String authUserUuid, int byLastDay, int page, int size, String sortBy, String direction);

    /**
     * This method is used to set prepare order for customer
     * @param orderId is the path parameter from client
     * @param orderStatusRequest is the request from client
     * @return OrderStatusResponse
     */
    OrderStatusResponse setPrepareOrder(String orderId, OrderStatusRequest orderStatusRequest);

    /**
     * This method is used to load order unauth by specific order id
     * @param authUserUuid is the request auth user
     * @param orderId is the path parameter from client
     * @return OrderUnauthResponse
     */
    OrderUnauthResponse loadOrderUnauthByOrderId(String authUserUuid, String orderId);

    /**
     * This method is used to load auth order unauth history by pagination
     * @param authUserUuid is the request auth user
     * @param byLastDay is the number of day of order
     * @param page is the page number
     * @param size is the amount of resource in a page
     * @param sortBy is the field to be sorted of the resource
     * @param direction is the direction of sorting (ASC or DESC)
     * @return Page<OrderUnauthResponse>
     */
    Page<OrderUnauthResponse> loadAuthOrderUnauthHistory(String authUserUuid, int byLastDay, int page, int size, String sortBy, String direction);

    /**
     * This method is used to delete order unauth by specific order id
     * @param authUserUuid is the request auth user
     * @param orderId is the path parameter from client
     */
    void deleteOrderUnauthByOrderId(String authUserUuid, String orderId);

}
