package com.efree.order.api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @Column(name = "order_id")
    String orderId;

    @Column(name = "user_id", nullable = false)
    String userId;

    @Column(name = "order_date", nullable = false)
    LocalDateTime orderDate;

    @Column(name = "total_amount", nullable = false)
    BigDecimal totalAmount;

    @Column(name = "payment_status", length = 50)
    String paymentStatus;

    @Column(name = "order_status", length = 50, nullable = false)
    String orderStatus;

    @Column(name = "shipping_address", columnDefinition = "TEXT", nullable = false)
    String shippingAddress;

    @Column(name = "billing_address", columnDefinition = "TEXT", nullable = false)
    String billingAddress;

    @Column(name = "is_prepared", nullable = false)
    Boolean isPrepared;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    List<Payment> payments;

}
