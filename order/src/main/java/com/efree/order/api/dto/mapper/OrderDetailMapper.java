package com.efree.order.api.dto.mapper;

import com.efree.order.api.dto.request.OrderDetailRequestDTO;
import com.efree.order.api.dto.response.OrderDetailResponseDTO;
import com.efree.order.api.entity.OrderDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailResponseDTO toResponseDTO(OrderDetail entity);

    OrderDetail toEntity(OrderDetailRequestDTO dto);
}
