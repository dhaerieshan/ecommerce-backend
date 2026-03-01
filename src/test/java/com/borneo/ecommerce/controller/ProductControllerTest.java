package com.borneo.ecommerce.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.repository.CategoryRepository;
import com.borneo.ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("ProductController Integration Tests")
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ProductRepository productRepository;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private ObjectMapper objectMapper;

  private Category testCategory;
  private Product testProduct;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();
    categoryRepository.deleteAll();

    testCategory = new Category();
    testCategory.setName("Electronics");
    testCategory = categoryRepository.save(testCategory);

    testProduct = new Product();
    testProduct.setName("Test Laptop");
    testProduct.setDescription("Test laptop description");
    testProduct.setPrice(100000);
    testProduct.setStock(50);
    testProduct.setImagePath("/images/test-laptop.jpg");
    testProduct.setCategory(testCategory);
    testProduct = productRepository.save(testProduct);
  }

  @Test
  @DisplayName("Should get all products with status 200")
  void testGetAllProducts() throws Exception {
    mockMvc
        .perform(get("/api/products").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content.[0].name").value("Test Laptop"));
  }

  @Test
  @DisplayName("Should get product by ID successfully")
  void testGetProductById() throws Exception {
    mockMvc
        .perform(
            get("/api/products/" + testProduct.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Test Laptop"))
        .andExpect(jsonPath("$.price").value(100000));
  }

  @Test
  @DisplayName("Should return 404 when getting non-existent product")
  void testGetProductByIdNotFound() throws Exception {
    mockMvc
        .perform(get("/api/products/999").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Should search products by keyword")
  void testSearchProducts() throws Exception {
    mockMvc
        .perform(
            get("/api/products/search")
                .param("query", "description")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].name").value("Test Laptop"));
  }

  @Test
  @DisplayName("Should return empty list for non-matching search")
  void testSearchProductsNoResults() throws Exception {
    mockMvc
        .perform(
            get("/api/products/search")
                .param("query", "NonExistentProduct")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  @DisplayName("Should get products by category using query param")
  void testGetProductsByCategory() throws Exception {
    mockMvc
        .perform(
            get("/api/products")
                .param("categoryId", String.valueOf(testCategory.getId()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1));
  }

  @Test
  @DisplayName("Should return featured products")
  void testGetFeaturedProducts() throws Exception {
    mockMvc
        .perform(get("/api/products/featured").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
