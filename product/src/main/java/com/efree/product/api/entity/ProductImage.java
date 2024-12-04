package com.efree.product.api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @Column(name = "image_id")
    String imageId;

    @Column(name = "image_url", nullable = false)
    String imageUrl;

    @Column(name = "description_en", columnDefinition = "TEXT")
    String descriptionEn;

    @Column(name = "description_kh", columnDefinition = "TEXT")
    String descriptionKh;

    @Column(name = "is_based")
    Boolean isBased;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

}
