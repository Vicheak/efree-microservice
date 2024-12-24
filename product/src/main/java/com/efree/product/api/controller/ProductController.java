package com.efree.product.api.controller;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.dto.request.PostStockRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.dto.response.ProductResponse;
import com.efree.product.api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<BaseApi<Object>> createProduct(@RequestBody @Valid ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        BaseApi<Object> api = BaseApi.builder()
                .message("New product has been created successfully")
                .code(HttpStatus.CREATED.value())
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<BaseApi<Object>> getProductById(@PathVariable UUID productId) {
        ProductResponse response = productService.getProductById(productId);
        BaseApi<Object> api = BaseApi.builder()
                .message("Product has been retrieved successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<BaseApi<Object>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nameEn") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<ProductResponse> responses = productService.getAllProducts(page, size, sortBy, direction);

        BaseApi<Object> api = BaseApi.builder()
                .message("All products retrieved successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(responses)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<BaseApi<Object>> updateProductById(@PathVariable UUID productId, @RequestBody @Valid ProductRequest request) {
        ProductResponse response = productService.updateProductById(productId, request);
        BaseApi<Object> api = BaseApi.builder()
                .message("Product has been updated successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<BaseApi<Object>> deleteProductById(@PathVariable UUID productId) {
        productService.deleteProductById(productId);
        BaseApi<Object> api = BaseApi.builder()
                .message("Product has been deleted successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload("No response payload")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @PostMapping("/post-stock")
    public ResponseEntity<BaseApi<Object>> postStock(@RequestBody @Valid PostStockRequest request) {
        productService.postStock(request);
        BaseApi<Object> api = BaseApi.builder()
                .message("Product stock has been posted successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload("No response payload")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }
    @GetMapping("/get-all-by-category/{categoryId}")
    public ResponseEntity<BaseApi<Object>> getAllProductsByCategory(@PathVariable String categoryId) {
        List<ProductResponse> responses = productService.getProductsByCategory(categoryId);
        BaseApi<Object> api = BaseApi.builder()
                .message("Retrieved all products by category successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(responses)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<BaseApi<Object>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String field,
            @RequestParam(defaultValue = "nameEn") String sortBy,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductResponse> responses = productService.searchProducts(keyword, field, sortBy, asc, page, size);

        BaseApi<Object> api = BaseApi.builder()
                .message("Search results retrieved successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(responses.getContent())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping("/filter/price")
    public ResponseEntity<BaseApi<Object>> filterProductsByPrice(
            @RequestParam(required = true) Double from,
            @RequestParam(required = true) Double to
    ) {
        List<ProductResponse> responses = productService.filterProductsByPrice(from, to);

        BaseApi<Object> api = BaseApi.builder()
                .message("Products filtered by price successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(responses)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }


}