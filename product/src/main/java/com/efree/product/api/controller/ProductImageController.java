package com.efree.product.api.controller;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.dto.request.ProductImageRequest;
import com.efree.product.api.dto.response.ProductImageResponse;
import com.efree.product.api.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/product-images")
@RequiredArgsConstructor
public class ProductImageController {
    private final ProductImageService productImageService;

    @PostMapping(value = "/single/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProductImage(@RequestParam("file") MultipartFile file, @PathVariable String productId, @RequestBody ProductImageRequest request) {
        ProductImageResponse response = productImageService.uploadProductImage(file, productId, request);
        BaseApi<Object> api = BaseApi.builder()
                .message("Post product image successfully")
                .code(HttpStatus.CREATED.value())
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{productId}/{imageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProductImage(@RequestParam("file") MultipartFile file, @PathVariable("productId") String productId, @PathVariable("imageId") String imageId) {
        ProductImageResponse response = productImageService.updateProductImage(file, productId, imageId);
        BaseApi<Object> api = BaseApi.builder()
                .message("Update product image successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }
    @DeleteMapping("/{productId}/{imageId}")
    public ResponseEntity<?> deleteByProductIdAndImageId(@PathVariable("productId") String productId, @PathVariable("imageId") String imageId) {
        productImageService.deleteByProductIdAndImageId(productId, imageId);
        BaseApi<Object> api = BaseApi.builder()
                .message("Delete product image successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(null)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteByProductId(@PathVariable("productId") String productId) {
        productImageService.deleteByProductId(productId);
        BaseApi<Object> api = BaseApi.builder()
                .message("Delete product images successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(null)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @GetMapping("/list/{productId}")
    public ResponseEntity<BaseApi<Object>> getProductImages(
            @PathVariable String productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductImageResponse> responses = productImageService.getProductImages(productId, page, size);

        BaseApi<Object> api = BaseApi.builder()
                .message("Product images retrieved successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(responses.getContent())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(api, HttpStatus.OK);
    }

}
