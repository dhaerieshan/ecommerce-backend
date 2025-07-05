package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.OrderDTO;
import com.borneo.ecommerce.dto.OrderItemDTO;
import com.borneo.ecommerce.model.Order;
import com.borneo.ecommerce.model.OrderItem;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.ProductRepository;
import com.borneo.ecommerce.service.OrderService;
import com.borneo.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final ProductRepository productRepository;

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkoutOrder(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User authUser,
            @RequestBody List<OrderItem> item) {

        User user = userService.findByUsername(authUser.getUsername());

        // ✅ Pass OrderItemDTOs to createOrder()
        Order order = orderService.createOrder(user, item);

        order.setStatus("in progress");

        // ✅ Convert Order to OrderDTO
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setUserId(order.getUser().getId());
        orderDTO.setTotalAmount(order.getTotalAmount());

        // ✅ Convert List<OrderItem> to List<OrderItemDTO>
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream().map(items -> {
            OrderItemDTO dto = new OrderItemDTO();
            dto.setOrderId(items.getOrder().getId());
            dto.setProductId(items.getProductId());  // ✅ Use OrderItem ID instead of Product ID
            dto.setQuantity(items.getQuantity());
            dto.setPrice(items.getPrice());
            return dto;
        }).collect(Collectors.toList());

        orderDTO.setItems(orderItemDTOs);

        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders(@AuthenticationPrincipal UserDetails authUser) {
        User user = userService.findByUsername(authUser.getUsername());
        List<OrderDTO> orders = orderService.getOrdersByUser(user)
                .stream()
                .map(OrderDTO::new)  // ✅ Uses the constructor above, so displayOrderNumber is included!
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@AuthenticationPrincipal UserDetails authUser, @PathVariable Long orderId) {
        User user = userService.findByUsername(authUser.getUsername());
        Order order = orderService.getOrderById(orderId, user);
        return ResponseEntity.ok(new OrderDTO(order));  // ✅ Again, displayOrderNumber included
    }

    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(
            @AuthenticationPrincipal UserDetails authUser,
            @PathVariable Long orderId) {

        User user = userService.findByUsername(authUser.getUsername());
        orderService.cancelOrder(orderId, user);

        return ResponseEntity.ok("Order cancelled successfully");
    }
}
