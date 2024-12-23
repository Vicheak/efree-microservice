package com.efree.product.api.service;

import com.efree.product.api.dto.request.PostStockRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(UUID productId);

    Page<ProductResponse> getAllProducts(int page, int size, String sortBy, String direction);

    ProductResponse updateProductById(UUID productId, ProductRequest request);

    void deleteProductById(UUID productId);

    void postStock(PostStockRequest request);
    List<ProductResponse> getProductsByCategory(String categoryId);
    Page<ProductResponse> searchProducts(String keyword, String field, String sortBy, boolean asc, int page, int size);
    List<ProductResponse> filterProductsByPrice(Double from, Double to);

}