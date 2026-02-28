package com.borneo.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
@Schema(description = "Represents a single product line item within an order")
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
      description = "Unique identifier of the order item",
      example = "501",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id", nullable = false)
  @Schema(description = "The product associated with this order item")
  private Product product;

  @Schema(description = "Quantity of the product ordered", example = "2")
  private int quantity;

  @Schema(description = "Price of the product at the time of ordering", example = "149500.00")
  private BigDecimal price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  @Schema(description = "The order this item belongs to")
  private Order order;

  public Long getProductId() {
    return product.getId();
  }
}
