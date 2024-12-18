package com.efree.order.api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "category_id", nullable = false)
    Long categoryId;

    @Column(name = "product_name_en", nullable = false)
    String nameEn;

    @Column(name = "product_name_kh", nullable = false)
    String nameKh;

    @Column(name = "product_description_en", columnDefinition = "TEXT")
    String descriptionEn;

    @Column(name = "product_description_kh", columnDefinition = "TEXT")
    String descriptionKh;

    @Column(name = "product_price", nullable = false, precision = 2)
    BigDecimal price;

    @Column(name = "product_stock_qty", nullable = false)
    Long stockQty;

    @Column(name = "product_avg_rating")
    BigDecimal averageRating;

    @Column(name = "product_total_review")
    Long totalReview;

    @Column(name = "product_status")
    Boolean status;

    @Column(name = "product_weight_type", length = 50)
    String weightType;

    @Column(name = "product_weight")
    BigDecimal weight;

    @Column(name = "product_dimension")
    String dimension;

    @Column(name = "product_total_sale")
    Long totalSale;

    @Column(name = "product_brand", length = 100)
    String brand;

    @Column(name = "product_warranty_period", length = 100)
    String warrantyPeriod;

    @Column(name = "product_is_featured")
    Boolean isFeatured;

    @Column(name = "product_is_new_arrival")
    Boolean isNewArrival;

    @Column(name = "product_is_best_seller")
    Boolean isBestSeller;

    @Column(name = "product_shipping_class", length = 100)
    String shippingClass;

    @Column(name = "product_shipping_fee")
    BigDecimal shippingFee;

    @Column(name = "product_return_policy", columnDefinition = "TEXT")
    String returnPolicy;

    @Column(name = "product_handling_time")
    Integer handlingTime;

    @Column(name = "product_meta_title")
    String metaTitle;

    @Column(name = "product_meta_description", columnDefinition = "TEXT")
    String metaDescription;

    @Column(name = "product_tags", columnDefinition = "TEXT")
    String tags;

    @Column(name = "product_is_second_hand")
    Boolean isSecondHand;

    @Column(name = "product_second_hand_description")
    String secondHandDescription;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;

}
