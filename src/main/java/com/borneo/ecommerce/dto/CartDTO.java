package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CartDTO {
    private Long userId;
    private String username;
    private List<CartItemDTO> items;

    public CartDTO(Cart cart) {
        this.userId = cart.getUser().getId();
        this.username = cart.getUser().getUsername();
        this.items = cart.getItems().stream()
                .map(CartItemDTO::new)
                .collect(Collectors.toList());
    }
}