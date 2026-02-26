package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request to remove a product from cart")
public class RemoveFromCartRequest {

    @Schema(description = "ID of the product to remove", example = "1")
    private Long productId;
}
