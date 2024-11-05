

package com.borneo.ecommerce.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WishlistDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime dateAdded;


}