package com.efree.product.api.controller;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.dto.request.RateRequest;
import com.efree.product.api.dto.response.RateResponse;
import com.efree.product.api.service.RateService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/rates")
@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;

    @PostMapping
    @Operation(summary = "rate product")
    public ResponseEntity<BaseApi<Object>> rateProduct(@RequestBody @Valid RateRequest request) {
        rateService.rateProduct(request);
        BaseApi<Object> api = BaseApi.builder()
                .message("Review product successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload("No response payload")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping("/count-by-product")
    @Operation(summary = "count rate of a product")
    public ResponseEntity<BaseApi<Object>> countRate(@RequestParam String productId) {
        Long rate = rateService.countRateByProductId(productId);
        BaseApi<Object> api = BaseApi.builder()
                .message("Count review successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .timestamp(LocalDateTime.now())
                .payload(rate)
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping("/count-by-product-user")
    @Operation(summary = "count rate of a product of a specific user")
    public ResponseEntity<BaseApi<Object>> countRateByUser(@RequestParam String productId,
                                                           @RequestParam String userId) {
        RateResponse rateResponse = rateService.countRateByUser(productId, userId);
        BaseApi<Object> api = BaseApi.builder()
                .message("Count review of a product successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .timestamp(LocalDateTime.now())
                .payload(rateResponse)
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

}