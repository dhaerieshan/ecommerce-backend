package com.borneo.ecommerce.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.borneo.ecommerce.dto.AddToCartRequest;
import com.borneo.ecommerce.dto.RemoveFromCartRequest;
import com.borneo.ecommerce.model.Cart;
import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.CartRepository;
import com.borneo.ecommerce.repository.CategoryRepository;
import com.borneo.ecommerce.repository.ProductRepository;
import com.borneo.ecommerce.repository.RoleRepository;
import com.borneo.ecommerce.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("CartController Integration Tests")
class CartControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private CartRepository cartRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private ProductRepository productRepository;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private RoleRepository roleRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private ObjectMapper objectMapper;

  private User testUser;
  private Product testProduct;

  @BeforeEach
  void setUp() {
    cartRepository.deleteAll();
    productRepository.deleteAll();
    categoryRepository.deleteAll();
    userRepository.deleteAll();
    roleRepository.deleteAll();

    Role userRole = new Role();
    userRole.setName("USER");
    userRole = roleRepository.save(userRole);

    testUser = new User();
    testUser.setUsername("testuser");
    testUser.setEmail("testuser@example.com");
    testUser.setPassword(passwordEncoder.encode("password123"));
    testUser.setFirstName("Test");
    testUser.setLastName("User");
    testUser.setAddress("123 Street");
    testUser.setRoles(java.util.Set.of(userRole));
    testUser = userRepository.save(testUser);

    Category category = new Category();
    category.setName("Electronics");
    category = categoryRepository.save(category);

    testProduct = new Product();
    testProduct.setName("Laptop");
    testProduct.setDescription("Test Laptop");
    testProduct.setPrice(100000);
    testProduct.setStock(50);
    testProduct.setCategory(category);
    testProduct = productRepository.save(testProduct);

    Cart testCart = new Cart();
    testCart.setUser(testUser);
    cartRepository.save(testCart);
  }

  @Test
  @DisplayName("Should get user cart successfully when authenticated")
  @WithMockUser(username = "testuser")
  void testGetUserCart() throws Exception {
    mockMvc
        .perform(get("/api/cart").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Should return 401 when accessing cart without authentication")
  void testGetUserCartUnauthenticated() throws Exception {
    mockMvc
        .perform(get("/api/cart").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Should add item to cart successfully when authenticated")
  @WithMockUser(username = "testuser")
  void testAddItemToCart() throws Exception {
    AddToCartRequest request = new AddToCartRequest();
    request.setProductId(testProduct.getId());
    request.setQuantity(2);

    mockMvc
        .perform(
            post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Product added to cart"));
  }

  @Test
  @DisplayName("Should return 4xx when adding item with invalid product")
  @WithMockUser(username = "testuser")
  void testAddItemToCartInvalidProduct() throws Exception {
    AddToCartRequest request = new AddToCartRequest();
    request.setProductId(999L);
    request.setQuantity(2);

    mockMvc
        .perform(
            post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @DisplayName("Should remove item from cart successfully when authenticated")
  @WithMockUser(username = "testuser")
  void testRemoveItemFromCart() throws Exception {
    // First add item
    AddToCartRequest addRequest = new AddToCartRequest();
    addRequest.setProductId(testProduct.getId());
    addRequest.setQuantity(2);
    mockMvc.perform(
        post("/api/cart/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(addRequest)));

    RemoveFromCartRequest removeRequest = new RemoveFromCartRequest();
    removeRequest.setProductId(testProduct.getId());

    mockMvc
        .perform(
            delete("/api/cart/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(removeRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Product removed from cart"));
  }

  @Test
  @DisplayName("Should clear cart successfully when authenticated")
  @WithMockUser(username = "testuser")
  void testClearCart() throws Exception {
    mockMvc
        .perform(delete("/api/cart/clear").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Cart cleared successfully"));
  }
}
