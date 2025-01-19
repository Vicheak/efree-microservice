package com.efree.order.api.external.productservice;

import com.efree.order.api.base.BaseApi;
import com.efree.order.api.external.productservice.dto.PostStockRequest;
import com.efree.order.api.external.productservice.dto.PromotionResponse;
import com.efree.order.api.external.productservice.dto.PromotionUsageRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "${rest.product-service.base-url}")
public interface ProductServiceRestClient {

    @PostMapping("/api/v1/products/post-stock")
    BaseApi<Object> postStock(@RequestBody @Valid PostStockRequest request);

    @GetMapping("/api/v1/promotions/list/{productId}")
    BaseApi<List<PromotionResponse>> getPromotionsByProductId(@PathVariable String productId);

    @PostMapping("/api/v1/promotions/post-promotion")
    BaseApi<Object> updatePromotionUsage(@RequestBody @Valid PromotionUsageRequest promotionUsageRequest);

}
