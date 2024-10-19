// src/main/java/com/borneo/ecommerce/repository/CategoryRepository.java

package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Fetch categories where parentCategory is null (i.e., top-level parent categories)
    @EntityGraph(attributePaths = {"subcategories"})
    List<Category> findByParentCategoryIsNull();

    // Fetch subcategories by parent ID
    @EntityGraph(attributePaths = {"subcategories"})
    List<Category> findByParentCategoryId(Long parentId);

    boolean existsByName(String name);

    Optional<Category> findByName(String name);
}