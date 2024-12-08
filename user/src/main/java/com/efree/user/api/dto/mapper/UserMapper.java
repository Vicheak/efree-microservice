package com.efree.user.api.dto.mapper;

import com.efree.user.api.dto.request.TransactionUserDto;
import com.efree.user.api.dto.response.AuthProfileUserDto;
import com.efree.user.api.dto.response.AuthUserDto;
import com.efree.user.api.dto.response.UserDto;
import com.efree.user.api.entity.User;
import com.efree.user.api.util.ValueInjectUtil;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    protected ValueInjectUtil valueInjectUtil;

    @Autowired
    public void setValueInjectUtil(ValueInjectUtil valueInjectUtil) {
        this.valueInjectUtil = valueInjectUtil;
    }

    @Mapping(target = "imageUrl", expression = "java(valueInjectUtil.getImageUri(user.getImageUrl()))")
    @Mapping(target = "downloadUrl", expression = "java(valueInjectUtil.getDownloadUri(user.getImageUrl()))")
    @Mapping(target = "authorities", expression = "java(valueInjectUtil.buildAuthorityResponse(user.getUserAuthorities()))")
    public abstract UserDto fromUserToUserDto(User user);

    public abstract List<UserDto> fromUserToUserDto(List<User> users);

    public abstract User fromTransactionUserDtoToUser(TransactionUserDto transactionUserDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void fromTransactionUserDtoToUser(@MappingTarget User user, TransactionUserDto transactionUserDto);

    public abstract AuthUserDto fromUserToAuthUserDto(User user);

    @Mapping(target = "imageUrl", expression = "java(valueInjectUtil.getImageUri(user.getImageUrl()))")
    @Mapping(target = "downloadUrl", expression = "java(valueInjectUtil.getDownloadUri(user.getImageUrl()))")
    public abstract AuthProfileUserDto fromUserToAuthProfileUserDto(User user);

}
