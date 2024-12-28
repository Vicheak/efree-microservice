package com.efree.product.api.controller;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.dto.request.PostStockRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.dto.response.ProductResponse;
import com.efree.product.api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EnableSpringDataWebSupport(pageSerializationMode =
        EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
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

    @GetMapping("/paginate")
    public ResponseEntity<BaseApi<Object>> getAllProductsByPagination(@RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                      @RequestParam(defaultValue = "desc") String direction) {
        Page<ProductResponse> responses = productService.getAllProductsByPagination(page, size, sortBy, direction);
        BaseApi<Object> api = BaseApi.builder()
                .message("All products has been retrieved successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(responses)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<BaseApi<Object>> getProductById(@PathVariable String productId) {
        ProductResponse response = productService.getProductById(UUID.fromString(productId));
        BaseApi<Object> api = BaseApi.builder()
                .message("Product has been retrieved successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping("/best-selling")
    public ResponseEntity<BaseApi<Object>> getAllBestSellingProducts(@RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                     @RequestParam(defaultValue = "desc") String direction) {
        Page<ProductResponse> responses = productService.getAllBestSellingProducts(page, size, sortBy, direction);
        BaseApi<Object> api = BaseApi.builder()
                .message("Products have been retrieved successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(responses)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping("/all-by-category/{categoryId}")
    public ResponseEntity<BaseApi<Object>> getAllProductsByCategory(@PathVariable String categoryId) {
        List<ProductResponse> responses = productService.getProductsByCategory(categoryId);
        BaseApi<Object> api = BaseApi.builder()
                .message("Products have been retrieved by category successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(responses)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<BaseApi<Object>> searchProducts(@RequestParam(required = false) String keyword,
                                                          @RequestParam(required = false) String field,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "createdAt") String sortBy,
                                                          @RequestParam(defaultValue = "desc") String direction) {
        Page<ProductResponse> responses = productService.searchProducts(keyword, field, page, size, sortBy, direction);
        BaseApi<Object> api = BaseApi.builder()
                .message("Search result has been performed successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(responses)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping("/filter/price")
    public ResponseEntity<BaseApi<Object>> filterProductsByPrice(@RequestParam BigDecimal from,
                                                                 @RequestParam BigDecimal to) {
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

    @PutMapping("/{productId}")
    public ResponseEntity<BaseApi<Object>> updateProductById(@PathVariable String productId,
                                                             @RequestBody @Valid ProductRequest request) {
        ProductResponse response = productService.updateProductById(UUID.fromString(productId), request);
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
    public ResponseEntity<BaseApi<Object>> deleteProductById(@PathVariable String productId) {
        productService.deleteProductById(UUID.fromString(productId));
        BaseApi<Object> api = BaseApi.builder()
                .message("Product has been deleted successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload("No response payload")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    //FOR CALL INTERNAL SERVICE
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

}