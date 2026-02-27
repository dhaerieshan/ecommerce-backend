package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request to add a product to the cart")
public class AddToCartRequest {

    @Schema(description = "ID of the product to add", example = "5")
    private Long productId;

    @Schema(description = "Quantity to add", example = "2")
    private int quantity;
    // ✅ Removed unused categoryId field
}
