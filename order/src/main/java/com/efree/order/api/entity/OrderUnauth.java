package com.efree.order.api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
@Table(name = "order_unauth")
public class OrderUnauth implements Serializable {

    @Id
    @Column(name = "order_unauth_id")
    String orderUnauthId;

    @Column(name = "user_id", nullable = false)
    String userId;

    @Column(name = "order_date", nullable = false)
    LocalDateTime orderDate;

    @Column(name = "total_amount", nullable = false)
    BigDecimal totalAmount;

    @Column(name = "order_contact", nullable = false)
    String orderContact;

    @OneToMany(mappedBy = "orderUnauth", fetch = FetchType.EAGER)
    List<OrderUnauthDetail> orderUnauthDetails;

}
