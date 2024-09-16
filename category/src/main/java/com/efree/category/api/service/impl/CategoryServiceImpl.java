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
    public CategoryResponseDto loadCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Category with id, %d has not been found in the system!"
                                        .formatted(id))
                );

        return categoryMapper.mapFromCategoryToCategoryResponseDto(category);
    }

    @Transactional
    @Override
    public CategoryResponseDto createNewCategory(CategoryRequestDto categoryRequestDto) {
        //check if category's name already exists
        if (categoryRepository.existsByNameIsIgnoreCase(categoryRequestDto.name()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Category's name conflicts resource in the system!");

        //map from dto to entity category
        Category category = categoryMapper.mapFromCategoryRequestDtoToCategory(categoryRequestDto);

        //check if there are categories in the database
        if (categoryRepository.count() != 0) {
            Category topCategory = categoryRepository.findFirstByOrderByIdDesc()
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Category has not been found in the system!")
                    );

            //increase the primary key
            category.setId(topCategory.getId() + 1);
        } else category.setId(1L);

        return categoryMapper.mapFromCategoryToCategoryResponseDto(categoryRepository.insert(category));
    }

    @Transactional
    @Override
    public CategoryResponseDto updateCategoryById(Long id, CategoryRequestDto categoryRequestDto) {
        //check if the category does not exist
        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Category with id, %d has not been found in the system!"
                                        .formatted(id))
                );

        //check if category's name already exists (except the previous name)
        if (Objects.nonNull(categoryRequestDto.name()))
            if (!categoryRequestDto.name().equalsIgnoreCase(category.getName()) &&
                    categoryRepository.existsByNameIsIgnoreCase(categoryRequestDto.name()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Category's name conflicts resource in the system!");

        categoryMapper.mapFromCategoryRequestDtoToCategory(category, categoryRequestDto);

        return categoryMapper.mapFromCategoryToCategoryResponseDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public void deleteCategoryById(Long id) {
        //check if the category does not exist
        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Category with id, %d has not been found in the system!"
                                        .formatted(id))
                );

        categoryRepository.delete(category);
    }

}
