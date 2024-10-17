package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Get all top-level categories
    @GetMapping
    public List<Category> getAllTopLevelCategories() {
        return categoryService.findAllTopLevelCategories();
    }

    // Get subcategories by parent category ID
    @GetMapping("/{parentId}/subcategories")
    public List<Category> getSubcategories(@PathVariable Long parentId) {
        return categoryService.findSubcategoriesByParentId(parentId);
    }

}