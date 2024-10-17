package com.borneo.ecommerce.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long productId;
    private int quantity;
    private Long categoryId; // Include if needed

}
