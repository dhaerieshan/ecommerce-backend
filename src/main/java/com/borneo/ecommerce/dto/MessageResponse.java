package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Standard API response")
public class MessageResponse {

    @Schema(description = "Response message", example = "Operation completed successfully")
  private String message;

  public MessageResponse(String message) {
    this.message = message;
  }

  @Data
  @NoArgsConstructor
  @Schema(description = "Paginated response wrapper")
  public static class PageResponse<T> {

      @Schema(description = "Items on this page")
    private List<T> content;

    @Schema(description = "Current page number (0-indexed)", example = "0")
    private int page;

      @Schema(description = "Items per page", example = "5")
    private int size;

      @Schema(description = "Total items across all pages", example = "50")
    private long totalElements;

    @Schema(description = "Total number of pages", example = "10")
    private int totalPages;

      @Schema(description = "Is this the first page?", example = "true")
      private boolean first;

    @Schema(description = "Is this the last page?", example = "false")
    private boolean last;

      public PageResponse(org.springframework.data.domain.Page<T> page) {
      this.content = page.getContent();
      this.page = page.getNumber();
      this.size = page.getSize();
      this.totalElements = page.getTotalElements();
      this.totalPages = page.getTotalPages();
      this.first = page.isFirst();
          this.last = page.isLast();
    }
  }
}
