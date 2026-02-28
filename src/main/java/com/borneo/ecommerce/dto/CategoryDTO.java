package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Product category")
public class CategoryDTO {

  @Schema(description = "Category ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Schema(description = "Category name", example = "Electronics")
  private String name;

  @Schema(
      description = "Category image path",
      example = "/images/electronics.jpg",
      accessMode = Schema.AccessMode.READ_ONLY)
  private String imagePath;

  @Schema(
      description = "Category banner path",
      example = "/images/electronics-banner.jpg",
      accessMode = Schema.AccessMode.READ_ONLY)
  private String bannerPath;

  @Schema(description = "Parent category ID (null if top-level)", example = "null")
  private Long parentId;

  @Schema(description = "List of subcategories under this category")
  private List<CategoryDTO> children;

  public CategoryDTO(Category category) {
    this.id = category.getId();
    this.name = category.getName();
    this.imagePath = category.getImagePath();
    this.bannerPath = category.getBannerPath();
    this.parentId = category.getParent() != null ? category.getParent().getId() : null;
    this.children =
        category.getSubcategories() != null
            ? category.getSubcategories().stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList())
            : null;
  }
}
