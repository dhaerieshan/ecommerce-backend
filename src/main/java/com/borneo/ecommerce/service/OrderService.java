package com.borneo.ecommerce.service;

import com.borneo.ecommerce.model.Order;
import com.borneo.ecommerce.model.OrderItem;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.OrderRepository;
import com.borneo.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(User user, List<OrderItem> items) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        Order order = new Order();
        order.setUser(user);  // 🔴 Ensure the user is set
        order.setOrderDate(new Date());

        BigDecimal totalAmount = items.stream()
                .map(item -> BigDecimal.valueOf(item.getPrice()) // Ensure BigDecimal
                        .multiply(BigDecimal.valueOf(item.getQuantity()))) // Multiply correctly
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);
        order.setItems(items);

        items.forEach(item -> item.setOrder(order));  // 🔴 Ensure each item links back to the order

        return orderRepository.save(order);
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }
}