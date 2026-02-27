package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Cart;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Schema(description = "Shopping cart for a user")
public class CartDTO {

    @Schema(description = "User ID who owns this cart", example = "3")
    private Long userId;

    @Schema(description = "Username of the cart owner", example = "priya_user")
    private String username;

    @Schema(description = "List of items in the cart")
    private List<CartItemDTO> items;

    public CartDTO(Cart cart) {
        this.userId = cart.getUser().getId();
        this.username = cart.getUser().getUsername();
        this.items = cart.getItems().stream().map(CartItemDTO::new).collect(Collectors.toList());
    }
}
