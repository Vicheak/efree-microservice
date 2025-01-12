package com.efree.order.api.entity.primarykey;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUnauthDetailId implements Serializable {

    String orderUnauthId;
    UUID productId;

}