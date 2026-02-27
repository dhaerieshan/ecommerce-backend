package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.AddToCartRequest;
import com.borneo.ecommerce.dto.CartDTO;
import com.borneo.ecommerce.dto.MessageResponse;
import com.borneo.ecommerce.dto.RemoveFromCartRequest;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Cart;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.service.CartService;
import com.borneo.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "07. Cart", description = "Shopping cart management APIs")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    @Operation(
            summary = "Get current user's cart",
            description = "Retrieves all items in the authenticated user's shopping cart.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CartDTO.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(
                                            value =
                                                    "{\"message\": \"Unauthorized - JWT token missing or invalid\"}"))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"User not found\"}")))
            })
    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) throw new ResourceNotFoundException("User not found");
        Cart cart = cartService.getCartByUser(user);
        return ResponseEntity.ok(new CartDTO(cart));
    }

    @Operation(
            summary = "Add item to cart",
            description =
                    "Adds a product with the specified quantity to the user's cart. Updates quantity if product already exists.",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody =
            @RequestBody(
                    description = "Product ID and quantity to add",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddToCartRequest.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product added to cart",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Product added to cart\"}"))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid product ID or quantity",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(
                                            value = "{\"message\": \"Invalid product ID or quantity\"}"))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Product not found\"}")))
            })
    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @org.springframework.web.bind.annotation.RequestBody AddToCartRequest request) {
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) throw new ResourceNotFoundException("User not found");
        cartService.addProductToCart(user, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(new MessageResponse("Product added to cart"));
    }

    @Operation(
            summary = "Remove item from cart",
            description = "Removes a specific product from the user's cart.",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody =
            @RequestBody(
                    description = "Product ID to remove",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RemoveFromCartRequest.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product removed from cart",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(value = "{\"message\": \"Product removed from cart\"}"))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found in cart",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(value = "{\"message\": \"Product not found in cart\"}")))
            })
    @DeleteMapping("/remove")
    public ResponseEntity<MessageResponse> removeFromCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @org.springframework.web.bind.annotation.RequestBody RemoveFromCartRequest request) {
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) throw new ResourceNotFoundException("User not found");
        cartService.removeProductFromCart(user, request.getProductId());
        return ResponseEntity.ok(new MessageResponse("Product removed from cart"));
    }

    @Operation(
            summary = "Clear entire cart",
            description = "Removes all items from the authenticated user's cart at once.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart cleared successfully",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(value = "{\"message\": \"Cart cleared successfully\"}"))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Unauthorized\"}")))
            })
    @DeleteMapping("/clear")
    public ResponseEntity<MessageResponse> clearCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) throw new ResourceNotFoundException("User not found");
        cartService.clearCart(user);
        return ResponseEntity.ok(new MessageResponse("Cart cleared successfully"));
    }
}
