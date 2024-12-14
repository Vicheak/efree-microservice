package com.efree.category.api.controller;

import com.efree.category.api.base.BaseApi;
import com.efree.category.api.dto.request.CategoryRequestDto;
import com.efree.category.api.dto.response.CategoryResponseDto;
import com.efree.category.api.external.fileservice.dto.FileDto;
import com.efree.category.api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public BaseApi<?> loadAllCategories() {

        List<CategoryResponseDto> categoryResponseDtoList = categoryService.loadAllCategories();

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("All categories loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(categoryResponseDtoList)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public BaseApi<?> loadCategoryById(@PathVariable String id) {

        CategoryResponseDto categoryResponseDto = categoryService.loadCategoryById(id);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("Category with id, %s loaded successfully!".formatted(id))
                .timestamp(LocalDateTime.now())
                .payload(categoryResponseDto)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BaseApi<?> createNewCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {

        CategoryResponseDto newCategoryResponseDto = categoryService.createNewCategory(categoryRequestDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("A category has been created successfully!")
                .timestamp(LocalDateTime.now())
                .payload(newCategoryResponseDto)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public BaseApi<?> updateCategoryById(@PathVariable String id,
                                         @RequestBody CategoryRequestDto categoryRequestDto) {

        CategoryResponseDto updatedCategoryResponseDto = categoryService.updateCategoryById(id, categoryRequestDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("A category has been updated successfully!")
                .timestamp(LocalDateTime.now())
                .payload(updatedCategoryResponseDto)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public BaseApi<?> deleteCategoryById(@PathVariable String id) {

        categoryService.deleteCategoryById(id);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("A category has been deleted successfully!")
                .timestamp(LocalDateTime.now())
                .payload(Map.of("message", "Payload has no content!"))
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/upload/single/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseApi<?> uploadCategoryImage(@PathVariable String uuid,
                                          @RequestPart MultipartFile file) {

        FileDto fileDto = categoryService.uploadCategoryImage(uuid, file);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("A category image has been uploaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(fileDto)
                .build();
    }

}
