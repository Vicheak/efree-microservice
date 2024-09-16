package com.efree.category.api.repository;

import com.efree.category.api.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, Long> {

    boolean existsByNameIsIgnoreCase(String name);

    Optional<Category> findFirstByOrderByIdDesc();

}
