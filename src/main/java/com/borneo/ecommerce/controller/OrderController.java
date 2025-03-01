package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.OrderRequest;
import com.borneo.ecommerce.dto.OrderResponse;
import com.borneo.ecommerce.model.Order;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.ProductRepository;
import com.borneo.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductRepository productRepository;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkoutOrder(@RequestBody OrderRequest orderRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        Order order = orderService.createOrder(user, orderRequest.getItems());
        return ResponseEntity.ok(new OrderResponse(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(@AuthenticationPrincipal User user) {
        List<OrderResponse> orders = orderService.getUserOrders(user)
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orders);
    }
}