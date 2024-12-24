package com.efree.product.api.repository;

import com.efree.product.api.dto.response.ProductImageResponse;
import com.efree.product.api.entity.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
    Page<ProductImage> findByProductId(UUID productId, Pageable pageable);
    void deleteByProductId(UUID productId);
    void deleteByImageIdAndProductId(UUID productImageId, UUID productId);
}