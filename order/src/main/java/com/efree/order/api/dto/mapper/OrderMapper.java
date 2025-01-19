package com.efree.order.api.dto.mapper;

import com.efree.order.api.dto.response.OrderResponse;
import com.efree.order.api.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

     OrderResponse mapFromOrderToOrderResponse(Order order);

}
