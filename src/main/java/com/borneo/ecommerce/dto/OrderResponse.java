package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderResponse {
    private Long orderId;
    private BigDecimal totalAmount;
    private Date orderDate;
    private List<OrderItemDTO> items;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.totalAmount = order.getTotalAmount();
        this.orderDate = order.getOrderDate();
        this.items = order.getItems().stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());
    }
}