package com.borneo.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Orders")
@Schema(description = "Represents a purchase order placed by a user")
public class Order {

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @Schema(description = "List of items included in this order")
  public List<OrderItem> orderItems;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Unique identifier of the order", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Schema(description = "Timestamp when the order was placed", example = "2024-06-15T10:30:00.000+00:00")
  private Date orderDate;

  @Schema(description = "Total monetary amount of the order", example = "450000.00")
  private BigDecimal totalAmount;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @Schema(description = "The user who placed this order")
  private User user;

  @Column(unique = true)
  @Schema(description = "Human-readable order number displayed to the customer", example = "ORD-20240615-00101")
  private String displayOrderNumber;

  @Column(name = "status")
  @Schema(description = "Current status of the order", example = "In progress", allowableValues = {"In progress", "SHIPPED", "DELIVERED", "CANCELLED"})
  private String status;
}
