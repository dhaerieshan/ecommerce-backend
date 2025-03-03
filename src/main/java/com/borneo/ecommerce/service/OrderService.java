package com.borneo.ecommerce.service;

import com.borneo.ecommerce.model.Order;
import com.borneo.ecommerce.model.OrderItem;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.OrderItemRepository;
import com.borneo.ecommerce.repository.OrderRepository;
import com.borneo.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;



    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public Order createOrder(User user, List<OrderItem> items) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());

        BigDecimal totalAmount = BigDecimal.ZERO; // ✅ Declare totalAmount before lambda

        List<OrderItem> orderItems = new ArrayList<>(); // ✅ Store order items in a list

        for (OrderItem itemDTO : items) {
            OrderItem orderItem = new OrderItem();
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDTO.getProductId()));

            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(product.getPrice()));
            orderItem.setOrder(order); // ✅ Associate with order

            BigDecimal itemTotal = BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalAmount = totalAmount.add(itemTotal); // ✅ Accumulate total safely

            orderItems.add(orderItem); // ✅ Add order item to list
        }

        order.setTotalAmount(totalAmount); // ✅ Set total amount before saving
        order.setOrderItems(orderItems);

        orderRepository.save(order); // ✅ Save the order first
        orderItemRepository.saveAll(orderItems); // ✅ Save order items after order

        return order;
    }
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    // ✅ Fetch a single order by ID
    public Order getOrderById(Long orderId, User user) {
        return orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}