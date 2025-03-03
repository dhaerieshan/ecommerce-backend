package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor  // ✅ Required for Jackson serialization
@AllArgsConstructor
public class OrderItemDTO {
    private Long orderId;
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;

    public OrderItemDTO(OrderItem orderItem) {  // ✅ Ensure this constructor exists
        this.orderId = orderItem.getOrder().getId();
        this.productId = orderItem.getProduct().getId();
        this.productName = orderItem.getProduct().getName();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }
}