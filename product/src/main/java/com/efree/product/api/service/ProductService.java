package com.efree.product.api.service;

import com.efree.product.api.dto.request.PostStockRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    Page<ProductResponse> getAllProductsByPagination(int page, int size, String sortBy, String direction);

    ProductResponse getProductById(UUID productId);

    Page<ProductResponse> getAllBestSellingProducts(int page, int size, String sortBy, String direction);

    List<ProductResponse> getProductsByCategory(String categoryId);

    Page<ProductResponse> searchProducts(String keyword, String field, int page, int size, String sortBy, String direction);

    List<ProductResponse> filterProductsByPrice(BigDecimal from, BigDecimal to);

    ProductResponse updateProductById(UUID productId, ProductRequest request);

    void deleteProductById(UUID productId);

    void postStock(PostStockRequest request);

//    void updateStock(PostStockRequest request);

//    ImportProductResponse importProduct(MultipartFile requestedFile);

}