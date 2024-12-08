package com.efree.product.api.controller;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.dto.request.OrderRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.dto.response.ProductResponse;
import com.efree.product.api.service.ProductService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        BaseApi api = BaseApi.builder()
                .message("Product has created successfully")
                .code(201)
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api,HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID productId) {
        ProductResponse response = productService.getProductById(productId);
        BaseApi api = BaseApi.builder()
                .message("Product has retrieved successfully")
                .code(200)
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> responses = productService.getAllProducts();
        BaseApi api = BaseApi.builder()
                .message("All products has retrieved successfully")
                .code(200)
                .isSuccess(true)
                .payload(responses)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api,HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID productId, @RequestBody ProductRequest request) {
        ProductResponse response = productService.updateProduct(productId, request);
        BaseApi api = BaseApi.builder()
                .message("Product has updated successfully")
                .code(200)
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api,HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
        BaseApi api = BaseApi.builder()
                .message("Product has deleted successfully")
                .code(200)
                .isSuccess(true)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api,HttpStatus.OK);
    }
    @PostMapping("/stock-qty")
    public ResponseEntity<Void> order(@RequestBody OrderRequest request) {
        productService.StockQty(request);
        BaseApi api = BaseApi.builder()
                .message("Product has order successfully")
                .code(200)
                .isSuccess(true)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api,HttpStatus.OK);
    }
}

