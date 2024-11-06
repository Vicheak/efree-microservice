package com.efree.gateway.dto.mapper;

import com.efree.gateway.dto.request.RegisterDto;
import com.efree.gateway.external.userservice.dto.TransactionUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    TransactionUserDto fromRegisterDtoToTransactionUserDto(RegisterDto registerDto);

}
