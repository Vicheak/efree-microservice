package com.efree.product.api.service.serviceImpl;

import com.efree.product.api.config.ModelMapperConfig;
import com.efree.product.api.dto.request.OrderRequest;
import com.efree.product.api.dto.request.ProductRequest;
import com.efree.product.api.dto.response.ProductResponse;
import com.efree.product.api.dto.response.PromotionResponse;
import com.efree.product.api.entity.Product;
import com.efree.product.api.entity.ProductImage;
import com.efree.product.api.entity.Promotion;
import com.efree.product.api.exception.CustomNotfoundException;
import com.efree.product.api.repository.ProductImageRepository;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.repository.PromotionRepository;
import com.efree.product.api.service.ProductService;
import com.efree.product.api.service.PromotionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = productRepository.save(mapToProduct(request));
        return product.toResponse();
    }

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


    @Override
    public ProductResponse updateProduct(UUID productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomNotfoundException("Product not found"));

        productRepository.save(mapToProduct(request));
        return product.toResponse();
    }

    @Override
    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomNotfoundException("Product not found")
        );
        productRepository.delete(product);
    }

    private Product mapToProduct(ProductRequest request) {


        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .categoryId(request.getCategoryId())
                .userId(request.getUserId())
                .averageRating(request.getAverageRating())
                .brand(request.getBrand())
                .isFeatured(request.getIsFeatured())
                .price(request.getPrice())
                .handlingTime(request.getHandlingTime())
                .isSecondHand(request.getIsSecondHand())
                .isNewArrival(request.getIsNewArrival())
                .dimension(request.getDimension())
                .metaTitle(request.getMetaTitle())
                .metaDescription(request.getMetaDescription())
                .returnPolicy(request.getReturnPolicy())
                .secondHandDescription(request.getSecondHandDescription())
                .shippingClass(request.getShippingClass())
                .shippingFee(request.getShippingFee())
                .stockQty(request.getStockQty())
                .totalReview(request.getTotalReview())
                .status(request.getStatus())
                .totalSale(request.getTotalSale())
                .tags(request.getTags())
                .warrantyPeriod(request.getWarrantyPeriod())
                .weight(request.getWeight())
                .weightType(request.getWeightType())
                .build();
        return product;
    }
    @Override
    public void StockQty(OrderRequest request) {
        Product product = productRepository.findById(UUID.fromString(request.getProductId()))
                .orElseThrow(() -> new CustomNotfoundException("Product not found"));
        if(request.getQuantity() > product.getStockQty()) {
            throw new CustomNotfoundException("Quantity exceeds stock limit");
        }
        product.setStockQty(product.getStockQty()- request.getQuantity());
        productRepository.save(product);
    }

}

