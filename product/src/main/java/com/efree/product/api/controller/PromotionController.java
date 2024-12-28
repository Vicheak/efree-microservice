package com.efree.product.api.controller;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.dto.request.PromotionRequest;
import com.efree.product.api.dto.request.PromotionUsageRequest;
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
    public ResponseEntity<BaseApi<Object>> createPromotion(@PathVariable String productId,
                                                           @RequestBody @Valid PromotionRequest promotionRequest) {
        PromotionResponse promotion = promotionService.createPromotion(UUID.fromString(productId), promotionRequest);
        BaseApi<Object> api = BaseApi.builder()
                .message("New promotion has been posted successfully")
                .code(HttpStatus.CREATED.value())
                .isSuccess(true)
                .payload(promotion)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.CREATED);
    }

    @GetMapping("/list/{productId}")
    public ResponseEntity<BaseApi<Object>> getPromotionsByProductId(@PathVariable String productId) {
        List<PromotionResponse> promotions = promotionService.getPromotionsByProductId(UUID.fromString(productId));
        BaseApi<Object> api = BaseApi.builder()
                .message("Promotions has been retrieved successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(promotions)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @PutMapping("/{productId}/{promotionId}")
    public ResponseEntity<BaseApi<Object>> updatePromotion(@PathVariable String productId,
                                                           @PathVariable String promotionId,
                                                           @RequestBody @Valid PromotionRequest promotionRequest) {
        PromotionResponse promotion = promotionService.updatePromotion(UUID.fromString(productId),
                UUID.fromString(promotionId), promotionRequest);
        BaseApi<Object> api = BaseApi.builder()
                .message("Promotion has been updated successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(promotion)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @PutMapping("/disable/{productId}/{promotionId}")
    public ResponseEntity<BaseApi<Object>> updateStatus(@PathVariable String productId,
                                                        @PathVariable String promotionId,
                                                        @RequestParam Boolean status) {
        promotionService.updateStatus(UUID.fromString(productId), UUID.fromString(promotionId), status);
        BaseApi<Object> api = BaseApi.builder()
                .message("Promotion status has been updated successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload("No response payload")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/{promotionId}")
    public ResponseEntity<BaseApi<Object>> deletePromotion(@PathVariable String productId,
                                                           @PathVariable String promotionId) {
        promotionService.deletePromotion(UUID.fromString(productId), UUID.fromString(promotionId));
        BaseApi<Object> api = BaseApi.builder()
                .message("Promotion has been deleted successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload("No response payload")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    //FOR CALL INTERNAL SERVICE
    @PostMapping("/post-promotion")
    public ResponseEntity<BaseApi<Object>> updatePromotionUsage(@RequestBody @Valid PromotionUsageRequest promotionUsageRequest) {
        promotionService.updatePromotionUsage(promotionUsageRequest);
        BaseApi<Object> api = BaseApi.builder()
                .message("Promotion usage has been posted successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload("No response payload")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

}