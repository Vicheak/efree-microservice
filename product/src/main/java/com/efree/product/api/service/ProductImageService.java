package com.efree.product.api.service;

import com.efree.product.api.dto.request.ProductImageRequest;
import com.efree.product.api.dto.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductImageService {

    ProductImageResponse uploadBaseProductImage(MultipartFile fileRequest, UUID productId, ProductImageRequest request);

    List<ProductImageResponse> uploadProductImages(List<MultipartFile> fileRequests, UUID productId);

    List<ProductImageResponse> getProductImages(UUID productId);

    ProductImageResponse updateProductImage(MultipartFile fileRequest, UUID productId, UUID productImageId, ProductImageRequest request);

    void deleteByProductIdAndImageId(UUID productId, UUID productImageId);

    void deleteByProductId(UUID productId);

}
