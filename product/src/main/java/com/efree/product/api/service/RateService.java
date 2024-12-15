package com.efree.product.api.service;

import com.efree.product.api.dto.request.RateRequest;

public interface RateService {

    void toggleRate(RateRequest request);

    Integer countRateByProductId(String productId);

}