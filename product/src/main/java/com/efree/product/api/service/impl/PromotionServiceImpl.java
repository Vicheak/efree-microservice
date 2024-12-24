package com.efree.product.api.service.impl;

import com.efree.product.api.dto.request.PromotionRequest;
import com.efree.product.api.dto.response.PromotionResponse;
import com.efree.product.api.entity.Product;
import com.efree.product.api.entity.Promotion;
import com.efree.product.api.exception.CustomNotfoundException;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.repository.PromotionRepository;
import com.efree.product.api.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public PromotionResponse createPromotion(UUID productId, PromotionRequest promotionRequest) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomNotfoundException("Product with ID " + productId + " not found"));

        Promotion promotion = mapToPromotion(promotionRequest);
        promotion.setProduct(product);

        Promotion savedPromotion = promotionRepository.save(promotion);

        return savedPromotion.toResponse();
    }

    @Override
    @Transactional
    public PromotionResponse updatePromotion(UUID productId, UUID promotionId, PromotionRequest promotionRequest) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomNotfoundException("Product with ID " + productId + " not found"));

        Promotion promotion = promotionRepository.findById(promotionId).orElseThrow(() ->
                new CustomNotfoundException("Promotion with ID " + promotionId + " not found"));

        promotion.setTypeEn(promotionRequest.getTypeEn());
        promotion.setTypeKh(promotionRequest.getTypeKh());
        promotion.setDiscount(promotionRequest.getDiscount());
        promotion.setMaxDiscount(promotionRequest.getMaxDiscount());
        promotion.setEndDate(promotionRequest.getEndDate());
        promotion.setUsageLimit(promotionRequest.getUsageLimit());
        promotion.setApplicableCustomer(promotionRequest.getApplicableCustomer());
        promotion.setStatus(promotionRequest.getStatus());

        Promotion updatedPromotion = promotionRepository.save(promotion);

        return updatedPromotion.toResponse();
    }

    @Override
    @Transactional
    public void deletePromotion(UUID productId, UUID promotionId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomNotfoundException("Product with ID " + productId + " not found"));

        Promotion promotion = promotionRepository.findById(promotionId).orElseThrow(() ->
                new CustomNotfoundException("Promotion with ID " + promotionId + " not found"));

        promotionRepository.delete(promotion);
    }

    @Override
    @Transactional
    public void updateStatus(UUID productId, UUID promotionId, Boolean status) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomNotfoundException("Product with ID " + productId + " not found"));

        Promotion promotion = promotionRepository.findById(promotionId).orElseThrow(() ->
                new CustomNotfoundException("Promotion with ID " + promotionId + " not found"));

        promotion.setStatus(status);
        promotionRepository.save(promotion);
    }

    @Override
    public List<PromotionResponse> getPromotionsByProductId(UUID productId) {
        List<Promotion> promotions = promotionRepository.findByProductId(productId);

        if (promotions.isEmpty()) {
            throw new CustomNotfoundException("No promotions found for the given product ID.");
        }
        return promotions.stream()
                .map(Promotion::toResponse)
                .toList();
    }


    private Promotion mapToPromotion(PromotionRequest promotionRequest) {
        String code = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
        return Promotion.builder()
                .typeEn(promotionRequest.getTypeEn())
                .typeKh(promotionRequest.getTypeKh())
                .discount(promotionRequest.getDiscount())
                .promoCode(code)
                .status(promotionRequest.getStatus())
                .maxDiscount(promotionRequest.getMaxDiscount())
                .startDate(LocalDateTime.now())
                .endDate(promotionRequest.getEndDate())
                .usageLimit(promotionRequest.getUsageLimit())
                .applicableCustomer(promotionRequest.getApplicableCustomer())
                .build();
    }

}
