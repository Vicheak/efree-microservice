package com.efree.product.api.service;

import com.efree.product.api.dto.request.PromotionRequest;
import com.efree.product.api.dto.response.PromotionResponse;

import java.util.List;
import java.util.UUID;

public interface PromotionService {
    PromotionResponse createPromotion(PromotionRequest promotionRequest);
    PromotionResponse getPromotionById(UUID id);
    List<PromotionResponse> getAllPromotions();
    PromotionResponse updatePromotion(UUID id, PromotionRequest promotionRequest);
    void deletePromotion(UUID id);
}

