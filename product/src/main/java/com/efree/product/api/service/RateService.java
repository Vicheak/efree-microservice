package com.efree.product.api.service;

import com.efree.product.api.dto.request.RateRequest;
import com.efree.product.api.dto.response.RateResponse;

public interface RateService {

    /**
     * This method is used to rate a product
     * @param request is the request from client
     */
    void rateProduct(RateRequest request);

    /**
     * This method is used to count total rate of a product
     * @param productId is the request parameter from client
     * @return Long
     */
    Long countRateByProductId(String productId);

    /**
     * This method is used to count rate of a product of a specific user
     * @param productId is the request parameter from client
     * @param userId is the request parameter from client
     * @return RateResponse
     */
    RateResponse countRateByUser(String productId, String userId);

}