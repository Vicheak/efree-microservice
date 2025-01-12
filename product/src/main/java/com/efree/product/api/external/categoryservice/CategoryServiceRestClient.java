package com.efree.product.api.external.categoryservice;

import com.efree.product.api.base.BaseApi;
import com.efree.product.api.external.categoryservice.dto.CategoryResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "category-service"/*, url = "${rest.category-service.base-url}"*/)
public interface CategoryServiceRestClient {

    @GetMapping("/api/v1/categories/{id}")
    BaseApi<CategoryResponseDto> loadCategoryById(@PathVariable String id);

}
