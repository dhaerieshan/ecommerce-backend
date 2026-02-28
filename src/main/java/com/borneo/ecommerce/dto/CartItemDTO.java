package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "A single item inside a cart")
public class CartItemDTO {

  @Schema(description = "Cart item ID", example = "10")
  private Long id;

  @Schema(description = "Product details")
  private ProductDTO product;

  @Schema(description = "Quantity of the product in cart", example = "2")
  private int quantity;

  public CartItemDTO(CartItem cartItem) {
    this.id = cartItem.getId();
    this.product = new ProductDTO(cartItem.getProduct());
    this.quantity = cartItem.getQuantity();
  }
}
