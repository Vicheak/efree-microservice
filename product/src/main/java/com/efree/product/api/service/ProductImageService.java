package com.efree.product.api.service;

import com.efree.product.api.dto.request.ProductImageRequest;
import com.efree.product.api.dto.response.ProductImageResponse;

import java.util.List;
import java.util.UUID;

public interface ProductImageService {
    ProductImageResponse createImage(ProductImageRequest productImageRequest);
    ProductImageResponse getImageById(UUID id);
    List<ProductImageResponse> getAllImages();
    ProductImageResponse updateImage(UUID id, ProductImageRequest productImageRequest);
    void deleteImage(UUID id);
}

