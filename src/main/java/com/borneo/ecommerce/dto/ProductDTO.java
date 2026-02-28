package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Product details")
public class ProductDTO {

  @Schema(description = "Product ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @NotBlank(message = "Product name is required")
  @Schema(description = "Product name", example = "Samsung Galaxy S24")
  private String name;

  @Schema(
      description = "Product description",
      example = "6.2-inch display, 50MP camera, Snapdragon 8 Gen 3")
  private String description;

  @NotNull(message = "Price is required")
  @Positive(message = "Price must be positive")
  @Schema(description = "Price in INR (paise/integer)", example = "89999")
  private int price;

  @NotNull(message = "Stock is required")
  @Schema(description = "Available stock quantity", example = "100")
  private Integer stock;

  @Schema(
      description = "Image path",
      example = "/images/samsung-s24.jpg",
      accessMode = Schema.AccessMode.READ_ONLY)
  private String imagePath;

  @NotNull(message = "Category is required")
  @Schema(description = "Category ID this product belongs to", example = "2")
  private Long categoryId;

  public ProductDTO() {}

  public ProductDTO(Product product) {
    this.id = product.getId();
    this.name = product.getName();
    this.description = product.getDescription();
    this.price = product.getPrice();
    this.stock = product.getStock();
    this.imagePath = product.getImagePath();
    if (product.getCategory() != null) {
      this.categoryId = product.getCategory().getId();
    }
  }
}
