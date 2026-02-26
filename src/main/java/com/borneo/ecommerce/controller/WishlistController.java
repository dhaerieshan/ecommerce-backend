

package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.WishlistDTO;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.service.UserService;
import com.borneo.ecommerce.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "08. Wishlist", description = "Wishlist management APIs")
@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Add product to wishlist",
            description = "Saves a product to the authenticated user's wishlist",
            tags = {"Wishlist"},
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product added to wishlist",
                            content = @Content(schema = @Schema(implementation = WishlistDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found"),
                    @ApiResponse(responseCode = "409", description = "Product already in wishlist")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
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

    @Operation(
            summary = "Get user's wishlist",
            description = "Returns all products saved in the authenticated user's wishlist",
            tags = {"Wishlist"},
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Wishlist retrieved successfully",
                            content = @Content(schema = @Schema(implementation = WishlistDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
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


    @Operation(
            summary = "Remove product from wishlist",
            description = "Removes a specific product from the user's wishlist",
            tags = {"Wishlist"},
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product removed from wishlist",
                            content = @Content(schema = @Schema(implementation = WishlistDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found in wishlist")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
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


    private Long getUserId(Authentication authentication) throws Exception {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("User not authenticated");
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username);   

        if (user != null) {
            return user.getId();
        } else {
            throw new Exception("User not found");
        }
    }
}