package com.efree.product.api.external.categoryservice;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.external.categoryservice.dto.CategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceRestClientConsumer {

    private final CategoryServiceRestClient categoryServiceRestClient;

    public CategoryResponseDto loadCategoryById(String categoryId) {
        BaseApi<CategoryResponseDto> categoryResponseDtoBaseApi =
                categoryServiceRestClient.loadCategoryById(categoryId);
        return categoryResponseDtoBaseApi.payload();
    }

}
