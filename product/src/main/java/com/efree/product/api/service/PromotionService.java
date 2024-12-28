package com.efree.product.api.service;

import com.efree.product.api.dto.request.PromotionRequest;
import com.efree.product.api.dto.request.PromotionUsageRequest;
import com.efree.product.api.dto.response.PromotionResponse;

import java.util.List;
import java.util.UUID;

public interface PromotionService {

    PromotionResponse createPromotion(UUID productId, PromotionRequest promotionRequest);

    List<PromotionResponse> getPromotionsByProductId(UUID productId);

    PromotionResponse updatePromotion(UUID productId, UUID promotionId, PromotionRequest promotionRequest);

    void updateStatus(UUID productId, UUID promotionId, Boolean status);

    void deletePromotion(UUID productId, UUID promotionId);

    void updatePromotionUsage(PromotionUsageRequest promotionUsageRequest);

}