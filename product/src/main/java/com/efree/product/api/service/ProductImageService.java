package com.efree.product.api.service;

import com.efree.product.api.dto.request.ProductImageRequest;
import com.efree.product.api.dto.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductImageService {

    /**
     * This method is used to upload base product image of a specific product
     * @param fileRequest is the file request from client
     * @param productId is the path parameter from client
     * @param request is the request from client
     * @return ProductImageResponse
     */
    ProductImageResponse uploadBaseProductImage(MultipartFile fileRequest, UUID productId, ProductImageRequest request);

    /**
     * This method is used to upload product images of a specific product
     * @param fileRequests is the files request from client
     * @param productId is the path parameter from client
     * @return List<ProductImageResponse>
     */
    List<ProductImageResponse> uploadProductImages(List<MultipartFile> fileRequests, UUID productId);

    /**
     * This method is used to get all product images resource of a specific product
     * @param productId is the path parameter from client
     * @return List<ProductImageResponse>
     */
    List<ProductImageResponse> getProductImages(UUID productId);

    /**
     * This method is used to update product image of a specific product
     * @param fileRequest is the file request from client
     * @param productId is the path parameter from client
     * @param productImageId is the path parameter from client
     * @param request is the request from client
     * @return ProductImageResponse
     */
    ProductImageResponse updateProductImage(MultipartFile fileRequest, UUID productId, UUID productImageId, ProductImageRequest request);

    /**
     * This method is used to remove product image of a specific product
     * @param productId is the path parameter from client
     * @param productImageId is the path parameter from client
     */
    void deleteByProductIdAndImageId(UUID productId, UUID productImageId);

    /**
     * This method is used to remove all product image resource of a specific product
     * @param productId is the path parameter from client
     */
    void deleteByProductId(UUID productId);

}
