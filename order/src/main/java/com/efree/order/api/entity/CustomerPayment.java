package com.efree.order.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "customer_payments")
public class CustomerPayment {

    @Id
    @Column(name = "payment_id")
    String paymentID;

    @Column(name = "order_id", nullable = false)
    String orderId;

    @Column(name = "amount", nullable = false, precision = 2)
    BigDecimal amount;

    @Column(name = "payment_date")
    String paymentDate;

    @Column(name = "payment_method")
    String paymentMethod;

    @Column(name = "transaction_id")
    String transactionId;

    @Column(name = "payment_status")
    Boolean paymentStatus;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;
}
