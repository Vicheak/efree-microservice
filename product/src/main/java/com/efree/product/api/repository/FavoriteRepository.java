package com.efree.product.api.repository;

import com.efree.product.api.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    List<Favorite> findAllByUserId(String userId);

    @Modifying
    @Query(value = """
            DELETE FROM favorites_products WHERE favorite_id = :favoriteId AND product_id = :productId
            """, nativeQuery = true)
    void deleteByFavoriteIdAndProductId(@Param("favoriteId") UUID favoriteId, @Param("productId") UUID productId);

}