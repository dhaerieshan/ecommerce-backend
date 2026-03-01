package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.MessageResponse;
import com.borneo.ecommerce.dto.OrderDTO;
import com.borneo.ecommerce.dto.OrderItemDTO;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Order;
import com.borneo.ecommerce.model.OrderItem;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.ProductRepository;
import com.borneo.ecommerce.service.OrderService;
import com.borneo.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "09. Orders", description = "Order creation, tracking, and management APIs")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final UserService userService;
  private final ProductRepository productRepository;

  @Operation(
      summary = "Checkout and place order",
      description = "Places a new order from the user's cart with the provided items.",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Order placed successfully",
            content = @Content(schema = @Schema(implementation = OrderDTO.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Cart is empty or invalid data",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Cart is empty\"}"))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Unauthorized\"}")))
      })
  @PostMapping("/checkout")
  public ResponseEntity<OrderDTO> checkoutOrder(
      @AuthenticationPrincipal org.springframework.security.core.userdetails.User authUser,
      @RequestBody List<OrderItem> item) {
    User user = userService.findByUsername(authUser.getUsername());
    if (user == null) throw new ResourceNotFoundException("User not found");

    Order order = orderService.createOrder(user, item);
    order.setStatus("PENDING");

    OrderDTO orderDTO = new OrderDTO(order);
    return ResponseEntity.ok(orderDTO);
  }

  @Operation(
      summary = "Get all orders",
      description = "Returns a paginated list of orders for the authenticated user.",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Orders retrieved successfully",
            content =
                @Content(schema = @Schema(implementation = MessageResponse.PageResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Unauthorized\"}")))
      })
  @GetMapping
  public ResponseEntity<MessageResponse.PageResponse<OrderDTO>> getOrders(
      @AuthenticationPrincipal UserDetails authUser,
      @Parameter(description = "Page number (0-indexed)", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Number of items per page", example = "5")
          @RequestParam(defaultValue = "5")
          int size) {
    User user = userService.findByUsername(authUser.getUsername());
    if (user == null) throw new ResourceNotFoundException("User not found");

    List<OrderDTO> orders =
        orderService.getOrdersByUser(user).stream().map(OrderDTO::new).collect(Collectors.toList());

    PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), orders.size());
    List<OrderDTO> pageContent = start >= orders.size() ? List.of() : orders.subList(start, end);
    Page<OrderDTO> orderPage = new PageImpl<>(pageContent, pageable, orders.size());

    return ResponseEntity.ok(new MessageResponse.PageResponse<>(orderPage));
  }

  @Operation(
      summary = "Get order by ID",
      description = "Fetches full details of a specific order by its ID.",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Order found",
            content = @Content(schema = @Schema(implementation = OrderDTO.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Order not found",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Order not found\"}")))
      })
  @GetMapping("/{orderId}")
  public ResponseEntity<OrderDTO> getOrderById(
      @AuthenticationPrincipal UserDetails authUser,
      @Parameter(description = "Order ID", example = "101") @PathVariable Long orderId) {
    User user = userService.findByUsername(authUser.getUsername());
    if (user == null) throw new ResourceNotFoundException("User not found");
    Order order = orderService.getOrderById(orderId, user);
    return ResponseEntity.ok(new OrderDTO(order));
  }

  @Operation(
      summary = "Cancel an order",
      description =
          "Cancels a pending or confirmed order. Cannot cancel shipped or delivered orders.",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Order cancelled successfully",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(value = "{\"message\": \"Order cancelled successfully\"}"))),
        @ApiResponse(
            responseCode = "400",
            description = "Order cannot be cancelled at this stage",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value = "{\"message\": \"Order cannot be cancelled at this stage\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "Order not found",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Order not found\"}")))
      })
  @DeleteMapping("/{orderId}/cancel")
  public ResponseEntity<MessageResponse> cancelOrder(
      @AuthenticationPrincipal UserDetails authUser,
      @Parameter(description = "Order ID", example = "101") @PathVariable Long orderId) {
    User user = userService.findByUsername(authUser.getUsername());
    if (user == null) throw new ResourceNotFoundException("User not found");
    orderService.cancelOrder(orderId, user);
    return ResponseEntity.ok(new MessageResponse("Order cancelled successfully"));
  }
}
