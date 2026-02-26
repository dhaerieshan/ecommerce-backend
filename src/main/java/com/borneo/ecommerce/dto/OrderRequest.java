package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Request to place an order")
public class OrderRequest {

    @Schema(description = "List of order items to checkout")
    private List<OrderItem> items;
}
