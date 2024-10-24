package com.borneo.ecommerce.service;

import com.borneo.ecommerce.dto.CategoryDTO;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.repository.CategoryRepository;
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
        category.setImagePath(categoryDTO.getImagePath());

        // Handle parent category
        if (categoryDTO.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + categoryDTO.getParentId()));
            category.setParent(parentCategory);
        }

        Category savedCategory = categoryRepository.save(category);
        return new CategoryDTO(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        List<Category> topLevelCategories = categoryRepository.findByParentIsNull();
        return topLevelCategories.stream()
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
        category.setImagePath(categoryDTO.getImagePath() != null ? categoryDTO.getImagePath() : category.getImagePath());

        if (categoryDTO.getParentId() != null) {
            if (categoryDTO.getParentId().equals(id)) {
                throw new IllegalArgumentException("Category cannot be its own parent.");
            }
            Category parent = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + categoryDTO.getParentId()));
            category.setParent(parent);
        } else {
            category.setParent(null);  // Make it a top-level category
        }

        Category updatedCategory = categoryRepository.save(category);
        return new CategoryDTO(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return new CategoryDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getSubcategories(Long id) {
        Category parent = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + id));

        // Fetch the list of subcategories from the parent category
        List<Category> subcategories = parent.getSubcategories();

        // Convert the list of Category entities to CategoryDTOs
        return subcategories.stream()
                .map(CategoryDTO::new)  // Convert each Category to CategoryDTO
                .collect(Collectors.toList());
    }

}
