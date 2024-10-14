package com.efree.product.api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_id")
    String id;

    @Column(name = "category_id", nullable = false)
    Long categoryId;

    @Column(name = "seller_id")
    String sellerId;

    @Column(name = "product_name", nullable = false)
    String name;

    @Column(name = "product_description", columnDefinition = "TEXT")
    String description;

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

    @OneToMany(mappedBy = "product")
    List<ProductImage> productImages;

    @OneToMany(mappedBy = "product")
    List<Promotion> promotions;

}