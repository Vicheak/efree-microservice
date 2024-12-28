package com.efree.product.api.repository;

import com.efree.product.api.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

    void deleteByProduct_Id(UUID productId);

}