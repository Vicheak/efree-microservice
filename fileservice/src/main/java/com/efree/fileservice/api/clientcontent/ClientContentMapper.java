package com.efree.fileservice.api.clientcontent;

import com.efree.fileservice.api.clientcontent.web.ClientContentRequestDto;
import com.efree.fileservice.api.clientcontent.web.ClientContentResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientContentMapper {

    ClientContentResponseDto mapFromClientContentToClientContentResponseDto(ClientContent clientContent);

    List<ClientContentResponseDto> mapFromClientContentToClientContentResponseDto(List<ClientContent> clientContents);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapFromClientContentRequestDtoToClientContent(@MappingTarget ClientContent clientContent, ClientContentRequestDto clientContentRequestDto);

}
