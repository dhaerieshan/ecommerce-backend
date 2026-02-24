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
        order.setStatus("IN_PROGRESS");

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItem itemDTO : items) {
            OrderItem orderItem = new OrderItem();
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDTO.getProductId()));

            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(product.getPrice()));
            orderItem.setOrder(order);

            BigDecimal itemTotal = BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        //   Save once to generate ID
        Order savedOrder = orderRepository.save(order);

        //   Now the ID exists, so build displayOrderNumber correctly
        String randomPart = String.format("%04d", (int) (Math.random() * 10000));
        String idPart = String.format("%02d", savedOrder.getId());
        savedOrder.setDisplayOrderNumber(randomPart + idPart);

        //   Save again with the displayOrderNumber
        savedOrder = orderRepository.save(savedOrder);

        //   Save the items too
        orderItemRepository.saveAll(orderItems);

        return savedOrder;
    }
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    //   Fetch a single order by ID
    public Order getOrderById(Long orderId, User user) {
        return orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public void cancelOrder(Long orderId, User user) {
        Order order = getOrderById(orderId, user);
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }
}