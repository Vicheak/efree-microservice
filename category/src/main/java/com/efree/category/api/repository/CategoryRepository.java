package com.efree.category.api.repository;

import com.efree.category.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {

    boolean existsByNameEnIsIgnoreCase(String nameEn);

    boolean existsByNameKhIsIgnoreCase(String nameKh);

    Optional<Category> findFirstByOrderByIdDesc();

}
