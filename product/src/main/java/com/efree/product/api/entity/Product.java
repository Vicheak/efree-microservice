package com.efree.product.api.entity;

import com.efree.product.api.dto.response.ProductImageResponse;
import com.efree.product.api.dto.response.ProductResponse;
import com.efree.product.api.dto.response.PromotionResponse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
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
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    UUID id;

    @Column(name = "category_id", nullable = false)
    String categoryId;
    @Column(name = "user_id", nullable = false)
    String userId;
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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    List<ProductImage> productImages;

    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    List<Promotion> promotions;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Rate> rates = new ArrayList<>();

    @ManyToMany(mappedBy = "products")
    List<Favorite> favorites = new ArrayList<>();


    public ProductResponse toResponse() {
        List<ProductImageResponse> imageResponse = new ArrayList<>();
        List<PromotionResponse> promotionResponses = new ArrayList<>();
        if (productImages != null && !productImages.isEmpty()) {
            for (ProductImage image : productImages) {
                imageResponse.add(image.toResponse());
            }
        } else {
            imageResponse = null;
        }
        if (promotions != null && !promotions.isEmpty()) {
            for (Promotion promotion : promotions) {
                promotionResponses.add(promotion.toResponseWithoutProduct()); // Calls the adjusted method
            }
        } else {
            promotionResponses = null;
        }
        return ProductResponse.builder()
                .productId(this.id)
                .categoryId(this.categoryId)
                .userId(this.userId)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .stockQty(this.stockQty)
                .averageRating(this.averageRating)
                .totalReview(this.totalReview)
                .status(this.status)
                .weightType(this.weightType)
                .weight(this.weight)
                .dimension(this.dimension)
                .totalSale(this.totalSale)
                .brand(this.brand)
                .warrantyPeriod(this.warrantyPeriod)
                .isFeatured(this.isFeatured)
                .isNewArrival(this.isNewArrival)
                .shippingClass(this.shippingClass)
                .shippingFee(this.shippingFee)
                .returnPolicy(this.returnPolicy)
                .handlingTime(this.handlingTime)
                .metaTitle(this.metaTitle)
                .metaDescription(this.metaDescription)
                .tags(this.tags)
                .isSecondHand(this.isSecondHand)
                .secondHandDescription(this.secondHandDescription)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .productImages(imageResponse)
                .promotions(promotionResponses)
                .build();
    }
    public ProductResponse toResponseOnlyProduct() {

        return ProductResponse.builder()
                .productId(this.id)
                .categoryId(this.categoryId)
                .userId(this.userId)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .stockQty(this.stockQty)
                .averageRating(this.averageRating)
                .totalReview(this.totalReview)
                .status(this.status)
                .weightType(this.weightType)
                .weight(this.weight)
                .dimension(this.dimension)
                .totalSale(this.totalSale)
                .brand(this.brand)
                .warrantyPeriod(this.warrantyPeriod)
                .isFeatured(this.isFeatured)
                .isNewArrival(this.isNewArrival)
                .shippingClass(this.shippingClass)
                .shippingFee(this.shippingFee)
                .returnPolicy(this.returnPolicy)
                .handlingTime(this.handlingTime)
                .metaTitle(this.metaTitle)
                .metaDescription(this.metaDescription)
                .tags(this.tags)
                .isSecondHand(this.isSecondHand)
                .secondHandDescription(this.secondHandDescription)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }



}
