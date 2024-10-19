package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Stock is required")
    private Integer stock; // Ensure this matches your entity's field name

    private String imagePath;

    @NotNull(message = "Category is required")
    private Long categoryId; // To receive category selection

    // Constructors
    public ProductDTO() {
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock(); // Ensure consistency
        this.imagePath = product.getImagePath();
        if (product.getCategory() != null) {
            this.categoryId = product.getCategory().getId();
        }
    }
}