package com.borneo.ecommerce.service;

import com.borneo.ecommerce.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {
  CategoryDTO createCategory(CategoryDTO categoryDTO);

  List<CategoryDTO> getAllCategories();

  List<CategoryDTO> getSubcategories(Long id);

  CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

  void deleteCategory(Long id);

  CategoryDTO getCategoryById(Long id);
}
