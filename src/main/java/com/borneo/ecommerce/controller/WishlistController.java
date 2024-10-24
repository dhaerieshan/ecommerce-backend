// src/main/java/com/borneo/ecommerce/controller/WishlistController.java

package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.WishlistDTO;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.service.UserService;
import com.borneo.ecommerce.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private UserService userService;

    // Endpoint to add a product to the wishlist
    @PostMapping("/{productId}")
    public ResponseEntity<?> addToWishlist(@PathVariable Long productId, Authentication authentication) {
        try {
            Long userId = getUserId(authentication);
            User user = userService.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

            boolean added = wishlistService.addToWishlist(user, productId);

            if (added) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Product added to wishlist");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is already in wishlist or does not exist");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // Endpoint to remove a product from the wishlist
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long productId, Authentication authentication) {
        try {
            Long userId = getUserId(authentication);
            User user = userService.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

            boolean removed = wishlistService.removeFromWishlist(user, productId);

            if (removed) {
                return ResponseEntity.ok("Product removed from wishlist");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found in wishlist");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // Endpoint to get all products in the wishlist
    @GetMapping
    public ResponseEntity<?> getWishlist(Authentication authentication) {
        try {
            Long userId = getUserId(authentication);
            User user = userService.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

            Set<WishlistDTO> wishlist = wishlistService.getWishlist(user);
            return ResponseEntity.ok(wishlist);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // Helper method to extract user ID from Authentication
    private Long getUserId(Authentication authentication) throws Exception {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("User not authenticated");
        }

        String username = authentication.getName(); // Extract username
        User user = userService.findByUsername(username); // Fetch User entity

        if (user != null) {
            return user.getId();
        } else {
            throw new Exception("User not found");
        }
    }
}