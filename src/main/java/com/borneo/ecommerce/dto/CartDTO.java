package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Cart;

import java.util.List;
import java.util.stream.Collectors;

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

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }
}