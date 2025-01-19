package com.efree.product.api.service;

import com.efree.product.api.dto.request.PromotionRequest;
import com.efree.product.api.dto.request.PromotionUsageRequest;
import com.efree.product.api.dto.response.PromotionResponse;

import java.util.List;
import java.util.UUID;

public interface PromotionService {

    /**
     * This method is used to create new promotion of a specific product
     * @param productId is the path parameter from client
     * @param promotionRequest is the request from client
     * @return PromotionResponse
     */
    PromotionResponse createPromotion(UUID productId, PromotionRequest promotionRequest);

    /**
     * This method is used to get all promotions of a specific product
     * @param productId is the path parameter from client
     * @return List<PromotionResponse>
     */
    List<PromotionResponse> getPromotionsByProductId(UUID productId);

    /**
     * This method is used to update promotion of a specific product
     * @param productId is the path parameter from client
     * @param promotionId is the path parameter from client
     * @param promotionRequest is the request from client
     * @return PromotionResponse
     */
    PromotionResponse updatePromotion(UUID productId, UUID promotionId, PromotionRequest promotionRequest);

    /**
     * This method is used to update status of a promotion of a specific product
     * @param productId is the path parameter from client
     * @param promotionId is the path parameter from client
     * @param status is the request parameter from client
     */
    void updateStatus(UUID productId, UUID promotionId, Boolean status);

    /**
     * This method is used to remove a promotion of a specific product
     * @param productId is the path parameter from client
     * @param promotionId is the path parameter from client
     */
    void deletePromotion(UUID productId, UUID promotionId);

    /**
     * This method is used to update a promotion usage of a specific product
     * @param promotionUsageRequest is the request from client
     */
    void updatePromotionUsage(PromotionUsageRequest promotionUsageRequest);

}