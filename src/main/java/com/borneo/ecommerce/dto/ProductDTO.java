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
    private int price;

    @NotNull(message = "Stock is required")
    private Integer stock;

    private String imagePath;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private String barcodeNumber; //   new column

    public ProductDTO() {
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.imagePath = product.getImagePath();
        this.barcodeNumber = product.getBarcodeNumber();
        if (product.getCategory() != null) {
            this.categoryId = product.getCategory().getId();
        }
    }
}