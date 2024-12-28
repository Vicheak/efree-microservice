package com.efree.product.api.service.impl;

import com.efree.product.api.dto.request.PostStockRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.dto.response.ProductResponse;
import com.efree.product.api.entity.Product;
import com.efree.product.api.enums.WeightType;
import com.efree.product.api.external.categoryservice.CategoryServiceRestClientConsumer;
import com.efree.product.api.external.categoryservice.dto.CategoryResponseDto;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryServiceRestClientConsumer categoryServiceRestClientConsumer;

    @Transactional
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        //validate request
        validateProductRequest(request, true);
        Product product = productRepository.save(mapToProduct(request));
        ProductResponse productResponse = product.toResponse();
        productResponse.setCategoryResponseDto(request.getCategoryResponseDto());
        return productResponse;
    }

    @Transactional
    @Override
    public Page<ProductResponse> getAllProductsByPagination(int page, int size, String sortBy, String direction) {
        PageRequest pageRequest = buildPageRequest(page, size, sortBy, direction);
        Page<Product> productsPage = productRepository.findAll(pageRequest);
        return productsPage.map(Product::toResponse);
    }

    @Override
    public ProductResponse getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );
        CategoryResponseDto categoryResponseDto =
                categoryServiceRestClientConsumer.loadCategoryById(product.getCategoryId());
        ProductResponse productResponse = product.toResponse();
        productResponse.setCategoryResponseDto(categoryResponseDto);
        return productResponse;
    }

    @Override
    public Page<ProductResponse> getAllBestSellingProducts(int page, int size, String sortBy, String direction) {
        PageRequest pageRequest = buildPageRequest(page, size, sortBy, direction);
        Specification<Product> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isBestSeller"), true); //only best selling
        Page<Product> productsPage = productRepository.findAll(spec, pageRequest);
        return productsPage.map(Product::toResponse);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String categoryId) {
        categoryServiceRestClientConsumer.loadCategoryById(categoryId);
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream().map(Product::toResponse).toList();
    }

    @Override
    public Page<ProductResponse> searchProducts(String keyword, String field, int page, int size, String sortBy, String direction) {
        PageRequest pageRequest = buildPageRequest(page, size, sortBy, direction);
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            if (field != null && keyword != null) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(field)),
                        "%" + keyword.toLowerCase() + "%");
            }
            return null;
        };
        Page<Product> productsPage = productRepository.findAll(spec, pageRequest);
        return productsPage.map(Product::toResponse);
    }

    @Override
    public List<ProductResponse> filterProductsByPrice(BigDecimal from, BigDecimal to) {
        // Validate price range
        if (from.compareTo(to) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid price range");
        }

        // Fetch filtered products
        List<Product> products = productRepository.findByPriceBetween(from, to);

        // Convert to ProductResponse
        return products.stream()
                .map(Product::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public ProductResponse updateProductById(UUID productId, ProductRequest request) {
        validateProductRequest(request, false);

        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );

        // Check for duplicate product name
        if (Objects.nonNull(request.getNameEn()) && !request.getNameEn().isEmpty())
            if (!request.getNameEn().equalsIgnoreCase(product.getNameEn()) &&
                    productRepository.existsByNameEnIsIgnoreCase(request.getNameEn()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Product's name conflicts resource in the system!");

        if (Objects.nonNull(request.getNameKh()) && !request.getNameKh().isEmpty())
            if (!request.getNameKh().equalsIgnoreCase(product.getNameKh()) &&
                    productRepository.existsByNameKhIsIgnoreCase(request.getNameKh()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Product's name conflicts resource in the system!");

        Product updatedProduct = mapToProduct(request);
        updatedProduct.setId(product.getId()); // Retain the same ID for the update
        productRepository.save(updatedProduct);
        ProductResponse productResponse = updatedProduct.toResponse();
        productResponse.setCategoryResponseDto(request.getCategoryResponseDto());
        return productResponse;
    }

    @Transactional
    @Override
    public void deleteProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );

        //check resources in product
        if (!product.getProductImages().isEmpty() || !product.getPromotions().isEmpty() ||
                !product.getFavorites().isEmpty() || !product.getRates().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Product resource cannot be removed, it is used for another resource");
        }

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
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(request.getProductId()))
                );

        if (request.getQuantity() > product.getStockQty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Quantity exceeds stock limit");
        }
        product.setStockQty(product.getStockQty() - request.getQuantity());
        productRepository.save(product);
    }

    private void validateProductRequest(ProductRequest request, Boolean isForNew) {
        // Check for category
        CategoryResponseDto categoryResponseDto =
                categoryServiceRestClientConsumer.loadCategoryById(request.getCategoryId());
        request.setCategoryResponseDto(categoryResponseDto);

        if (isForNew) {
            // Check for duplicate product name
            if (productRepository.existsByNameEnIsIgnoreCase(request.getNameEn())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Product's name conflicts resource in the system!");
            }

            if (Objects.nonNull(request.getNameKh()) && !request.getNameKh().isEmpty()) {
                if (productRepository.existsByNameKhIsIgnoreCase(request.getNameKh()))
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Product's name conflicts resource in the system!");
            }
        }

        // Validate weight type
        if (!WeightType.isValid(request.getWeightType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid weight type. Allowed values: " + WeightType.allowedValues());
        }
    }

    private PageRequest buildPageRequest(int page, int size, String sortBy, String direction) {
        if (page <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Page number must be greater than 0");
        }
        Sort.Direction sortDirection = "asc" .equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy));
    }

    private Product mapToProduct(ProductRequest request) {
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
                .metaTitle(request.getMetaTitle())
                .metaDescription(request.getMetaDescription())
                .isSecondHand(request.getIsSecondHand())
                .secondHandDescription(request.getSecondHandDescription())
                .build();
    }

}