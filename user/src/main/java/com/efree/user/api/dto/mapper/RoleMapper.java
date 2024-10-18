package com.efree.user.api.dto.mapper;

import com.efree.user.api.dto.response.RoleDto;
import com.efree.user.api.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto fromRoleToRoleDto(Role role);

}
