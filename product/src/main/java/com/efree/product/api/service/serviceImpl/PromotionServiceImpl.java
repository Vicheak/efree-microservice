package com.efree.product.api.service.serviceImpl;

import com.efree.product.api.dto.request.PromotionRequest;
import com.efree.product.api.dto.response.PromotionResponse;
import com.efree.product.api.entity.Product;
import com.efree.product.api.entity.Promotion;
import com.efree.product.api.exception.CustomNotfoundException;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.repository.PromotionRepository;
import com.efree.product.api.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    @Override
    public PromotionResponse createPromotion(PromotionRequest promotionRequest) {
      Promotion  promotion = promotionRepository.save(mapToPromotion(promotionRequest));
        return promotion.toResponse();
    }

    @Override
    public PromotionResponse getPromotionById(UUID id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow(
                () -> new CustomNotfoundException("Promotion not found")
        );
        return promotion.toResponse();
    }

    @Override
    public List<PromotionResponse> getAllPromotions() {
        return promotionRepository.findAll()
                .stream()
                .map(Promotion::toResponse)
                .toList();
    }

    @Override
    public PromotionResponse updatePromotion(UUID id, PromotionRequest promotionRequest) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new CustomNotfoundException("Promotion not found")
        );
        promotion = promotionRepository.save(mapToPromotion(promotionRequest));
        return promotion.toResponse();
    }

    @Override
    public void deletePromotion(UUID id) {
        promotionRepository.deleteById(id);
    }

    private Promotion mapToPromotion(PromotionRequest promotionRequest){
        Product product = productRepository.findById(UUID.fromString(promotionRequest.getProductId())).orElseThrow(
                () -> new CustomNotfoundException("Product not found")
        );
        Promotion promotion = Promotion.builder()
                .status(promotionRequest.getStatus())
                .type(promotionRequest.getType())
                .applicableCustomer(promotionRequest.getApplicableCustomer())
                .discount(promotionRequest.getDiscount())
                .endDate(promotionRequest.getEndDate())
                .startDate(promotionRequest.getStartDate())
                .maxDiscount(promotionRequest.getMaxDiscount())
                .promoCode(promotionRequest.getPromoCode())
                .usageCount(promotionRequest.getUsageCount())
                .usageLimit(promotionRequest.getUsageLimit())
                .product(product)
                .build();
        return promotion;
    }
}

