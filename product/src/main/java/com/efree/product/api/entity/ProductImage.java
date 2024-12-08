package com.efree.product.api.entity;

import com.efree.product.api.dto.response.ProductImageResponse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "image_id")
    UUID imageId;

    @Column(name = "image_url", nullable = false)
    String imageUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductImageResponse toResponse() {
        return ProductImageResponse.builder()
                .imageId(this.imageId)
                .imageUrl(this.imageUrl)
                .description(this.description)
                .build();
    }


}
