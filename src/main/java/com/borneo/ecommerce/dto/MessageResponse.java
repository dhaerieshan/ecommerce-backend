package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@Schema(description = "Generic message response")
public class MessageResponse {

  @Schema(description = "Response message", example = "message here")
  private String message;

  public MessageResponse(String message) {
    this.message = message;
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
