package com.efree.product.api.repository;

import com.efree.product.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByNameEnOrNameKh(String nameEn, String nameKh);
    List<Product> findByCategoryId(String categoryId);
}