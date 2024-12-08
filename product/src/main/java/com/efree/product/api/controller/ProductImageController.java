package com.efree.product.api.controller;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.dto.request.ProductImageRequest;
import com.efree.product.api.dto.response.ProductImageResponse;
import com.efree.product.api.service.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-images")
@RequiredArgsConstructor
public class ProductImageController {
    private final ProductImageService productImageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductImageResponse> createImage(@RequestParam List<MultipartFile> files,@RequestBody @Valid ProductImageRequest productImageRequest) {
        ProductImageResponse response = productImageService.createImage(productImageRequest);
        BaseApi api = BaseApi.builder()
                .message("Product image has created successfully")
                .code(201)
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImageResponse> getImageById(@PathVariable UUID id) {
        ProductImageResponse response = productImageService.getImageById(id);
        BaseApi api = BaseApi.builder()
                .message("Product image has retrieved successfully")
                .code(200)
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductImageResponse>> getAllImages() {
        List<ProductImageResponse> images = productImageService.getAllImages();
        BaseApi api = BaseApi.builder()
                .message("All product images has retrieved successfully")
                .code(200)
                .isSuccess(true)
                .payload(images)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductImageResponse> updateImage(@PathVariable UUID id, @RequestBody @Valid ProductImageRequest productImageRequest) {
        ProductImageResponse response = productImageService.updateImage(id, productImageRequest);
        BaseApi api = BaseApi.builder()
                .message("Product image has updated successfully")
                .code(200)
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID id) {
        productImageService.deleteImage(id);
        BaseApi api = BaseApi.builder()
                .message("Product image has deleted successfully")
                .code(200)
                .isSuccess(true)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(api,HttpStatus.OK);
    }
}

