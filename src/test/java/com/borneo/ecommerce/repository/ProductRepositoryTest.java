package com.borneo.ecommerce.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("ProductRepository Tests")
class ProductRepositoryTest {

  @Autowired private ProductRepository productRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Autowired private TestEntityManager entityManager;

  @BeforeEach
  void setup() {
    productRepository.deleteAll();
    categoryRepository.deleteAll();
  }

  @Test
  @DisplayName("Should save product successfully")
  void testSaveProduct() {
    Category category = new Category();
    category.setName("Electronics");
    category = categoryRepository.save(category);

    Product product = new Product();
    product.setName("Phone");
    product.setPrice(50000);
    product.setStock(20);
    product.setCategory(category);

    Product saved = productRepository.save(product);

    assertNotNull(saved.getId());
    assertEquals("Phone", saved.getName());
  }

  @Test
  @DisplayName("Should find product by name")
  void testFindByName() {
    Product product = new Product();
    product.setName("new");
    product.setPrice(100000);
    product.setStock(10);
    entityManager.persist(product);
    entityManager.flush();

    Optional<Product> found = productRepository.findById(1L);

    assertNotNull(found);
    assertEquals("new", found.get().getName());
  }

  @Test
  @DisplayName("Should find products by category ID")
  void testFindByCategoryId() {
    Category category = new Category();
    category.setName("Electronics");

    Product product = new Product();
    product.setName("Laptop");
    product.setPrice(100000);
    product.setStock(10);
    product.setCategory(category);
    entityManager.persist(category);
    entityManager.persist(product);
    entityManager.flush();

    List<Product> products = productRepository.findByCategoryId(category.getId());

    assertEquals(1, products.size());
    assertEquals("Laptop", products.get(0).getName());
  }

  @Test
  @DisplayName("Should delete product successfully")
  void testDeleteProduct() {
    Category category = new Category();
    category.setName("Electronics");
    category = categoryRepository.save(category);

    Product product = new Product();
    product.setName("Laptop");
    product.setPrice(100000);
    product.setStock(10);
    product.setCategory(category);
    Product saved = productRepository.save(product);

    productRepository.delete(saved);

    assertFalse(productRepository.existsById(saved.getId()));
  }

  @Test
  @DisplayName("should bring top 5 producits by category id")
  void testFindTop5ByCategoryIdOrderByCreatedAtDesc() {
    Category category = new Category();
    category.setName("Electronics");

    Category subCategory = new Category();
    subCategory.setName("mobile");
    subCategory.setParent(category);

    Product product = new Product();
    product.setName("mobile1");
    product.setDescription("A powerful mobile");
    product.setPrice(100000);
    product.setStock(10);
    product.setCategory(subCategory);

    Product product1 = new Product();
    product1.setName("mobile2");
    product1.setDescription("A powerful mobile");
    product1.setPrice(100000);
    product1.setStock(10);
    product1.setCategory(subCategory);

    entityManager.persist(category);
    entityManager.persist(subCategory);
    entityManager.persist(product);
    entityManager.persist(product1);
    entityManager.flush();

    List<Product> result =
        productRepository.findTop5ByCategoryAndIdNot(subCategory, product.getId());
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("Should find product by ID")
  void testFindById() {
    Category category = new Category();
    category.setName("Electronics");
    entityManager.persist(category);

    Product product = new Product();
    product.setName("Laptop");
    product.setPrice(100000);
    product.setStock(10);
    product.setCategory(category);
    entityManager.persist(product);
    entityManager.flush();

    Optional<Product> found = productRepository.findById(product.getId());

    assertTrue(found.isPresent());
    assertEquals("Laptop", found.get().getName());
  }

  @Test
  @DisplayName("find products  by categories and subcategories")
  void testFindByCategoryIds() {
    Category parentCategory = new Category();
    parentCategory.setName("Electronics");
    Category subCategory = new Category();
    subCategory.setName("Laptops");
    subCategory.setParent(parentCategory);

    Product product = new Product();
    product.setName("acer laptop");
    product.setPrice(100000);
    product.setStock(10);
    product.setCategory(subCategory);
    entityManager.persist(parentCategory);
    entityManager.persist(subCategory);
    entityManager.persist(product);
    entityManager.flush();

    List<Product> result =
        productRepository.findByCategoryIds(List.of(parentCategory.getId(), subCategory.getId()));
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("should find products by name or description or category ignore case")
  void
      testFindByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategory_NameContainingIgnoreCase() {
    Category category = new Category();
    category.setName("Electronics");
    category = categoryRepository.save(category);

    Product product = new Product();
    product.setName("Laptop");
    product.setDescription("A powerful laptop");
    product.setPrice(100000);
    product.setStock(10);
    product.setCategory(category);
    productRepository.save(product);

    List<Product> result = productRepository.searchByNameDescriptionOrCategory("powerful");
    List<Product> result1 = productRepository.searchByNameDescriptionOrCategory("electronics");
    List<Product> result2 = productRepository.searchByNameDescriptionOrCategory("a");
    List<Product> result3 = productRepository.searchByNameDescriptionOrCategory("LAPTOP");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Laptop", result.get(0).getName());
    assertEquals("Laptop", result1.get(0).getName());
    assertEquals("Laptop", result2.get(0).getName());
    assertEquals("Laptop", result3.get(0).getName());
  }
}
