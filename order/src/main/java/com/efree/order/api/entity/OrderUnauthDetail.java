package com.efree.order.api.entity;

import com.efree.order.api.entity.primarykey.OrderUnauthDetailId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@IdClass(OrderUnauthDetailId.class)
@Table(name = "order_unauth_details")
public class OrderUnauthDetail implements Serializable {

    @Id
    @Column(name = "order_unauth_id")
    String orderUnauthId;

    @Id
    @Column(name = "product_id")
    UUID productId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_unauth_id", insertable = false, updatable = false)
    OrderUnauth orderUnauth;

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

}
