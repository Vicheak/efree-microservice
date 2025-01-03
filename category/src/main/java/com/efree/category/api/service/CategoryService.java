package com.efree.category.api.service;

import com.efree.category.api.dto.request.CategoryRequestDto;
import com.efree.category.api.dto.response.CategoryResponseDto;
import com.efree.category.api.external.fileservice.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    /**
     * This method is used to load all category resources from the system
     * @return List<CategoryResponseDto>
     */
    List<CategoryResponseDto> loadAllCategories();

    /**
     * This method is used to load specific category resource by id
     * @param id is the path parameter from client
     * @return CategoryResponseDto
     */
    CategoryResponseDto loadCategoryById(String id);

    /**
     * This method is used to create new category resource into the system
     * @param categoryRequestDto is the request from client
     * @return CategoryResponseDto
     */
    CategoryResponseDto createNewCategory(CategoryRequestDto categoryRequestDto);

    /**
     * This method is used to update specific category resource by name
     * @param id is the path parameter from client
     * @param categoryRequestDto is the request from client
     * @return CategoryResponseDto
     */
    CategoryResponseDto updateCategoryById(String id, CategoryRequestDto categoryRequestDto);

    /**
     * This method is used to delete specific category resource by name
     * @param id is the path parameter from client
     */
    void deleteCategoryById(String id);

    /**
     * This method is used to upload single image resource to server
     * @param uuid is the path parameter from client
     * @param fileRequest fileRequest is the request from client
     * @return FileDto
     */
    FileDto uploadCategoryImage(String uuid, MultipartFile fileRequest);

}
