package com.borneo.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Orders")
public class Order {

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  public List<OrderItem> orderItems;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Temporal(TemporalType.TIMESTAMP)
  private Date orderDate;

  private BigDecimal totalAmount;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(unique = true)
  private String displayOrderNumber;

  @Column(name = "status")
  private String status; // e.g., "In progress", "CANCELLED"
}
