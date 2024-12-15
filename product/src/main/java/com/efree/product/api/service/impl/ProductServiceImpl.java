package com.efree.product.api.service.impl;

import com.efree.product.api.dto.request.PostStockRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.dto.response.ProductResponse;
import com.efree.product.api.entity.Product;
import com.efree.product.api.exception.CustomNotfoundException;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Transactional
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        //check duplicate product name En and Kh (For Kh if empty)

        //check validate weight type (Create Enum, i.e : KG, MG, ..)

        //convert meta title and meta description to slug for elastic search

        Product product = productRepository.save(mapToProduct(request));
        return product.toResponse();
    }

    //bro! please use ResponseStatusException, keep consistent as other service!, update all exception part
    @Override
    public ProductResponse getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomNotfoundException("Product not found"));
        return product.toResponse();
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::toResponse).toList();  // Using toResponse method
    }

    @Transactional
    @Override
    public ProductResponse updateProductById(UUID productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomNotfoundException("Product not found"));

        //check duplicate product name En and Kh (For Kh if empty), but except its original product name

        //check validate weight type (Create Enum, i.e : KG, MG, ..)

        //convert meta title and meta description to slug for elastic search

        productRepository.save(mapToProduct(request));
        return product.toResponse();
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
                .orElseThrow(() -> new CustomNotfoundException("Product not found"));
        if (request.getQuantity() > product.getStockQty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Quantity exceeds stock limit");
        }
        product.setStockQty(product.getStockQty() - request.getQuantity());
        productRepository.save(product);
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