package com.efree.product.api.entity;

import com.efree.product.api.dto.response.RateResponse;
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
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rate_id")
    UUID id;

    @Column(name = "user_id", nullable = false)
    String userId;

    @Column(name = "rating_value", nullable = false)
    Integer ratingValue;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    public RateResponse toResponse(){
        return RateResponse.builder()
                .rateId(this.id.toString())
                .userId(userId)
                .productId(this.product.getId().toString())
                .ratingValue(this.ratingValue)
                .build();
    }

}