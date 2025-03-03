package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.AddToCartRequest;
import com.borneo.ecommerce.dto.CartDTO;
import com.borneo.ecommerce.dto.RemoveFromCartRequest;
import com.borneo.ecommerce.model.Cart;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.service.CartService;
import com.borneo.ecommerce.service.OrderService;
import com.borneo.ecommerce.service.ProductService;
import com.borneo.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;


    @Autowired
    private ProductService productService; // Service to update product stock


    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByUsername(userDetails.getUsername());
            Cart cart = cartService.getCartByUser(user);
            CartDTO cartDTO = new CartDTO(cart);
            return ResponseEntity.ok(cartDTO);
        } catch (Exception e) {
            System.err.println("Error fetching cart: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByUsername(userDetails.getUsername());
            cartService.clearCart(user);
            return ResponseEntity.ok("Cart cleared successfully");
        } catch (Exception e) {
            System.err.println("Error clearing cart: " + e.getMessage());
            return ResponseEntity.status(500).body("Error clearing cart");
        }
    }
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddToCartRequest request) {
        try {
            User user = userService.findByUsername(userDetails.getUsername());
            cartService.addProductToCart(user, request.getProductId(), request.getQuantity());
            return ResponseEntity.ok("Product added to cart");
        } catch (Exception e) {
            System.err.println("Error adding to cart: " + e.getMessage());
            return ResponseEntity.status(500).body("Error adding product to cart");
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFromCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RemoveFromCartRequest request) {
        try {
            User user = userService.findByUsername(userDetails.getUsername());
            cartService.removeProductFromCart(user, request.getProductId());
            return ResponseEntity.ok("Product removed from cart");
        } catch (Exception e) {
            System.err.println("Error removing from cart: " + e.getMessage());
            return ResponseEntity.status(500).body("Error removing product from cart");
        }
    }

//    @PostMapping("/checkout")
//    public ResponseEntity<OrderResponse> checkoutCart(@AuthenticationPrincipal User user) {
//        Cart cart = cartService.getCartByUser(user); // Get the cart
//        List<OrderItem> cartItems = cart.getItems().stream()
//                .map(OrderItem::new) // Convert each CartItem to OrderItem
//                .collect(Collectors.toList());
//
//        if (cartItems.isEmpty()) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        Order order = orderService.createOrder(user, cartItems);
//        cartService.clearCart(user);
//        return ResponseEntity.ok(new OrderResponse(order)); // Ensure OrderResponse has a constructor for Order
//    }
    // Other methods...
}