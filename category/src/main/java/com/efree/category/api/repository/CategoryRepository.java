package com.efree.category.api.repository;

import com.efree.category.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, String>, JpaSpecificationExecutor<Category> {

    boolean existsByNameEnIsIgnoreCase(String nameEn);

    boolean existsByNameKhIsIgnoreCase(String nameKh);

}
