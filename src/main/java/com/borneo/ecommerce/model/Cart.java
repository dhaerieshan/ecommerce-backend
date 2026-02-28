package com.borneo.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "carts")
@Schema(
        description = "Represents a shopping cart belonging to a user. Each user has exactly one cart.")
public class Cart {

  @Id
  @Schema(
          description = "The user ID, which also serves as the cart's primary key",
          example = "1",
          accessMode = Schema.AccessMode.READ_ONLY)
  private Long userId;

  @OneToOne
  @MapsId
  @JoinColumn(name = "user_id")
  @Schema(description = "The user who owns this cart")
  private User user;

  @OneToMany(
          mappedBy = "cart",
          cascade = CascadeType.ALL,
          orphanRemoval = true,
          fetch = FetchType.EAGER)
  @JsonManagedReference
  @Schema(description = "List of items currently in the cart")
  private List<CartItem> items = new ArrayList<>();
}
