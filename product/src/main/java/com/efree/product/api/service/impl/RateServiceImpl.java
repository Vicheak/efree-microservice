package com.efree.product.api.service.impl;

import com.efree.product.api.dto.request.RateRequest;
import com.efree.product.api.entity.Product;
import com.efree.product.api.entity.Rate;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.repository.RateRepository;
import com.efree.product.api.service.ProductService;
import com.efree.product.api.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

    private final RateRepository rateRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Transactional
    @Override
    public void toggleRate(RateRequest request) {
        if (request.getRatingValue() >= 0) {
            // Validate rating value
            if (request.getRatingValue() < 1 || request.getRatingValue() > 5) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Rating value must be between 1 and 5.");
            }

            // Check for existing rate
            Rate existingRate = rateRepository.findByUserIdAndProductId(request.getUserId(), UUID.fromString(request.getProductId()))
                    .orElse(null);

            if (existingRate != null) {
                // Update existing rate
                existingRate.setRatingValue(request.getRatingValue());
                rateRepository.save(existingRate);
            } else {
                // Create a new rate
                Product product = productRepository.findById(UUID.fromString(request.getProductId()))
                        .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + request.getProductId()));

                Rate newRate = Rate.builder()
                        .userId(request.getUserId())
                        .product(product)
                        .ratingValue(request.getRatingValue())
                        .build();

                rateRepository.save(newRate);
            }
        } else {
            // Unrate logic
            rateRepository.deleteByUserIdAndProductId(request.getUserId(), UUID.fromString(request.getProductId()));
        }
    }

    @Override
    public Integer countRateByProductId(String productId) {
        productService.getProductById(UUID.fromString(productId));
        return rateRepository.countByProductId(UUID.fromString(productId));
    }

}