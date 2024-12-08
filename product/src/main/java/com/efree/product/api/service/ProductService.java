package com.efree.product.api.service;

import com.efree.product.api.dto.request.OrderRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.dto.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(UUID productId);
    List<ProductResponse> getAllProducts();
    ProductResponse updateProduct(UUID productId, ProductRequest request);
    void deleteProduct(UUID productId);
    void StockQty(OrderRequest request);
}
