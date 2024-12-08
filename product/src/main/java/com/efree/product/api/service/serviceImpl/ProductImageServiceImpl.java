package com.efree.product.api.service.serviceImpl;

import com.efree.product.api.dto.request.ProductImageRequest;
import com.efree.product.api.dto.response.ProductImageResponse;
import com.efree.product.api.entity.Product;
import com.efree.product.api.entity.ProductImage;
import com.efree.product.api.exception.CustomNotfoundException;
import com.efree.product.api.repository.ProductImageRepository;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductImageResponse createImage(ProductImageRequest productImageRequest) {
        ProductImage productImage = new ProductImage();
        productImage.setImageUrl(productImageRequest.getImageUrl());
        productImage.setDescription(productImageRequest.getDescription());
        Product product = productRepository.findById(UUID.fromString(productImageRequest.getProductId())).orElseThrow(
                () -> new CustomNotfoundException("Product not found")
        );
        productImage.setProduct(product);
        productImage = productImageRepository.save(productImage);
        return productImage.toResponse();
    }

    @Override
    public ProductImageResponse getImageById(UUID id) {
        ProductImage productImage = productImageRepository.findById(id).orElseThrow(() -> new ServiceException("Product Image not found"));
        return productImage.toResponse();
    }

    @Override
    public List<ProductImageResponse> getAllImages() {
        return productImageRepository.findAll()
                .stream()
                .map(ProductImage::toResponse)
                .toList();
    }

    @Override
    public ProductImageResponse updateImage(UUID id, ProductImageRequest productImageRequest) {
        ProductImage productImage = productImageRepository.findById(id).orElseThrow(() -> new ServiceException("Product Image not found"));
        productImage.setImageUrl(productImageRequest.getImageUrl());
        productImage.setDescription(productImageRequest.getDescription());
        Product product = productRepository.findById(UUID.fromString(productImageRequest.getProductId())).orElseThrow(
                () -> new CustomNotfoundException("Product not found")
        );
        productImage.setProduct(product);
        productImage = productImageRepository.save(productImage);
        return productImage.toResponse();
    }

    @Override
    public void deleteImage(UUID id) {
        productImageRepository.deleteById(id);
    }

}

