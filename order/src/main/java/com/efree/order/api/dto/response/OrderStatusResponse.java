package com.efree.order.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusResponse {

    String orderId;
    Boolean isPrepared;
    String orderStatus;

}
