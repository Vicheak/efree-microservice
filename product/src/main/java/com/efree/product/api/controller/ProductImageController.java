package com.efree.product.api.controller;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.dto.request.ProductImageRequest;
import com.efree.product.api.dto.response.ProductImageResponse;
import com.efree.product.api.service.ProductImageService;
import com.efree.product.api.util.AppGlobalUtil;
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

    @PostMapping(value = "/single/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseApi<Object>> uploadBaseProductImage(@RequestPart("file") MultipartFile file,
                                                                  @PathVariable String productId,
                                                                  @RequestPart("request") String request) {
        ProductImageRequest productImageRequest =
                AppGlobalUtil.convertStringToObject(request, ProductImageRequest.class);
        ProductImageResponse response =
                productImageService.uploadBaseProductImage(file, UUID.fromString(productId), productImageRequest);
        BaseApi<Object> api = BaseApi.builder()
                .message("Post product image successfully")
                .code(HttpStatus.CREATED.value())
                .isSuccess(true)
                .payload(response)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.CREATED);
    }

    @PostMapping(value = "/multiple/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseApi<Object>> uploadProductImages(@RequestPart("files") List<MultipartFile> files,
                                                               @PathVariable String productId) {
        List<ProductImageResponse> responses =
                productImageService.uploadProductImages(files, UUID.fromString(productId));
        BaseApi<Object> api = BaseApi.builder()
                .message("Post product images successfully")
                .code(HttpStatus.CREATED.value())
                .isSuccess(true)
                .payload(responses)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.CREATED);
    }

    @GetMapping("/list/{productId}")
    public ResponseEntity<BaseApi<Object>> getProductImages(@PathVariable String productId) {
        List<ProductImageResponse> responses = productImageService.getProductImages(UUID.fromString(productId));
        BaseApi<Object> api = BaseApi.builder()
                .message("Product images retrieved successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload(responses)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @PutMapping(value = "/{productId}/{imageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseApi<Object>> updateProductImage(@RequestParam("file") MultipartFile file,
                                                              @PathVariable("productId") String productId,
                                                              @PathVariable("imageId") String imageId,
                                                              @RequestPart("request") String request) {
        ProductImageRequest productImageRequest =
                AppGlobalUtil.convertStringToObject(request, ProductImageRequest.class);
        ProductImageResponse response =
                productImageService.updateProductImage(file, UUID.fromString(productId),
                        UUID.fromString(imageId), productImageRequest);
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
    public ResponseEntity<BaseApi<Object>> deleteByProductIdAndImageId(@PathVariable("productId") String productId,
                                                                       @PathVariable("imageId") String imageId) {
        productImageService.deleteByProductIdAndImageId(UUID.fromString(productId), UUID.fromString(imageId));
        BaseApi<Object> api = BaseApi.builder()
                .message("Delete product image successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload("No response payload")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<BaseApi<Object>> deleteByProductId(@PathVariable("productId") String productId) {
        productImageService.deleteByProductId(UUID.fromString(productId));
        BaseApi<Object> api = BaseApi.builder()
                .message("Delete product images successfully")
                .code(HttpStatus.OK.value())
                .isSuccess(true)
                .payload("No response payload")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

}
