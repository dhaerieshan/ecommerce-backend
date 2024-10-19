// src/main/java/com/borneo/ecommerce/service/CategoryService.java

package com.borneo.ecommerce.service;

import com.borneo.ecommerce.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> getAllCategories();

    List<CategoryDTO> getSubcategories(Long parentId);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);
}