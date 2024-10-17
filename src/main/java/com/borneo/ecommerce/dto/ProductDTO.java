package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String imagePath;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Integer stock;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imagePath = product.getImagePath();
        this.description = product.getDescription();
        this.stock = product.getStock();
        if (product.getCategory() != null) {
            this.categoryId = product.getCategory().getId();
            this.categoryName = buildCategoryPath(product.getCategory());
        }
    }

    private String buildCategoryPath(Category category) {
        if (category == null) return null;
        StringBuilder path = new StringBuilder(category.getName());
        Category parent = category.getParent();
        while (parent != null) {
            path.insert(0, parent.getName() + " > ");
            parent = parent.getParent();
        }
        return path.toString();
    }

}