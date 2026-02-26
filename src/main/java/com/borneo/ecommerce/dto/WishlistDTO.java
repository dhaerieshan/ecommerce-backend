package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Wishlist item details")
public class WishlistDTO {

    @Schema(description = "Wishlist entry ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID of the user who owns the wishlist", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "ID of the product saved to wishlist", example = "5")
    private Long productId;

    @Schema(description = "Date and time the product was added", example = "2026-02-27T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dateAdded;
}
