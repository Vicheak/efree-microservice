package com.efree.product.api.entity;

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

    //update relationship here bro, many to many
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

}