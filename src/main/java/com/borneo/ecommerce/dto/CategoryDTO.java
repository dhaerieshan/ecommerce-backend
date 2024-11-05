package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long id;
    private String name;
    private String imagePath;
    private String bannerPath;
    private Long parentId;
    private List<CategoryDTO> children;


    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.imagePath = category.getImagePath();
        this.bannerPath = category.getBannerPath();
        this.parentId = category.getParent() != null ? category.getParent().getId() : null;
        this.children = category.getSubcategories() != null
                ? category.getSubcategories().stream().map(CategoryDTO::new).collect(Collectors.toList())
                : null;
    }


}