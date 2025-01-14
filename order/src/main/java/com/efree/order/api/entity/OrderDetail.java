package com.efree.order.api.entity;

import com.efree.order.api.entity.primarykey.OrderDetailId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@IdClass(OrderDetailId.class)
@Table(name = "order_details")
public class OrderDetail implements Serializable {

    @Id
    @Column(name = "order_id")
    String orderId;

    @Id
    @Column(name = "product_id")
    UUID productId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    Order order;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    Product product;

    @Column(name = "quantity", nullable = false)
    Long quantity;

    @Column(name = "unit_price", nullable = false)
    BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false)
    BigDecimal totalPrice;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;

}
