package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Individual order item")
public class OrderItemDTO {

    @Schema(
            description = "Order ID this item belongs to",
            example = "101",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long orderId;

    @Schema(description = "Product ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long productId;

    @Schema(
            description = "Product name",
            example = "Samsung Galaxy S26",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String productName;

    @Schema(
            description = "Product image path",
            example = "/images/samsung-s26.jpg",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String productImage;

    @Schema(description = "Quantity ordered", example = "1")
    private int quantity;

    @Schema(
            description = "Price per unit at time of order",
            example = "999.00",
            accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal price;

    public OrderItemDTO(OrderItem orderItem) {
        this.orderId = orderItem.getOrder().getId();
        this.productId = orderItem.getProduct().getId();
        this.productName = orderItem.getProduct().getName();
        this.productImage = orderItem.getProduct().getImagePath();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }
}
