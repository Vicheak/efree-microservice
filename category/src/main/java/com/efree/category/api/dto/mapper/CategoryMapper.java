package com.efree.category.api.dto.mapper;

import com.efree.category.api.dto.request.CategoryRequestDto;
import com.efree.category.api.dto.response.CategoryResponseDto;
import com.efree.category.api.entity.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDto mapFromCategoryToCategoryResponseDto(Category category);

    List<CategoryResponseDto> mapFromCategoryToCategoryResponseDto(List<Category> categories);

    Category mapFromCategoryRequestDtoToCategory(CategoryRequestDto categoryRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapFromCategoryRequestDtoToCategory(@MappingTarget Category category, CategoryRequestDto categoryRequestDto);

}
