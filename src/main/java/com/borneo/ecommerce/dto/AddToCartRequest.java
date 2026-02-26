package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request to add a product to cart")
public class AddToCartRequest {

    @Schema(description = "ID of the product to add", example = "1")
    private Long productId;

    @Schema(description = "Quantity to add", example = "2")
    private int quantity;

    @Schema(description = "Category ID of the product", example = "3")
    private Long categoryId;
}
