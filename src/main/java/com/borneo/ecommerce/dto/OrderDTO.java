package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Order details")
public class OrderDTO {

    @Schema(
            description = "Order database ID",
            example = "101",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(
            description = "Human readable order number",
            example = "ORD-2026-00101",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String displayOrderNumber;

    @Schema(
            description = "Date the order was placed",
            example = "2026-02-27",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Date orderDate;

    @Schema(
            description = "ID of the user who placed the order",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(
            description = "Total order amount",
            example = "1999.00",
            accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalAmount;

    @Schema(
            description = "Current order status",
            example = "PENDING",
            allowableValues = {"PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"})
    private String status;

    @Schema(description = "List of items in this order")
    private List<OrderItemDTO> items;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.displayOrderNumber = order.getDisplayOrderNumber();
        this.orderDate = order.getOrderDate();
        this.userId = order.getUser().getId();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.items = order.getOrderItems().stream().map(OrderItemDTO::new).collect(Collectors.toList());
    }
}
