package com.efree.product.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(name = "description_en", columnDefinition = "TEXT")
    String descriptionEn;

    @Column(name = "description_kh", columnDefinition = "TEXT")
    String descriptionKh;

    @Column(name = "is_based")
    Boolean isBased;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

}
