package com.borneo.ecommerce.dto;

import com.borneo.ecommerce.model.OrderItem;
import java.util.List;
import lombok.Data;

@Data
public class OrderRequest {
  private List<OrderItem> items;
}
