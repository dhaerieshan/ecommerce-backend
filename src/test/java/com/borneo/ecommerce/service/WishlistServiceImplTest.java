package com.borneo.ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.borneo.ecommerce.dto.WishlistDTO;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.model.Wishlist;
import com.borneo.ecommerce.repository.ProductRepository;
import com.borneo.ecommerce.repository.WishlistRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("WishlistService Tests")
class WishlistServiceImplTest {

  @Mock private WishlistRepository wishlistRepository;
  @Mock private ProductRepository productRepository;

  @InjectMocks private WishlistService wishlistService;

  private User testUser;
  private Product testProduct;
  private Wishlist testWishlist;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testuser");

    testProduct = new Product();
    testProduct.setId(1L);
    testProduct.setName("Laptop");
    testProduct.setPrice(100000);

    testWishlist = Wishlist.builder()
        .user(testUser)
        .product(testProduct)
        .dateAdded(LocalDateTime.now())
        .build();
    testWishlist.setId(1L);
  }

  @Test
  @DisplayName("Should add product to wishlist successfully")
  void testAddToWishlist() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    when(wishlistRepository.existsByUserAndProductId(testUser, 1L)).thenReturn(false);
    when(wishlistRepository.save(any(Wishlist.class))).thenReturn(testWishlist);

    boolean result = wishlistService.addToWishlist(testUser, 1L);

    assertTrue(result);
    verify(wishlistRepository, times(1)).save(any(Wishlist.class));
  }

  @Test
  @DisplayName("Should return false when adding already wishlisted product")
  void testAddToWishlistAlreadyExists() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    when(wishlistRepository.existsByUserAndProductId(testUser, 1L)).thenReturn(true);

    boolean result = wishlistService.addToWishlist(testUser, 1L);

    assertFalse(result);
    verify(wishlistRepository, never()).save(any(Wishlist.class));
  }

  @Test
  @DisplayName("Should return false when product not found during add")
  void testAddToWishlistProductNotFound() {
    when(productRepository.findById(999L)).thenReturn(Optional.empty());

    boolean result = wishlistService.addToWishlist(testUser, 999L);

    assertFalse(result);
    verify(wishlistRepository, never()).save(any(Wishlist.class));
  }

  @Test
  @DisplayName("Should remove product from wishlist successfully")
  void testRemoveFromWishlist() {
    when(wishlistRepository.findByUserAndProductId(testUser, 1L)).thenReturn(testWishlist);

    boolean result = wishlistService.removeFromWishlist(testUser, 1L);

    assertTrue(result);
    verify(wishlistRepository, times(1)).delete(testWishlist);
  }

  @Test
  @DisplayName("Should return false when removing non-existent wishlist item")
  void testRemoveFromWishlistNotFound() {
    when(wishlistRepository.findByUserAndProductId(testUser, 999L)).thenReturn(null);

    boolean result = wishlistService.removeFromWishlist(testUser, 999L);

    assertFalse(result);
    verify(wishlistRepository, never()).delete(any());
  }

  @Test
  @DisplayName("Should get wishlist for user")
  void testGetWishlist() {
    when(wishlistRepository.findByUser(testUser)).thenReturn(Set.of(testWishlist));

    Set<WishlistDTO> result = wishlistService.getWishlist(testUser);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(wishlistRepository, times(1)).findByUser(testUser);
  }

  @Test
  @DisplayName("Should return empty set when wishlist is empty")
  void testGetWishlistEmpty() {
    when(wishlistRepository.findByUser(testUser)).thenReturn(Set.of());

    Set<WishlistDTO> result = wishlistService.getWishlist(testUser);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
