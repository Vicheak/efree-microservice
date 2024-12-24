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

    @Column(name = "description_en", columnDefinition = "TEXT")
    String descriptionEn;

    @Column(name = "description_kh", columnDefinition = "TEXT")
    String descriptionKh;

    @Column(name = "is_based")
    Boolean isBased;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    //update bro!
    public ProductImageResponse toResponse(String imageDownloadUrl) {
        return ProductImageResponse.builder()
                .imageId(this.imageId.toString())
                .imageBaseUrl("")
                .imageDownloadUrl(imageDownloadUrl)
                .descriptionEn(this.descriptionEn)
                .descriptionKh(this.descriptionKh)
                .isBased(this.isBased)
                .build();
    }

}
