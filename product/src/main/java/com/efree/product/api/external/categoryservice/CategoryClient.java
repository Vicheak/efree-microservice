package com.efree.product.api.external.categoryservice;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.external.categoryservice.dto.CategoryResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.time.LocalDateTime;

@FeignClient(name = "category-service", url = "${category-url.url}")
public interface CategoryClient {
    @CircuitBreaker(name = "categoryClient", fallbackMethod = "categoryFallback")
    @GetMapping("/api/v1/categories/{id}")
    BaseApi<CategoryResponseDto> loadCategoryById(@PathVariable String id);
    default BaseApi<CategoryResponseDto> categoryFallback(String id, Throwable throwable) {
        return BaseApi.<CategoryResponseDto>builder()
                .message("Service unavailable, fallback response.")
                .code(200)
                .isSuccess(false)
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .payload(new CategoryResponseDto(
                        "unavailable",
                        "unavailable",
                        "unavailable",
                        "unavailable",
                        "unavailable",
                        "unavailable"))
                .timestamp(LocalDateTime.now())
                .build();
    }
}
