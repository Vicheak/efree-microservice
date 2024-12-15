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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    //bro! please follow in API spec doc, I rejected merge request

    //this one use request path like this /api/v1/promotions/{productUuid}
    @PostMapping
    public ResponseEntity<BaseApi<Object>> createPromotion(@RequestBody @Valid PromotionRequest promotionRequest) {
        PromotionResponse promotion = promotionService.createPromotion(promotionRequest);
        BaseApi<Object> api = BaseApi.builder()
                .message("New promotion has been posted successfully")
                .code(HttpStatus.CREATED.value())
                .isSuccess(true)
                .payload(promotion)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.CREATED);
    }

    //wrong request path, follow in doc bro!
    @PutMapping("/{id}")
    public ResponseEntity<BaseApi<Object>> updatePromotion(@PathVariable UUID id, @RequestBody @Valid PromotionRequest promotionRequest) {
        PromotionResponse promotion = promotionService.updatePromotion(id, promotionRequest);
        BaseApi<Object> api = BaseApi.builder()
                .message("Promotion has been updated successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(promotion)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    //please update your code bro, follow requirements, not just CRUD

}