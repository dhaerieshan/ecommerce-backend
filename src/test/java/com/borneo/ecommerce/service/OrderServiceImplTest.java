package com.borneo.ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.borneo.ecommerce.model.Order;
import com.borneo.ecommerce.model.OrderItem;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.OrderItemRepository;
import com.borneo.ecommerce.repository.OrderRepository;
import com.borneo.ecommerce.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("OrderService Tests")
class OrderServiceImplTest {

  @Mock private OrderRepository orderRepository;
  @Mock private ProductRepository productRepository;
  @Mock private OrderItemRepository orderItemRepository;

  @InjectMocks private OrderService orderService;

  private User testUser;
  private Product testProduct;
  private Order testOrder;
  private OrderItem testOrderItem;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testuser");
    testUser.setEmail("test@example.com");

    testProduct = new Product();
    testProduct.setId(1L);
    testProduct.setName("Laptop");
    testProduct.setPrice(100000);
    testProduct.setStock(50);

    testOrder = new Order();
    testOrder.setId(1L);
    testOrder.setUser(testUser);
    testOrder.setTotalAmount(BigDecimal.valueOf(200000));
    testOrder.setStatus("IN_PROGRESS");

    testOrderItem = new OrderItem();
    testOrderItem.setId(1L);
    testOrderItem.setProduct(testProduct);
    testOrderItem.setQuantity(2);
    testOrderItem.setPrice(BigDecimal.valueOf(100000));
  }

  @Test
  @DisplayName("Should create order successfully")
  void testCreateOrder() {
    List<OrderItem> items = new ArrayList<>();
    items.add(testOrderItem);

    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
    when(orderItemRepository.saveAll(any())).thenReturn(items);

    Order result = orderService.createOrder(testUser, items);

    assertNotNull(result);
    assertEquals(testUser.getId(), result.getUser().getId());
    verify(orderRepository, atLeastOnce()).save(any(Order.class));
  }

  @Test
  @DisplayName("Should get order by ID successfully")
  void testGetOrderById() {
    when(orderRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testOrder));

    Order result = orderService.getOrderById(1L, testUser);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    verify(orderRepository, times(1)).findByIdAndUser(1L, testUser);
  }

  @Test
  @DisplayName("Should throw exception when order not found")
  void testGetOrderByIdNotFound() {
    when(orderRepository.findByIdAndUser(999L, testUser)).thenReturn(Optional.empty());

    assertThrows(Exception.class, () -> orderService.getOrderById(999L, testUser));

    verify(orderRepository, times(1)).findByIdAndUser(999L, testUser);
  }

  @Test
  @DisplayName("Should cancel order successfully")
  void testCancelOrder() {
    when(orderRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testOrder));
    when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

    orderService.cancelOrder(1L, testUser);

    assertEquals("CANCELLED", testOrder.getStatus());
    verify(orderRepository, times(1)).save(any(Order.class));
  }

  @Test
  @DisplayName("Should get orders by user")
  void testGetOrdersByUser() {
    List<Order> orders = List.of(testOrder);
    when(orderRepository.findByUserOrderByOrderDateDesc(testUser)).thenReturn(orders);

    List<Order> result = orderService.getOrdersByUser(testUser);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).getId());
    verify(orderRepository, times(1)).findByUserOrderByOrderDateDesc(testUser);
  }

  @Test
  @DisplayName("Should return empty list when user has no orders")
  void testGetOrdersByUserEmpty() {
    when(orderRepository.findByUserOrderByOrderDateDesc(testUser)).thenReturn(new ArrayList<>());

    List<Order> result = orderService.getOrdersByUser(testUser);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
