package com.efree.order.api.entity.primarykey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class OrderDetailId implements Serializable {

    @Column(name = "order_id")
    String orderId;

    @Column(name = "product_id")
    UUID productId;

}
