// src/main/java/com/borneo/ecommerce/dto/CategoryDTO.java

package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Category;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private Long parentId; // Nullable for top-level categories
    private List<CategoryDTO> subcategories;

    // Default constructor
    public CategoryDTO() {
    }

    // Constructor to map from Category entity
    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        if (category.getParentCategory() != null) {
            this.parentId = category.getParentCategory().getId();
        }
        if (category.getSubcategories() != null && !category.getSubcategories().isEmpty()) {
            this.subcategories = category.getSubcategories()
                    .stream()
                    .map(CategoryDTO::new)
                    .collect(Collectors.toList());
        }
    }
}