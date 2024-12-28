package com.efree.product.api.service.impl;

import com.efree.product.api.dto.request.PromotionRequest;
import com.efree.product.api.dto.request.PromotionUsageRequest;
import com.efree.product.api.dto.response.PromotionResponse;
import com.efree.product.api.entity.Product;
import com.efree.product.api.entity.Promotion;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.repository.PromotionRepository;
import com.efree.product.api.service.PromotionService;
import com.efree.product.api.util.AppGlobalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    @Transactional
    @Override
    public PromotionResponse createPromotion(UUID productId, PromotionRequest promotionRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );
        Promotion promotion = mapToPromotion(promotionRequest);
        promotion.setPromoCode(AppGlobalUtil.generatePromoCode("", 6));
        promotion.setStartDate(LocalDateTime.now());
        promotion.setUsageCount(0L);
        promotion.setProduct(product);
        Promotion savedPromotion = promotionRepository.save(promotion);
        return savedPromotion.toResponse();
    }

    @Override
    public List<PromotionResponse> getPromotionsByProductId(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );
        return product.getPromotions().stream().map(Promotion::toResponse).toList();
    }

    @Transactional
    @Override
    public PromotionResponse updatePromotion(UUID productId, UUID promotionId, PromotionRequest promotionRequest) {
        productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Promotion with id, %s has not been found in the system!"
                                        .formatted(promotionId.toString()))
                );
        promotion.setTypeEn(promotionRequest.getTypeEn());
        promotion.setTypeKh(promotionRequest.getTypeKh());
        promotion.setDiscount(promotionRequest.getDiscount());
        promotion.setMaxDiscount(promotionRequest.getMaxDiscount());
        promotion.setEndDate(promotionRequest.getEndDate());
        promotion.setUsageLimit(promotionRequest.getUsageLimit());
        promotion.setApplicableCustomer(promotionRequest.getApplicableCustomer());
        promotion.setStatus(promotionRequest.getStatus());
        promotionRepository.save(promotion);
        return promotion.toResponse();
    }

    @Transactional
    @Override
    public void updateStatus(UUID productId, UUID promotionId, Boolean status) {
        productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Promotion with id, %s has not been found in the system!"
                                        .formatted(promotionId.toString()))
                );
        promotion.setStatus(status);
        promotionRepository.save(promotion);
    }

    @Transactional
    @Override
    public void deletePromotion(UUID productId, UUID promotionId) {
        productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Promotion with id, %s has not been found in the system!"
                                        .formatted(promotionId.toString()))
                );
        promotionRepository.delete(promotion);
    }

    @Transactional
    @Override
    public void updatePromotionUsage(PromotionUsageRequest promotionUsageRequest) {
        productRepository.findById(UUID.fromString(promotionUsageRequest.getProductId()))
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(promotionUsageRequest.getProductId())
                        ));
        Promotion promotion = promotionRepository.findById(UUID.fromString(promotionUsageRequest.getPromotionId()))
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Promotion with id, %s has not been found in the system!"
                                        .formatted(promotionUsageRequest.getPromotionId()))
                );
        if (promotion.getUsageLimit() < promotion.getUsageCount() + 1) {
            throw new ResponseStatusException(HttpStatus.OK,
                    "Sorry, this promotion exceeds the usage limit!");
        }
        promotion.setUsageCount(promotion.getUsageCount() + 1);
        promotionRepository.save(promotion);
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
