package com.efree.category.api.dto.mapper;

import com.efree.category.api.dto.request.CategoryRequestDto;
import com.efree.category.api.dto.response.CategoryResponseDto;
import com.efree.category.api.entity.Category;
import com.efree.category.api.util.ValueInjectUtil;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    protected ValueInjectUtil valueInjectUtil;

    @Autowired
    public void setValueInjectUtil(ValueInjectUtil valueInjectUtil) {
        this.valueInjectUtil = valueInjectUtil;
    }

    @Mapping(target = "imageUrl", expression = "java(valueInjectUtil.getImageUri(category.getImageUrl()))")
    @Mapping(target = "downloadUrl", expression = "java(valueInjectUtil.getDownloadUri(category.getImageUrl()))")
    public abstract CategoryResponseDto mapFromCategoryToCategoryResponseDto(Category category);

    public abstract List<CategoryResponseDto> mapFromCategoryToCategoryResponseDto(List<Category> categories);

    public abstract Category mapFromCategoryRequestDtoToCategory(CategoryRequestDto categoryRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void mapFromCategoryRequestDtoToCategory(@MappingTarget Category category, CategoryRequestDto categoryRequestDto);

}
