package com.efree.product.api.repository;

import com.efree.product.api.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {
    List<Promotion> findByProductId(UUID productId);


}