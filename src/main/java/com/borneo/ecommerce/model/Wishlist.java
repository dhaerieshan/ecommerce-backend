package com.borneo.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "wishlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Represents a product saved to a user's wishlist")
public class Wishlist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
          description = "Unique identifier of the wishlist entry",
          example = "301",
          accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @Schema(description = "The user who added the product to their wishlist")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  @Schema(description = "The product saved in the wishlist")
  private Product product;

  @Column(nullable = false)
  @Schema(
          description = "Date and time when the product was added to the wishlist",
          example = "2024-06-15T10:30:00")
  private LocalDateTime dateAdded;
}
