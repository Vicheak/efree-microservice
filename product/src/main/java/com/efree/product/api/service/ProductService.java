package com.efree.product.api.service;

import com.efree.product.api.dto.request.PostStockRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.dto.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(UUID productId);

    List<ProductResponse> getAllProducts();

    ProductResponse updateProductById(UUID productId, ProductRequest request);

    void deleteProductById(UUID productId);

    void postStock(PostStockRequest request);

}