package com.efree.product.api.service.impl;

import com.efree.product.api.dto.request.RateRequest;
import com.efree.product.api.dto.response.RateResponse;
import com.efree.product.api.entity.Product;
import com.efree.product.api.entity.Rate;
import com.efree.product.api.external.userservice.UserServiceRestClientConsumer;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.repository.RateRepository;
import com.efree.product.api.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

    private final ProductRepository productRepository;
    private final RateRepository rateRepository;
    private final UserServiceRestClientConsumer userServiceRestClientConsumer;

    @Transactional
    @Override
    public void rateProduct(RateRequest request) {
        // Check if user does not exist
        userServiceRestClientConsumer.loadUserByUuid(request.getUserId());

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
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Product with id, %s has not been found in the system!"
                                            .formatted(request.getProductId()))
                    );

            Rate newRate = Rate.builder()
                    .userId(request.getUserId())
                    .product(product)
                    .ratingValue(request.getRatingValue())
                    .build();

            rateRepository.save(newRate);
        }
    }

    @Override
    public Long countRateByProductId(String productId) {
        productRepository.findById(UUID.fromString(productId))
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId))
                );
        return rateRepository.findTotalRatingByProductId(UUID.fromString(productId));
    }

    @Override
    public RateResponse countRateByUser(String productId, String userId) {
        productRepository.findById(UUID.fromString(productId))
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId))
                );
        Rate rate = rateRepository.findByProductIdAndUserId(UUID.fromString(productId), userId);
        if (Objects.isNull(rate)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Your requested product rate has not been found in the system!");
        }
        return rate.toResponse();
    }

}