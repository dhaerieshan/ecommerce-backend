package com.borneo.ecommerce.service;

import com.borneo.ecommerce.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAllTopLevelCategories();

    List<Category> findSubcategoriesByParentId(Long parentId);

    Category findById(Long id);
}