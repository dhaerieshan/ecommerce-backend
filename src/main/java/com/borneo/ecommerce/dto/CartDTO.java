package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.Cart;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Schema(description = "Shopping cart details")
public class CartDTO {

    @Schema(description = "ID of the user who owns the cart", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Username of the cart owner", example = "Dhaerieshan", accessMode = Schema.AccessMode.READ_ONLY)
    private String username;

    @Schema(description = "List of items in the cart")
    private List<CartItemDTO> items;

    public CartDTO(Cart cart) {
        this.userId = cart.getUser().getId();
        this.username = cart.getUser().getUsername();
        this.items = cart.getItems().stream()
                .map(CartItemDTO::new)
                .collect(Collectors.toList());
    }

    @Data
    @Schema(description = "Paginated response wrapper")
    public static class PageResponse<T> {

        @Schema(description = "List of results for current page")
        private List<T> content;

        @Schema(description = "Current page number (0-indexed)", example = "0")
        private int page;

        @Schema(description = "Number of items per page", example = "5")
        private int size;

        @Schema(description = "Total number of items across all pages", example = "50")
        private long totalElements;

        @Schema(description = "Total number of pages", example = "10")
        private int totalPages;

        @Schema(description = "Is this the last page?", example = "false")
        private boolean last;

        @Schema(description = "Is this the first page?", example = "true")
        private boolean first;

        public PageResponse(Page<T> page) {
            this.content = page.getContent();
            this.page = page.getNumber();
            this.size = page.getSize();
            this.totalElements = page.getTotalElements();
            this.totalPages = page.getTotalPages();
            this.last = page.isLast();
            this.first = page.isFirst();
        }
    }
}
