package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentIsNull();  // Top-level categories (no parent)

    List<Category> findByParentId(Long parentId);  // Subcategories of a specific parent

    boolean existsByName(String name);
}