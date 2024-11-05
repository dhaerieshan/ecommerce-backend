package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.CartItem;

public class CartItemDTO {
    private Long id;
    private ProductDTO product;
    private int quantity;

    public CartItemDTO(CartItem cartItem) {
        this.id = cartItem.getId();
        this.product = new ProductDTO(cartItem.getProduct());
        this.quantity = cartItem.getQuantity();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}