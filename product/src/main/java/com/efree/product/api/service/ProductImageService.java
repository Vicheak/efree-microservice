package com.efree.product.api.service;

import com.efree.product.api.dto.request.ProductImageRequest;
import com.efree.product.api.dto.response.ProductImageResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


public interface ProductImageService {
    ProductImageResponse uploadProductImage(MultipartFile fileRequest, String productId, ProductImageRequest request);
    ProductImageResponse updateProductImage(MultipartFile fileRequest, String productId, String productImageId);
    void deleteByProductIdAndImageId(String productImageId, String productId);
    void deleteByProductId(String productId);
    Page<ProductImageResponse> getProductImages(String productId, int page, int size);
}
