package com.efree.order.api.service;

import com.efree.order.api.dto.request.AuthorizeOrderRequest;
import com.efree.order.api.dto.request.ProceedAddToCartRequest;
import com.efree.order.api.dto.request.SaveOrderUnauthRequest;
import com.efree.order.api.dto.response.AuthorizeOrderResponse;
import com.efree.order.api.dto.response.ProceedAddToCartResponse;
import com.efree.order.api.dto.response.SaveOrderUnauthResponse;

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

}
