package com.efree.product.api.repository;

import com.efree.product.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    boolean existsByNameEnOrNameKh(String nameEn, String nameKh);
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByPriceBetween(Double from, Double to);
}