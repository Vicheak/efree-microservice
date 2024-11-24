package com.efree.user.api.dto.mapper;

import com.efree.user.api.dto.request.TransactionUserDto;
import com.efree.user.api.dto.response.AuthProfileUserDto;
import com.efree.user.api.dto.response.AuthUserDto;
import com.efree.user.api.dto.response.UserDto;
import com.efree.user.api.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto fromUserToUserDto(User user);

    List<UserDto> fromUserToUserDto(List<User> users);

    User fromTransactionUserDtoToUser(TransactionUserDto transactionUserDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromTransactionUserDtoToUser(@MappingTarget User user, TransactionUserDto transactionUserDto);

    AuthUserDto fromUserToAuthUserDto(User user);

    AuthProfileUserDto fromUserToAuthProfileUserDto(User user);

}
