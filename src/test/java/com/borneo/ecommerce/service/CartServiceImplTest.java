package com.borneo.ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borneo.ecommerce.model.Cart;
import com.borneo.ecommerce.model.CartItem;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.CartRepository;
import com.borneo.ecommerce.repository.ProductRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("CartService Tests")
class CartServiceImplTest {

  @Mock private CartRepository cartRepository;
  @Mock private ProductRepository productRepository;

  @InjectMocks private CartServiceImpl cartService;

  private Cart testCart;
  private User testUser;
  private Product testProduct;
  private CartItem testCartItem;

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

    testCart = new Cart();
    testCart.setUser(testUser);
    testCart.setItems(new ArrayList<>());

    testCartItem = new CartItem();
    testCartItem.setId(1L);
    testCartItem.setCart(testCart);
    testCartItem.setProduct(testProduct);
    testCartItem.setQuantity(2);
  }

  @Test
  @DisplayName("Should get user cart successfully")
  void testGetUserCart() {
    when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));

    Cart result = cartService.getCartByUser(testUser);

    assertNotNull(result);
    assertEquals(testUser.getId(), result.getUser().getId());
    verify(cartRepository, times(1)).findByUser(testUser);
  }

  @Test
  @DisplayName("Should create new cart when user cart not found")
  void testGetUserCartCreateNewCart() {
    when(cartRepository.findByUser(testUser)).thenReturn(Optional.empty());
    when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

    Cart result = cartService.getCartByUser(testUser);

    assertNotNull(result);
    assertEquals(testUser.getId(), result.getUser().getId());
    verify(cartRepository, times(1)).save(any(Cart.class));
  }

  @Test
  @DisplayName("Should add product to cart successfully")
  void testAddProductToCart() {
    when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

    cartService.addProductToCart(testUser, 1L, 2);

    assertFalse(testCart.getItems().isEmpty());
    assertEquals(1, testCart.getItems().size());
    verify(cartRepository, times(1)).save(any(Cart.class));
  }

  @Test
  @DisplayName("Should throw exception when adding product with invalid product ID")
  void testAddProductToCartProductNotFound() {
    when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
    when(productRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(Exception.class, () -> cartService.addProductToCart(testUser, 999L, 2));

    verify(productRepository, times(1)).findById(999L);
  }

  @Test
  @DisplayName("Should remove product from cart successfully")
  void testRemoveProductFromCart() {
    testCart.getItems().add(testCartItem);
    when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
    when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

    cartService.removeProductFromCart(testUser, 1L);

    assertTrue(testCart.getItems().isEmpty());
    verify(cartRepository, times(1)).save(any(Cart.class));
  }

  @Test
  @DisplayName("Should clear cart successfully")
  void testClearCart() {
    testCart.getItems().add(testCartItem);
    when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
    when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

    cartService.clearCart(testUser);

    assertTrue(testCart.getItems().isEmpty());
    verify(cartRepository, times(1)).save(any(Cart.class));
  }

  @Test
  @DisplayName("Should delete cart by user")
  void testDeleteByUser() {
    when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));

    cartService.deleteByUser(testUser);

    verify(cartRepository, times(1)).delete(testCart);
  }

  @Test
  @DisplayName("Should handle adding multiple items to cart - increments quantity for same product")
  void testAddMultipleProductsToCart() {
    when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

    cartService.addProductToCart(testUser, 1L, 2);
    cartService.addProductToCart(testUser, 1L, 1);

    assertEquals(1, testCart.getItems().size());
    assertEquals(3, testCart.getItems().getFirst().getQuantity());
    verify(cartRepository, times(2)).save(any(Cart.class));
  }
}
