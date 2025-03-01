package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.CartItem;
import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private ProductDTO product;
    private int quantity;

    public CartItemDTO(CartItem cartItem) {
        this.id = cartItem.getId();
        this.product = new ProductDTO(cartItem.getProduct()); // Assuming ProductDTO has a constructor
        this.quantity = cartItem.getQuantity();
    }
}