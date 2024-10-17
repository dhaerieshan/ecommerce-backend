package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.AddToCartRequest;
import com.borneo.ecommerce.dto.CartDTO;
import com.borneo.ecommerce.dto.RemoveFromCartRequest;
import com.borneo.ecommerce.model.Cart;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.service.CartService;
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
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByUsername(userDetails.getUsername());
            Cart cart = cartService.getCartByUser(user);
            CartDTO cartDTO = new CartDTO(cart);
            return ResponseEntity.ok(cartDTO);
        } catch (Exception e) {
            // Log the error (use a logger in real applications)
            System.err.println("Error fetching cart: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
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
            // Log the error (use a logger in real applications)
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
            // Log the error (use a logger in real applications)
            System.err.println("Error removing from cart: " + e.getMessage());
            return ResponseEntity.status(500).body("Error removing product from cart");
        }
    }

    // Other methods...
}