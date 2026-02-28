package com.borneo.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
@Schema(
        description =
                "Represents a product category, which can have a parent category and subcategories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "Unique identifier of the category",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(
            description = "Unique name of the category",
            example = "Electronics",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(
            description = "Relative or absolute path to the category thumbnail image",
            example = "/images/categories/electronics.png")
    private String imagePath;

    @Schema(
            description = "Relative or absolute path to the category banner image",
            example = "/images/banners/electronics-banner.png")
    private String bannerPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @Schema(description = "Parent category (null if this is a top-level category)")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "List of subcategories belonging to this category")
    private List<Category> subcategories;
}
