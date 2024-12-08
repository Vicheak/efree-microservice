package com.efree.product.api.controller;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.dto.request.PromotionRequest;
import com.efree.product.api.dto.response.PromotionResponse;
import com.efree.product.api.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping
    public ResponseEntity<PromotionResponse> createPromotion(@RequestBody @Valid PromotionRequest promotionRequest) {
        PromotionResponse promotion = promotionService.createPromotion(promotionRequest);
        BaseApi api = BaseApi.builder()
                .message("Promotion has created successfully")
                .code(201)
                .isSuccess(true)
                .payload(promotion)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionResponse> getPromotionById(@PathVariable UUID id) {
        PromotionResponse promotion = promotionService.getPromotionById(id);
        BaseApi api = BaseApi.builder()
                .message("Promotion has retrieved successfully")
                .code(200)
                .isSuccess(true)
                .payload(promotion)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PromotionResponse>> getAllPromotions() {
        List<PromotionResponse> promotions = promotionService.getAllPromotions();
        BaseApi api = BaseApi.builder()
                .message("All promotions has retrieved successfully")
                .code(200)
                .isSuccess(true)
                .payload(promotions)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionResponse> updatePromotion(@PathVariable UUID id, @RequestBody @Valid PromotionRequest promotionRequest) {
        PromotionResponse promotion = promotionService.updatePromotion(id, promotionRequest);
        BaseApi api = BaseApi.builder()
                .message("Promotion has updated successfully")
                .code(200)
                .isSuccess(true)
                .payload(promotion)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable UUID id) {
        promotionService.deletePromotion(id);
        BaseApi api = BaseApi.builder()
                .message("Promotion has deleted successfully")
                .code(200)
                .isSuccess(true)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api, HttpStatus.OK);
    }
}

