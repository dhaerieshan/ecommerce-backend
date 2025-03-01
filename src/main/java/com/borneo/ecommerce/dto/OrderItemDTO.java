package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.OrderItem;
import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private ProductDTO product;
    private int quantity;
    private int price;

    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.product = new ProductDTO(orderItem.getProduct()); // Assuming you have ProductDTO
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }
}