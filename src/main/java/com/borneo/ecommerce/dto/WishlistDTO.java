package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Wishlist item")
public class WishlistDTO {

  @Schema(description = "Wishlist entry ID", example = "7")
  private Long id;

  @Schema(description = "User ID who owns this wishlist item", example = "3")
  private Long userId;

  @Schema(description = "Product ID saved to wishlist", example = "5")
  private Long productId;

  @Schema(description = "Product name", example = "Samsung Galaxy S24")
  private String productName;

  @Schema(description = "Product image path", example = "/images/samsung-s24.jpg")
  private String productImage;

  @Schema(description = "Product price", example = "89999")
  private int productPrice;

  @Schema(description = "Date and time the product was added to wishlist")
  private LocalDateTime dateAdded;
}
