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
import com.efree.product.api.util.ValueInjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final FileServiceRestClientConsumer fileServiceRestClientConsumer;
    private final ValueInjectUtil valueInjectUtil;

    @Transactional
    @Override
    public ProductImageResponse uploadBaseProductImage(MultipartFile fileRequest, UUID productId, ProductImageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );

        FileDto fileDto = fileServiceRestClientConsumer.singleUpload(fileRequest);
        ProductImage productImage;

        //check if there is already a base image
        Optional<ProductImage> productImageOptional = product.getProductImages().stream()
                .filter(ProductImage::getIsBased)
                .findFirst();
        if (productImageOptional.isPresent()) {
            productImage = productImageOptional.get();
            productImage.setImageUrl(fileDto.name());
        } else {
            productImage = ProductImage.builder()
                    .imageUrl(fileDto.name())
                    .descriptionEn(request.getDescriptionEn())
                    .descriptionKh(request.getDescriptionKh())
                    .isBased(true)
                    .product(product)
                    .build();
        }
        productImage = productImageRepository.save(productImage);
        return this.toResponse(productImage);
    }

    @Transactional
    @Override
    public List<ProductImageResponse> uploadProductImages(List<MultipartFile> fileRequests, UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );

        List<FileDto> fileDtoList = fileServiceRestClientConsumer.multipleUpload(fileRequests);
        List<ProductImage> productImages = fileDtoList.stream()
                .map(fileDto -> ProductImage.builder()
                        .imageUrl(fileDto.name())
                        .descriptionEn("")
                        .descriptionKh("")
                        .isBased(false)
                        .product(product)
                        .build())
                .toList();
        productImages = productImageRepository.saveAll(productImages);
        return productImages.stream().map(this::toResponse).toList();
    }

    @Override
    public List<ProductImageResponse> getProductImages(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );
        return product.getProductImages().stream().map(this::toResponse).toList();
    }

    @Transactional
    @Override
    public ProductImageResponse updateProductImage(MultipartFile fileRequest, UUID productId, UUID productImageId, ProductImageRequest request) {
        productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );
        ProductImage productImage = productImageRepository.findById(productImageId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product image with id, %s has not been found in the system!"
                                        .formatted(productImageId.toString()))
                );
        FileDto fileDto = fileServiceRestClientConsumer.singleUpload(fileRequest);
        productImage.setImageUrl(fileDto.name());
        productImage.setDescriptionEn(request.getDescriptionEn());
        productImage.setDescriptionKh(request.getDescriptionKh());
        productImage = productImageRepository.save(productImage);
        return this.toResponse(productImage);
    }

    @Transactional
    @Override
    public void deleteByProductIdAndImageId(UUID productId, UUID productImageId) {
        productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );
        ProductImage productImage = productImageRepository.findById(productImageId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product image with id, %s has not been found in the system!"
                                        .formatted(productImageId.toString()))
                );
        productImageRepository.delete(productImage);
    }

    @Transactional
    @Override
    public void deleteByProductId(UUID productId) {
        productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id, %s has not been found in the system!"
                                        .formatted(productId.toString()))
                );
        productImageRepository.deleteByProduct_Id(productId);
    }

    public ProductImageResponse toResponse(ProductImage productImage) {
        return ProductImageResponse.builder()
                .imageId(productImage.getImageId().toString())
                .imageBaseUrl(valueInjectUtil.getImageUri(productImage.getImageUrl()))
                .imageDownloadUrl(valueInjectUtil.getDownloadUri(productImage.getImageUrl()))
                .descriptionEn(productImage.getDescriptionEn())
                .descriptionKh(productImage.getDescriptionKh())
                .isBased(productImage.getIsBased())
                .build();
    }

}
