package com.efree.product.api.repository;

import com.efree.product.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    boolean existsByNameEnIsIgnoreCase(String nameEn);
    boolean existsByNameKhIsIgnoreCase(String nameKh);
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByPriceBetween(BigDecimal from, BigDecimal to);

}