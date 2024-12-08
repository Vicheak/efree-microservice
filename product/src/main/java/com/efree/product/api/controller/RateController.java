package com.efree.product.api.controller;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.dto.request.RateRequest;
import com.efree.product.api.service.RateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/rates")
@RequiredArgsConstructor
public class RateController {
    private final RateService rateService;
    @PutMapping
    @Operation(summary = "rate and unrate")
    public ResponseEntity<BaseApi> toggleRate(RateRequest request){
        rateService.toggleRate(request);
        BaseApi api = BaseApi.builder()
                .message("Rate successful")
                .code(201)
                .isSuccess(true)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api, HttpStatus.CREATED);
    }
    @GetMapping("/count-by-product")
    @Operation(summary = "count rate of a product")
    public ResponseEntity<BaseApi> countRate(String productId){
        Integer rate = rateService.countRateByProductId(productId);
        BaseApi api = BaseApi.builder()
                .message("Count successful")
                .code(200)
                .isSuccess(true)
                .timestamp(LocalDateTime.now())
                .payload(rate)
                .build();
        return new ResponseEntity(api, HttpStatus.OK);
    }
}
