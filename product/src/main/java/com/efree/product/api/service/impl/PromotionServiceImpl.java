package com.efree.product.api.service.impl;

import com.efree.product.api.dto.request.PromotionRequest;
import com.efree.product.api.dto.response.PromotionResponse;
import com.efree.product.api.entity.Promotion;
import com.efree.product.api.exception.CustomNotfoundException;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.repository.PromotionRepository;
import com.efree.product.api.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public PromotionResponse createPromotion(PromotionRequest promotionRequest) {
        //convert discount to decimal percentage

        //set start date

        //generate promo code with 4 chars (character and digit randomly)

        Promotion promotion = promotionRepository.save(mapToPromotion(promotionRequest));
        return promotion.toResponse();
    }

    @Transactional
    @Override
    public PromotionResponse updatePromotion(UUID id, PromotionRequest promotionRequest) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new CustomNotfoundException("Promotion not found"));
        promotionRepository.save(mapToPromotion(promotionRequest));
        return promotion.toResponse();
    }

    private Promotion mapToPromotion(PromotionRequest promotionRequest) {
        return Promotion.builder()
                .typeEn(promotionRequest.getTypeEn())
                .typeKh(promotionRequest.getTypeKh())
                .discount(promotionRequest.getDiscount())
                .maxDiscount(promotionRequest.getMaxDiscount())
                .endDate(promotionRequest.getEndDate())
                .usageLimit(promotionRequest.getUsageLimit())
                .applicableCustomer(promotionRequest.getApplicableCustomer())
                .status(promotionRequest.getStatus())
                .build();
    }

}
