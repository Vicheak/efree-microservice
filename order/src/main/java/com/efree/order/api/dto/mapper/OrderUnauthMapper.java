package com.efree.order.api.dto.mapper;

import com.efree.order.api.dto.response.OrderUnauthResponse;
import com.efree.order.api.entity.OrderUnauth;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderUnauthMapper {

    OrderUnauthResponse mapFromOrderUnauthToOrderUnauthResponse(OrderUnauth orderUnauth);

}
