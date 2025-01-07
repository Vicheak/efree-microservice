package com.efree.payment.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "payments")
public class Payment implements Serializable {

    @Id
    @Column(name = "payment_id")
    String paymentId;

    @Column(name = "payment_amount", nullable = false)
    BigDecimal amount;

    @Column(name = "payment_date", nullable = false)
    LocalDateTime paymentDate;

    @Column(name = "payment_method", length = 100, nullable = false)
    String paymentMethod;

    @Column(name = "payment_platform", nullable = false)
    String platform;

    @Column(name = "transaction_id")
    String transactionId;

    @Column(name = "reference")
    String reference;

    @Column(name = "payment_status", length = 50, nullable = false)
    String paymentStatus;

    @Column(name = "payment_token")
    String paymentToken;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

}
