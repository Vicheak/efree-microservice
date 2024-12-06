package com.efree.order.api.entity;

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
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id")
    String id;

    @Column(name = "customer_id", nullable = false)
    Long categoryId;

    @Column(name = "order_date")
    String orderDate;

    @Column(name = "total_amount", nullable = false, precision = 2)
    BigDecimal totalAmount;

    @Column(name = "payment_status")
    String paymentStatus;

    @Column(name = "order_status")
    String orderStatus;

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    String shippingAddress;

    @Column(name = "billing_address", columnDefinition = "TEXT")
    String billingAddress;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;

    @Column(name = "is_prepared")
    Boolean isPrepared;
}
