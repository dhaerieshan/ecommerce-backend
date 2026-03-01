package com.borneo.ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.borneo.ecommerce.dto.CategoryDTO;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("CategoryService Tests")
class CategoryServiceImplTest {

  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private CategoryServiceImpl categoryService;

  private Category testCategory;
  private CategoryDTO testCategoryDTO;

  @BeforeEach
  void setUp() {
    @SuppressWarnings("resource")
    AutoCloseable _ = MockitoAnnotations.openMocks(this);

    testCategory = new Category();
    testCategory.setId(1L);
    testCategory.setName("Electronics");
    testCategory.setImagePath("/images/electronics.jpg");

    testCategoryDTO = new CategoryDTO();
    testCategoryDTO.setId(1L);
    testCategoryDTO.setName("Electronics");
    testCategoryDTO.setImagePath("/images/electronics.jpg");
  }

  @Test
  @DisplayName("Should create category successfully")
  void testCreateCategory() {
    when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

    CategoryDTO result = categoryService.createCategory(testCategoryDTO);

    assertNotNull(result);
    assertEquals("Electronics", result.getName());
    verify(categoryRepository, times(1)).save(any(Category.class));
  }
@Test
@DisplayName("Should get all categories successfully")
void testGetAllCategories() {
  List<Category> categories = new ArrayList<>();
  categories.add(testCategory);
  when(categoryRepository.findByParentIsNull()).thenReturn(categories);
  List<CategoryDTO> result = categoryService.getAllCategories();
  assertNotNull(result);
  assertEquals(1, result.size());
  assertEquals("Electronics", result.getFirst().getName());
  verify(categoryRepository, times(1)).findByParentIsNull();
}

@Test
@DisplayName("Should return empty list when no categories exist")
void testGetAllCategoriesEmpty() {
  when(categoryRepository.findByParentIsNull()).thenReturn(new ArrayList<>());

  List<CategoryDTO> result = categoryService.getAllCategories();

  assertNotNull(result);
  assertTrue(result.isEmpty());
  verify(categoryRepository, times(1)).findByParentIsNull();
}
  @Test
  @DisplayName("Should get category by ID successfully")
  void testGetCategoryById() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

    CategoryDTO result = categoryService.getCategoryById(1L);

    assertNotNull(result);
    assertEquals("Electronics", result.getName());
    verify(categoryRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Should throw exception when getting non-existent category")
  void testGetCategoryByIdNotFound() {
    when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class, () -> categoryService.getCategoryById(999L));

    verify(categoryRepository, times(1)).findById(999L);
  }

  @Test
  @DisplayName("Should update category successfully")
  void testUpdateCategory() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
    when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

    testCategoryDTO.setName("Updated Electronics");

    categoryService.updateCategory(1L, testCategoryDTO);

    assertEquals("Updated Electronics", testCategory.getName());
    verify(categoryRepository, times(1)).findById(1L);
    verify(categoryRepository, times(1)).save(testCategory);
  }

  @Test
  @DisplayName("Should throw exception when updating non-existent category")
  void testUpdateCategoryNotFound() {
    when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> categoryService.updateCategory(999L, testCategoryDTO));

    verify(categoryRepository, times(1)).findById(999L);
  }

  @Test
  @DisplayName("Should delete category successfully")
  void testDeleteCategory() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

    categoryService.deleteCategory(1L);

    verify(categoryRepository, times(1)).findById(1L);
    verify(categoryRepository, times(1)).delete(testCategory);
  }

  @Test
  @DisplayName("Should throw exception when deleting non-existent category")
  void testDeleteCategoryNotFound() {
    when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class, () -> categoryService.deleteCategory(999L));

    verify(categoryRepository, times(1)).findById(999L);
  }
}

