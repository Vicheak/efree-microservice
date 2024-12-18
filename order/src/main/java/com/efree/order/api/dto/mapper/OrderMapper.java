package com.efree.order.api.dto.mapper;

import com.efree.order.api.dto.request.OrderRequestDTO;
import com.efree.order.api.dto.response.OrderResponseDTO;
import com.efree.order.api.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "id", source = "entity.id")
    OrderResponseDTO toResponseDTO(Order entity);

    @Mapping(target = "id", ignore = true) // For creating new orders
    Order toEntity(OrderRequestDTO dto);
}
