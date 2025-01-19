package com.efree.product.api.service;

import com.efree.product.api.dto.request.PostStockRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.dto.response.ImportProductResponse;
import com.efree.product.api.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    /**
     * This method is used to post new product resource into database
     * @param request is the request from client
     * @return ProductResponse
     */
    ProductResponse createProduct(ProductRequest request);

    /**
     * This method is used to get products with pagination
     * @param page is the page number
     * @param size is the amount of resource in a page
     * @param sortBy is the field to be sorted of the resource
     * @param direction is the direction of sorting (ASC or DESC)
     * @return Page<ProductResponse>
     */
    Page<ProductResponse> getAllProductsByPagination(int page, int size, String sortBy, String direction);

    /**
     * This method is used to get product resource by specific id
     * @param productId is the path request from client
     * @return ProductResponse
     */
    ProductResponse getProductById(UUID productId);

    /**
     * This method is used to get all best-selling products with pagination
     * @param page is the page number
     * @param size is the amount of resource in a page
     * @param sortBy is the field to be sorted of the resource
     * @param direction is the direction of sorting (ASC or DESC)
     * @return Page<ProductResponse>
     */
    Page<ProductResponse> getAllBestSellingProducts(int page, int size, String sortBy, String direction);

    /**
     * This method is used to get product resource of a category
     * @param categoryId is the path request from client
     * @return List<ProductResponse>
     */
    List<ProductResponse> getProductsByCategory(String categoryId);

    /**
     * This method is used to search product resource by keyword and criteria
     * @param keyword is the keyword to be searched
     * @param field is the field to be searched
     * @param page is the page number
     * @param size is the amount of resource in a page
     * @param sortBy is the field to be sorted of the resource
     * @param direction is the direction of sorting (ASC or DESC)
     * @return Page<ProductResponse>
     */
    Page<ProductResponse> searchProducts(String keyword, String field, int page, int size, String sortBy, String direction);

    /**
     * This method is used to filter product resource by price range
     * @param from is the start price range
     * @param to is the end price range
     * @return List<ProductResponse>
     */
    List<ProductResponse> filterProductsByPrice(BigDecimal from, BigDecimal to);

    /**
     * This method is used to update product resource by specific id
     * @param productId is the path request from client
     * @param request is the request from client
     * @return ProductResponse
     */
    ProductResponse updateProductById(UUID productId, ProductRequest request);

    /**
     * This method is used to remove product resource by specific id
     * @param productId id the path request from client
     */
    void deleteProductById(UUID productId);

    /**
     * This method is used to import product stock and unit price
     * @param requestedFile is the requested import file (exel format)
     * @return List<ImportProductResponse>
     */
    List<ImportProductResponse> importProduct(MultipartFile requestedFile);

    /**
     * This method is used to post stock of product
     * @param request is the request from client
     */
    void postStock(PostStockRequest request);

}