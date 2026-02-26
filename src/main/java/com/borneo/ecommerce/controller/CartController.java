package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.*;
import com.borneo.ecommerce.model.Cart;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.service.CartService;
import com.borneo.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "07. Cart", description = "Shopping cart management APIs")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Operation(
            summary = "Add item to cart",
            description = "Adds a product with specified quantity to the user's cart",
            tags = {"Cart"},
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item added to cart",
                            content = @Content(schema = @Schema(implementation = CartDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid product or quantity"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
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

    @Operation(
            summary = "Remove item from cart",
            description = "Removes a specific product from the user's cart by product ID",
            tags = {"Cart"},
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item removed from cart",
                            content = @Content(schema = @Schema(implementation = CartItemDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found in cart")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/remove")
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

    @Operation(
            summary = "Get current user's cart",
            description = "Retrieves all items currently in the authenticated user's shopping cart",
            tags = {"Cart"},
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CartDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
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

    @Operation(
            summary = "Clear entire cart",
            description = "Removes all items from the authenticated user's cart",
            tags = {"Cart"},
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cart cleared successfully",
                            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
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

}
