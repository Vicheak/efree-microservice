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

    @PostMapping("/{productId}")
    public ResponseEntity<BaseApi<Object>> createPromotion(@PathVariable UUID productId, @RequestBody @Valid PromotionRequest promotionRequest) {
        PromotionResponse promotion = promotionService.createPromotion(productId, promotionRequest);
        BaseApi<Object> api = BaseApi.builder()
                .message("New promotion has been posted successfully")
                .code(HttpStatus.CREATED.value())
                .isSuccess(true)
                .payload(promotion)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}/{promotionId}")
    public ResponseEntity<BaseApi<Object>> updatePromotion(@PathVariable UUID productId, @PathVariable UUID promotionId, @RequestBody @Valid PromotionRequest promotionRequest) {
        PromotionResponse promotion = promotionService.updatePromotion(productId, promotionId, promotionRequest);
        BaseApi<Object> api = BaseApi.builder()
                .message("Promotion has been updated successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(promotion)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/{promotionId}")
    public ResponseEntity<BaseApi<Object>> deletePromotion(@PathVariable UUID productId, @PathVariable UUID promotionId) {
        promotionService.deletePromotion(productId, promotionId);
        BaseApi<Object> api = BaseApi.builder()
                .message("Promotion has been deleted successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @PatchMapping("/disable/{productId}/{promotionId}")
    public ResponseEntity<BaseApi<Object>> updateStatus(@PathVariable UUID productId, @PathVariable UUID promotionId, @RequestParam Boolean status) {
        promotionService.updateStatus(productId, promotionId, status);
        BaseApi<Object> api = BaseApi.builder()
                .message("Promotion status has been updated successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<BaseApi<Object>> getPromotionsByProductId(@PathVariable UUID productId) {
        List<PromotionResponse> promotions = promotionService.getPromotionsByProductId(productId);
        BaseApi<Object> api = BaseApi.builder()
                .message("Promotions retrieved successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(promotions)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }
}
