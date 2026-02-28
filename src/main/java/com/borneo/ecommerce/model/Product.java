package com.borneo.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
@Schema(description = "Represents a product available for purchase in the store")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier of the product",
      example = "42",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Schema(description = "Name of the product", example = "Wireless Bluetooth Headphones")
  private String name;

  @Column(columnDefinition = "TEXT")
  @Schema(
      description = "Detailed description of the product",
      example =
          "High-quality over-ear headphones with active noise cancellation and 30-hour battery life.")
  private String description;

  @Schema(
      description = "Price of the product in the smallest currency unit (e.g., cents/IDR)",
      example = "299000")
  private int price;

  @Schema(description = "Available stock quantity", example = "150")
  private int stock;

  @Schema(
      description = "Relative or absolute path to the product image",
      example = "/images/products/headphones.png")
  private String imagePath;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  @JsonBackReference
  @Schema(description = "Category to which this product belongs")
  private Category category;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnoreProperties("product")
  @Schema(description = "Set of wishlist entries referencing this product")
  private Set<Wishlist> wishlists = new HashSet<>();
}
