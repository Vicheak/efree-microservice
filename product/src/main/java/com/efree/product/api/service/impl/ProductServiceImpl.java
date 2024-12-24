package com.efree.product.api.service.impl;

import com.efree.product.api.external.categoryservice.CategoryClient;
import com.efree.product.api.dto.request.PostStockRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.external.categoryservice.dto.CategoryResponseDto;
import com.efree.product.api.dto.response.ProductResponse;
import com.efree.product.api.entity.Product;
import com.efree.product.api.enums.WeightType;
import com.efree.product.api.exception.CustomNotfoundException;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.service.ProductService;
import com.efree.product.api.utils.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryClient categoryClient;

    @Transactional
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        validateProductRequest(request);

        Product product = productRepository.save(mapToProduct(request));
        return product.toResponse();
    }

    @Override
    public ProductResponse getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return product.toResponse();
    }

    @Transactional
    @Override
    public Page<ProductResponse> getAllProducts(int page, int size, String sortBy, String direction) {
        if (page <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number must be greater than 0");
        }
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy));
        Page<Product> productsPage = productRepository.findAll(pageRequest);
        return productsPage.map(Product::toResponse);
    }


    @Transactional
    @Override
    public ProductResponse updateProductById(UUID productId, ProductRequest request) {
        validateProductRequest(request);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Product updatedProduct = mapToProduct(request);
        updatedProduct.setId(productId); // Retain the same ID for the update
        productRepository.save(updatedProduct);
        return updatedProduct.toResponse();
    }

    @Transactional
    @Override
    public void deleteProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomNotfoundException("Product not found"));
        productRepository.delete(product);
    }

    @Transactional
    @Override
    public void postStock(PostStockRequest request) {
        if (request.getQuantity() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Post quantity must not be ZERO");
        }

        Product product = productRepository.findById(UUID.fromString(request.getProductId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (request.getQuantity() > product.getStockQty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Quantity exceeds stock limit");
        }
        product.setStockQty(product.getStockQty() - request.getQuantity());
        productRepository.save(product);
    }
    @Override
    public List<ProductResponse> getProductsByCategory(String categoryId) {
        getCategoryClient(categoryId);
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream().map(Product::toResponse).toList();
    }

    @Transactional
    @Override
    public Page<ProductResponse> searchProducts(String keyword, String field, String sortBy, boolean asc, int page, int size) {
        if (page <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number must be greater than 0");
        }
        page = page - 1;
        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            if (field != null && keyword != null) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), "%" + keyword.toLowerCase() + "%");
            }
            return null;
        };

        Page<Product> productsPage = productRepository.findAll(spec, pageRequest);

        return productsPage.map(Product::toResponse);
    }

    @Override
    public List<ProductResponse> filterProductsByPrice(Double from, Double to) {
        // Validate price range
        if (from == null || to == null || from > to) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid price range");
        }

        // Fetch filtered products
        List<Product> products = productRepository.findByPriceBetween(from, to);

        // Convert to ProductResponse
        return products.stream()
                .map(Product::toResponse)
                .toList();
    }


    private Product mapToProduct(ProductRequest request) {
        getCategoryClient(request.getCategoryId());
        return Product.builder()
                .categoryId(request.getCategoryId())
                .nameEn(request.getNameEn())
                .nameKh(request.getNameKh())
                .descriptionEn(request.getDescriptionEn())
                .descriptionKh(request.getDescriptionKh())
                .price(request.getPrice())
                .stockQty(request.getStockQty())
                .status(request.getStatus())
                .weightType(request.getWeightType())
                .weight(request.getWeight())
                .dimension(request.getDimension())
                .brand(request.getBrand())
                .warrantyPeriod(request.getWarrantyPeriod())
                .isFeatured(request.getIsFeatured())
                .isNewArrival(request.getIsNewArrival())
                .isBestSeller(request.getIsBestSeller())
                .shippingClass(request.getShippingClass())
                .returnPolicy(request.getReturnPolicy())
                .metaTitle(SlugGenerator.generateSlug(request.getMetaTitle()))
                .metaDescription(SlugGenerator.generateSlug(request.getMetaDescription()))
                .isSecondHand(request.getIsSecondHand())
                .secondHandDescription(request.getSecondHandDescription())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private void validateProductRequest(ProductRequest request) {
        // Check for duplicate product name
        if (productRepository.existsByNameEnOrNameKh(request.getNameEn(), request.getNameKh())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Product name (English or Khmer) already exists.");
        }

        // Validate weight type
        if (!WeightType.isValid(request.getWeightType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid weight type. Allowed values: " + WeightType.allowedValues());
        }
    }
    void getCategoryClient(String id){
        try{
            CategoryResponseDto category = categoryClient.loadCategoryById(id).payload();
            if(category == null || category.id().equalsIgnoreCase("unavailable")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
            }
        }catch (CustomNotfoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Service unavailable");
        }
    }

}
