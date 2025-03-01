package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<OrderItem> items;
}