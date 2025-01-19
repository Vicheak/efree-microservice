package com.efree.product.api.repository;

import com.efree.product.api.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RateRepository extends JpaRepository<Rate, UUID> {

    Optional<Rate> findByUserIdAndProductId(String userId, UUID productId);

    @Query(value = "SELECT COALESCE(SUM(r.rating_value), 0) FROM rates r WHERE r.product_id = :productId",
            nativeQuery = true)
    Long findTotalRatingByProductId(@Param("productId") UUID productId);

    Rate findByProductIdAndUserId(UUID productId, String userId);

}