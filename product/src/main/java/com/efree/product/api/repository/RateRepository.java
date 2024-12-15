package com.efree.product.api.repository;

import com.efree.product.api.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RateRepository extends JpaRepository<Rate, UUID> {

    Optional<Rate> findByUserIdAndProductId(String userId, UUID productId);

    void deleteByUserIdAndProductId(String userId, UUID productId);

    Integer countByProductId(UUID productId);

}