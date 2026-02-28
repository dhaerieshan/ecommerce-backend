package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Individual item within an order")
public class OrderItemDTO {

  @Schema(description = "ID of the parent order", example = "101")
  private Long orderId;

  @Schema(description = "Product ID", example = "5")
  private Long productId;

  @Schema(description = "Product name at time of order", example = "Samsung Galaxy S24")
  private String productName;

  @Schema(description = "Product image path", example = "/images/samsung-s24.jpg")
  private String productImage;

  @Schema(description = "Quantity ordered", example = "2")
  private int quantity;

  @Schema(description = "Unit price at time of order", example = "89999.00")
  private BigDecimal price;

  // ✅ BUG FIX — was completely empty before, returning null/0 for all fields
  public OrderItemDTO(OrderItem orderItem) {
    this.orderId = orderItem.getOrder() != null ? orderItem.getOrder().getId() : null;
    this.productId = orderItem.getProductId();
    this.productName = orderItem.getProduct().getName();
    this.productImage = orderItem.getProduct().getImagePath();
    this.quantity = orderItem.getQuantity();
    this.price = orderItem.getPrice();
  }
}
