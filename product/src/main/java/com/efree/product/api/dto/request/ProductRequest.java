package com.efree.product.api.dto.request;

import com.efree.product.api.entity.ProductImage;
import com.efree.product.api.entity.Promotion;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    String categoryId;
    String userId;
    String name;
    String description;
    BigDecimal price;
    Long stockQty;
    BigDecimal averageRating;
    Long totalReview;
    Boolean status;
    String weightType;

    BigDecimal weight;
    String dimension;

    Long totalSale;
    String brand;

    String warrantyPeriod;
    Boolean isFeatured;
    Boolean isNewArrival;
    Boolean isBestSeller;
    String shippingClass;
    BigDecimal shippingFee;
    String returnPolicy;
    Integer handlingTime;
    String metaTitle;
    String metaDescription;
    String tags;
    Boolean isSecondHand;
    String secondHandDescription;
}
