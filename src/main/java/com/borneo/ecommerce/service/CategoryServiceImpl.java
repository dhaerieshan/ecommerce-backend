// src/main/java/com/borneo/ecommerce/service/impl/CategoryServiceImpl.java

package com.borneo.ecommerce.service.impl;

import com.borneo.ecommerce.dto.CategoryDTO;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.repository.CategoryRepository;
import com.borneo.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with the same name already exists.");
        }

        Category category = new Category();
        category.setName(categoryDTO.getName());

        if (categoryDTO.getParentId() != null) {
            Category parent = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + categoryDTO.getParentId()));
            category.setParentCategory(parent);
        }

        Category savedCategory = categoryRepository.save(category);
        return new CategoryDTO(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        List<Category> topLevelCategories = categoryRepository.findByParentCategoryIsNull();
        return topLevelCategories.stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getSubcategories(Long parentId) {
        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + parentId));
        List<Category> subcategories = categoryRepository.findByParentCategoryId(parentId);
        return subcategories.stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getName().equals(categoryDTO.getName()) && categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with the same name already exists.");
        }

        category.setName(categoryDTO.getName());

        if (categoryDTO.getParentId() != null) {
            if (categoryDTO.getParentId().equals(id)) {
                throw new IllegalArgumentException("Category cannot be its own parent.");
            }
            Category parent = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + categoryDTO.getParentId()));
            // Prevent circular relationship
            if (isCircularRelationship(id, parent, categoryDTO.getParentId())) {
                throw new IllegalArgumentException("Circular category relationship detected.");
            }
            category.setParentCategory(parent);
        } else {
            category.setParentCategory(null);
        }

        Category updatedCategory = categoryRepository.save(category);
        return new CategoryDTO(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getSubcategories().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with existing subcategories.");
        }

        if (!category.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated products.");
        }

        categoryRepository.delete(category);
    }

    // Utility method to check for circular relationships
    private boolean isCircularRelationship(Long currentId, Category parent, Long newParentId) {
        if (newParentId == null) return false;
        if (currentId != null && newParentId.equals(currentId)) return true;

        Category tempParent = parent;
        while (tempParent != null) {
            if (tempParent.getId().equals(currentId)) {
                return true;
            }
            tempParent = tempParent.getParentCategory();
        }
        return false;
    }
}