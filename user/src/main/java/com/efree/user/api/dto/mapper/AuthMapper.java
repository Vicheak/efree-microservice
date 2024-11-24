package com.efree.user.api.dto.mapper;

import com.efree.user.api.dto.request.RegisterDto;
import com.efree.user.api.dto.request.TransactionUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    TransactionUserDto fromRegisterDtoToTransactionUserDto(RegisterDto registerDto);

}
