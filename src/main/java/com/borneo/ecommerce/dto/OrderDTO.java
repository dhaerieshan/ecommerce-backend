package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Order;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private Date orderDate;
    private Long userId;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;  // ⚠️ Fix capitalization (was "TotalAmount")

    // ✅ Add a constructor that accepts an Order object
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.userId = order.getUser().getId();  // Assuming Order has a getUser() method
        this.items = order.getOrderItems().stream().map(OrderItemDTO::new).collect(Collectors.toList());
        this.totalAmount = order.getTotalAmount();
    }

    // ✅ No-argument constructor (needed for frameworks like Jackson)
    public OrderDTO() {
    }
}