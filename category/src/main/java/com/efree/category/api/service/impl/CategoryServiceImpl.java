package com.efree.category.api.service.impl;

import com.efree.category.api.dto.mapper.CategoryMapper;
import com.efree.category.api.dto.request.CategoryRequestDto;
import com.efree.category.api.dto.response.CategoryResponseDto;
import com.efree.category.api.entity.Category;
import com.efree.category.api.repository.CategoryRepository;
import com.efree.category.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> loadAllCategories() {
        return categoryMapper.mapFromCategoryToCategoryResponseDto(categoryRepository.findAll());
    }

    @Override
    public CategoryResponseDto loadCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Category with id, %s has not been found in the system!"
                                        .formatted(id))
                );

        return categoryMapper.mapFromCategoryToCategoryResponseDto(category);
    }

    @Transactional
    @Override
    public CategoryResponseDto createNewCategory(CategoryRequestDto categoryRequestDto) {
        //check if category's name already exists
        if (categoryRepository.existsByNameEnIsIgnoreCase(categoryRequestDto.nameEn()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Category's name conflicts resource in the system!");

        if (Objects.nonNull(categoryRequestDto.nameKh()) && !categoryRequestDto.nameKh().isEmpty()) {
            if (categoryRepository.existsByNameKhIsIgnoreCase(categoryRequestDto.nameKh()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Category's name conflicts resource in the system!");
        }

        //map from dto to entity category
        Category category = categoryMapper.mapFromCategoryRequestDtoToCategory(categoryRequestDto);
        category.setId(UUID.randomUUID().toString());
        categoryRepository.save(category);
        return categoryMapper.mapFromCategoryToCategoryResponseDto(category);
    }

    @Transactional
    @Override
    public CategoryResponseDto updateCategoryById(String id, CategoryRequestDto categoryRequestDto) {
        //check if the category does not exist
        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Category with id, %s has not been found in the system!"
                                        .formatted(id))
                );

        //check if category's name already exists (except the previous name)
        if (Objects.nonNull(categoryRequestDto.nameEn()) && !categoryRequestDto.nameKh().isEmpty())
            if (!categoryRequestDto.nameEn().equalsIgnoreCase(category.getNameEn()) &&
                    categoryRepository.existsByNameEnIsIgnoreCase(categoryRequestDto.nameEn()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Category's name conflicts resource in the system!");

        if (Objects.nonNull(categoryRequestDto.nameKh()) && !categoryRequestDto.nameKh().isEmpty())
            if (!categoryRequestDto.nameKh().equalsIgnoreCase(category.getNameKh()) &&
                    categoryRepository.existsByNameKhIsIgnoreCase(categoryRequestDto.nameKh()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Category's name conflicts resource in the system!");

        categoryMapper.mapFromCategoryRequestDtoToCategory(category, categoryRequestDto);

        return categoryMapper.mapFromCategoryToCategoryResponseDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public void deleteCategoryById(String id) {
        //check if the category does not exist
        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Category with id, %s has not been found in the system!"
                                        .formatted(id))
                );

        categoryRepository.delete(category);
    }

}
