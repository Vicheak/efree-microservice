package com.efree.product.api.service.impl;

import com.efree.product.api.dto.request.ProductImageRequest;
import com.efree.product.api.dto.response.ProductImageResponse;
import com.efree.product.api.entity.Product;
import com.efree.product.api.entity.ProductImage;
import com.efree.product.api.external.fileservice.FileServiceRestClientConsumer;
import com.efree.product.api.external.fileservice.dto.FileDto;
import com.efree.product.api.repository.ProductImageRepository;
import com.efree.product.api.repository.ProductRepository;
import com.efree.product.api.service.ProductImageService;
import com.efree.product.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProImagesServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final FileServiceRestClientConsumer fileServiceRestClientConsumer;
    private final ProductRepository productRepository;
    private final ProductService productService;
    @Override
    public ProductImageResponse uploadProductImage(MultipartFile fileRequest, String productId, ProductImageRequest request) {
        Product product = productRepository.findById(UUID.fromString(productId)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Product with id not found"))
        );
        FileDto fileDto = fileServiceRestClientConsumer.singleUpload(fileRequest);
        ProductImage productImage = ProductImage.builder()
                .product(product)
                .imageUrl(fileDto.name())
                .descriptionEn(request.getDescriptionEn())
                .descriptionKh(request.getDescriptionKh())
                .isBased(true)
                .build();

        return productImage.toResponse(fileDto.downloadUri());
    }

    @Override
    public ProductImageResponse updateProductImage(MultipartFile fileRequest, String productId, String productImageId) {
        productService.getProductById(UUID.fromString(productId));
        ProductImage productImage = productImageRepository.findById(UUID.fromString(productImageId)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Product image with id not found"))
        );
        FileDto fileDto = fileServiceRestClientConsumer.singleUpload(fileRequest);
        productImage.setImageUrl(fileDto.name());
        return productImage.toResponse(fileDto.downloadUri());
    }

    @Override
    public void deleteByProductIdAndImageId(String productImageId, String productId) {
       productService.getProductById(UUID.fromString(productId));
        ProductImage productImage = productImageRepository.findById(UUID.fromString(productImageId)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Product image with id not found"))
        );
        productImageRepository.deleteByImageIdAndProductId(UUID.fromString(productImageId),UUID.fromString(productId));

    }

    @Override
    public void deleteByProductId(String productId) {
        productService.getProductById(UUID.fromString(productId));
        productImageRepository.deleteByProductId(UUID.fromString(productId));
    }

    @Override
    public Page<ProductImageResponse> getProductImages(String productId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ProductImage> productImages = productImageRepository.findByProductId(UUID.fromString(productId), pageable);

        return productImages.map(productImage ->
                productImage.toResponse(productImage.getImageUrl())
        );
    }


}
