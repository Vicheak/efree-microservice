package com.efree.product.api.entity;

import com.efree.product.api.dto.response.FavoriteResponse;
import com.efree.product.api.dto.response.ProductResponse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "favorite_id")
    UUID id;

    @Column(name = "user_id", nullable = false)
    String userId;

    @ManyToMany
    @JoinTable(name = "favorites_products", joinColumns = @JoinColumn(name = "favorite_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    List<Product> products;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;

    public FavoriteResponse toResponse() {
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            productResponses.add(product.toResponse());
        }
        return FavoriteResponse.builder()
                .favoriteId(this.id.toString())
                .userId(this.userId)
                .products(productResponses)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

}