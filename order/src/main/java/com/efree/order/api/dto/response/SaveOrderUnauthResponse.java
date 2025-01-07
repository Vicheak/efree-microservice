package com.efree.order.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaveOrderUnauthResponse {

    String orderUnauthId;
    LocalDateTime orderDate;
    BigDecimal totalAmount;
    String orderContact;
    List<SaveOrderUnauthCartResponse> orderUnauthDetails;

}
