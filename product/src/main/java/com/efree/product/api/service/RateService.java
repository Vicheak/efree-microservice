package com.efree.product.api.service;

import com.efree.product.api.dto.request.RateRequest;

public interface RateService {

    void rateProduct(RateRequest request);

    Long countRateByProductId(String productId);

}