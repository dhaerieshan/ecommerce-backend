package com.borneo.ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.borneo.ecommerce.dto.ProductDTO;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.repository.CategoryRepository;
import com.borneo.ecommerce.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("ProductService Tests")
class ProductServiceImplTest {

  @Mock private ProductRepository productRepository;
  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private ProductServiceImpl productService;

  private Product testProduct;
  private ProductDTO testProductDTO;
  private Category testCategory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Initialize test data
    testCategory = new Category();
    testCategory.setId(1L);
    testCategory.setName("Electronics");

    testProduct = new Product();
    testProduct.setId(1L);
    testProduct.setName("Laptop");
    testProduct.setDescription("High-performance laptop");
    testProduct.setPrice(100000);
    testProduct.setStock(50);
    testProduct.setImagePath("/images/laptop.jpg");
    testProduct.setCategory(testCategory);

    testProductDTO = new ProductDTO();
    testProductDTO.setId(1L);
    testProductDTO.setName("Laptop");
    testProductDTO.setDescription("High-performance laptop");
    testProductDTO.setPrice(100000);
    testProductDTO.setStock(50);
    testProductDTO.setImagePath("/images/laptop.jpg");
    testProductDTO.setCategoryId(1L);
  }

  @Test
  @DisplayName("Should create product successfully")
  void testCreateProduct() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
    when(productRepository.save(any(Product.class))).thenReturn(testProduct);

    ProductDTO result = productService.createProduct(testProductDTO);

    assertNotNull(result);
    assertEquals("Laptop", result.getName());
    assertEquals(100000, result.getPrice());
    verify(categoryRepository, times(1)).findById(1L);
    verify(productRepository, times(1)).save(any(Product.class));
  }

  @Test
  @DisplayName("Should throw exception when creating product with invalid category")
  void testCreateProductWithInvalidCategory() {
    when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          testProductDTO.setCategoryId(999L);
          productService.createProduct(testProductDTO);
        });

    verify(categoryRepository, times(1)).findById(999L);
  }

  @Test
  @DisplayName("Should get all products successfully")
  void testGetAllProducts() {
    List<Product> products = new ArrayList<>();
    products.add(testProduct);

    when(productRepository.findAll()).thenReturn(products);

    List<ProductDTO> result = productService.getAllProducts();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Laptop", result.get(0).getName());
    verify(productRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Should return empty list when no products exist")
  void testGetAllProductsEmpty() {
    when(productRepository.findAll()).thenReturn(new ArrayList<>());

    List<ProductDTO> result = productService.getAllProducts();

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(productRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Should update product successfully")
  void testUpdateProduct() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

    testProductDTO.setName("Updated Laptop");
    testProductDTO.setPrice(120000);

    productService.updateProduct(1L, testProductDTO);

    assertEquals("Updated Laptop", testProduct.getName());
    assertEquals(120000, testProduct.getPrice());
    verify(productRepository, times(1)).findById(1L);
    verify(productRepository, times(1)).save(testProduct);
  }

  @Test
  @DisplayName("Should throw exception when updating non-existent product")
  void testUpdateProductNotFound() {
    when(productRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> productService.updateProduct(999L, testProductDTO));

    verify(productRepository, times(1)).findById(999L);
  }

  @Test
  @DisplayName("Should delete product successfully")
  void testDeleteProduct() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

    productService.deleteProduct(1L);

    verify(productRepository, times(1)).findById(1L);
    verify(productRepository, times(1)).delete(testProduct);
  }

  @Test
  @DisplayName("Should throw exception when deleting non-existent product")
  void testDeleteProductNotFound() {
    when(productRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class, () -> productService.deleteProduct(999L));

    verify(productRepository, times(1)).findById(999L);
  }

  @Test
  @DisplayName("Should get product by ID successfully")
  void testGetProductById() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

    ProductDTO result = productService.getProductById(1L);

    assertNotNull(result);
    assertEquals("Laptop", result.getName());
    verify(productRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Should throw exception when getting non-existent product")
  void testGetProductByIdNotFound() {
    when(productRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(999L));

    verify(productRepository, times(1)).findById(999L);
  }

  @Test
  @DisplayName("Should find products by category ID")
  void testFindByCategoryId() {
    List<Product> products = new ArrayList<>();
    products.add(testProduct);

    when(productRepository.findByCategoryId(1L)).thenReturn(products);

    List<ProductDTO> result = productService.findByCategoryId(1L);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(productRepository, times(1)).findByCategoryId(1L);
  }

  @Test
  @DisplayName("Should reduce stock successfully")
  void testReduceStock() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    int initialStock = testProduct.getStock();

    productService.reduceStock(1L, 10);

    assertEquals(initialStock - 10, testProduct.getStock());
    verify(productRepository, times(1)).save(testProduct);
  }
}

