package com.efree.product.api.entity;

import com.efree.product.api.dto.response.PromotionResponse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "promotion_id")
    UUID id;

    @Column(name = "promotion_type_en", length = 100, nullable = false)
    String typeEn;

    @Column(name = "promotion_type_kh", length = 100, nullable = false)
    String typeKh;

    @Column(name = "promotion_discount")
    Double discount;

    @Column(name = "promo_code", length = 100)
    String promoCode;

    @Column(name = "promotion_max_discount")
    Double maxDiscount;

    @Column(name = "promotion_start_date")
    LocalDateTime startDate;

    @Column(name = "promotion_end_date")
    LocalDateTime endDate;

    @Column(name = "promotion_usage_limit")
    Long usageLimit;

    @Column(name = "promotion_usage_count")
    Long usageCount;

    @Column(name = "applicable_customer", columnDefinition = "TEXT")
    String applicableCustomer;

    @Column(name = "promotion_status")
    Boolean status;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    public PromotionResponse toResponse() {
        return PromotionResponse.builder()
                .promotionId(this.id.toString())
                .typeEn(this.typeEn)
                .typeKh(this.typeKh)
                .discount(this.discount)
                .promoCode(this.promoCode)
                .maxDiscount(this.maxDiscount)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .usageLimit(this.usageLimit)
                .usageCount(this.usageCount)
                .applicableCustomer(this.applicableCustomer)
                .status(this.status)
                .build();
    }

}
