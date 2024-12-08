package com.efree.product.api.repository;

import com.efree.product.api.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface RateRepository extends JpaRepository<Rate, UUID> {
    Optional<Rate> findByUserIdAndProductId(String userId, UUID productId);
    void deleteByUserIdAndProductId(String userId, UUID productId);
    Integer countByProductId(UUID productId);
}
