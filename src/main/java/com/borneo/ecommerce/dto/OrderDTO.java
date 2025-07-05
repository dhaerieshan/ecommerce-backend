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
    private Long id;  // DB ID if you still want it
    private String displayOrderNumber; // ✅ NEW
    private Date orderDate;
    private Long userId;
    private BigDecimal totalAmount;
    private String status;
    private List<OrderItemDTO> items;

    // ✅ Constructor
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.displayOrderNumber = order.getDisplayOrderNumber();
        this.orderDate = order.getOrderDate();
        this.userId = order.getUser().getId();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.items = order.getOrderItems().stream().map(OrderItemDTO::new).collect(Collectors.toList());
    }


    // ✅ No-argument constructor (needed for frameworks like Jackson)
    public OrderDTO() {
    }
}