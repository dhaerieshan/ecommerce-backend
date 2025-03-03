package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {
    private Long orderId;
    private BigDecimal totalAmount;
    private Date orderDate;
    private List<OrderItem> items;

}