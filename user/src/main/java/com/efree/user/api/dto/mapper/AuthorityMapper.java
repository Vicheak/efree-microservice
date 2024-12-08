package com.efree.user.api.dto.mapper;

import com.efree.user.api.dto.response.AuthorityResponseDto;
import com.efree.user.api.entity.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {

    @Mapping(target = "authorityId", source = "id")
    @Mapping(target = "authorityName", source = "name")
    AuthorityResponseDto fromAuthorityToAuthorityResponseDto(Authority authority);

    List<AuthorityResponseDto> fromAuthorityToAuthorityResponseDto(List<Authority> authorities);

}
