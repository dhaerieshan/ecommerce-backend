package com.borneo.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_items")
@Schema(description = "Represents a single product entry inside a user's shopping cart")
public class CartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier of the cart item",
      example = "201",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @ManyToOne(optional = false)
  @Schema(description = "The product added to the cart")
  private Product product;

  @Schema(description = "Quantity of the product in the cart", example = "3")
  private int quantity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id")
  @Schema(description = "The cart this item belongs to")
  private Cart cart;
}
