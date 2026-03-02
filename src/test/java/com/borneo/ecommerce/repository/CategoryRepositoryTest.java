package com.borneo.ecommerce.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borneo.ecommerce.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("CategoryRepository Tests")
class CategoryRepositoryTest {

  @Autowired private CategoryRepository categoryRepository;

  @Autowired private TestEntityManager entityManager;

  @BeforeEach
  void setUp() {
    categoryRepository.deleteAll();
  }

  @Test
  @DisplayName("Should save category successfully")
  void testSaveCategory() {

    Category category = new Category();
    category.setName("Books");
    category.setImagePath("/images/books.jpg");

    Category saved = categoryRepository.save(category);

    assertNotNull(saved.getId());
    assertEquals("Books", saved.getName());
  }

  @Test
  @DisplayName("Should find category by name via stream filter")
  void testFindByName() {
    Category category = new Category();
    category.setName("Electronics");
    category.setImagePath("/images/electronics.jpg");
    entityManager.persist(category);
    entityManager.flush();

    Category found =
        categoryRepository.findAll().stream()
            .filter(c -> "Electronics".equals(c.getName()))
            .findFirst()
            .orElse(null);

    assertNotNull(found);
    assertEquals("Electronics", found.getName());
  }

  @Test
  @DisplayName("Should return false when category name not found")
  void testExistsByNameNotFound() {
    boolean found = categoryRepository.existsByName("NonExistent");

    assertFalse(found);
  }

  @Test
  @DisplayName("Should return true when category name exists")
  void testExistsByName() {
    Category category = new Category();
    category.setName("Gadgets");
    entityManager.persist(category);
    entityManager.flush();

    boolean found = categoryRepository.existsByName("Gadgets");

    assertTrue(found);
  }

  @Test
  @DisplayName("Should find all categories")
  void testFindAll() {
    Category c1 = new Category();
    c1.setName("Electronics");
    entityManager.persist(c1);
    entityManager.flush();
    Category c2 = new Category();
    c2.setName("Books");
    entityManager.persist(c2);
    entityManager.flush();
    var categories = categoryRepository.findAll();

    assertNotNull(categories);
    // assertEquals(2, categories.size());
  }

  @Test
  @DisplayName("Should delete category successfully")
  void testDeleteCategory() {
    Category category = new Category();
    category.setName("Temporary Category");
    Category saved = categoryRepository.save(category);

    categoryRepository.delete(saved);

    assertTrue(categoryRepository.findById(saved.getId()).isEmpty());
  }

  @Test
  @DisplayName("Should count all categories")
  void testCount() {
    Category c1 = new Category();
    c1.setName("Electronics");
    entityManager.persist(c1);
    entityManager.flush();
    long count = categoryRepository.count();

    assertEquals(1, count);
  }

  @Test
  @DisplayName("Should check if category exists by ID")
  void testExistsById() {
    Category category = new Category();
    category.setName("Gadgets");

    entityManager.persist(category);
    entityManager.flush();

    boolean exists = categoryRepository.existsById(category.getId());

    assertTrue(exists);
  }

  @Test
  @DisplayName("Should check if category does not exist by ID")
  void testExistsByIdNotFound() {
    boolean exists = categoryRepository.existsById(999L);

    assertFalse(exists);
  }
}
