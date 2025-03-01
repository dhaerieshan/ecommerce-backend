package com.borneo.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private int price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // ✅ Add a constructor to convert from CartItem
    public OrderItem(CartItem cartItem) {
        this.product = cartItem.getProduct();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getProduct().getPrice() * cartItem.getQuantity();
    }
}