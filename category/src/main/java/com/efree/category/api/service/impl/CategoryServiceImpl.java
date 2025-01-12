package com.efree.category.api.service.impl;

import com.efree.category.api.dto.mapper.CategoryMapper;
import com.efree.category.api.dto.request.CategoryRequestDto;
import com.efree.category.api.dto.response.CategoryResponseDto;
import com.efree.category.api.entity.Category;
import com.efree.category.api.external.fileservice.FileServiceRestClientConsumer;
import com.efree.category.api.external.fileservice.dto.FileDto;
import com.efree.category.api.repository.CategoryRepository;
import com.efree.category.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final FileServiceRestClientConsumer fileServiceRestClientConsumer;

    @Override
    public List<CategoryResponseDto> loadAllCategories() {
        return categoryMapper.mapFromCategoryToCategoryResponseDto(categoryRepository.findAll());
    }

    @Override
    public Page<CategoryResponseDto> getAllCategoriesByPagination(int page, int size, String sortBy, String direction) {
        PageRequest pageRequest = buildPageRequest(page, size, sortBy, direction);
        Page<Category> categoriesPage = categoryRepository.findAll(pageRequest);
        return categoriesPage.map(categoryMapper::mapFromCategoryToCategoryResponseDto);
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

    @Override
    public List<CategoryResponseDto> searchProducts(String keyword, String field, String sortBy, String direction) {
        Specification<Category> spec = (root, query, criteriaBuilder) -> {
            if (field != null && keyword != null) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(field)),
                        "%" + keyword.toLowerCase() + "%");
            }
            return null;
        };
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        List<Category> categories = categoryRepository.findAll(spec, Sort.by(sortDirection, sortBy));
        return categoryMapper.mapFromCategoryToCategoryResponseDto(categories);
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

        //check category resource
        if (!category.getProducts().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Category resource cannot be removed, it is used for another resource");
        }

        categoryRepository.delete(category);
    }

    @Transactional
    @Override
    public FileDto uploadCategoryImage(String uuid, MultipartFile fileRequest) {
        //check if the category does not exist
        Category category = categoryRepository.findById(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Category with id, %s has not been found in the system!"
                                        .formatted(uuid))
                );

        //invoke file service to upload to server
        FileDto fileDto = fileServiceRestClientConsumer.singleUpload(fileRequest);
        category.setImageUrl(fileDto.name());
        categoryRepository.save(category);

        return fileDto;
    }

    private PageRequest buildPageRequest(int page, int size, String sortBy, String direction) {
        if (page <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Page number must be greater than 0");
        }
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy));
    }

}
