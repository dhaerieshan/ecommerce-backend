package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.MessageResponse;
import com.borneo.ecommerce.dto.WishlistDTO;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.service.UserService;
import com.borneo.ecommerce.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
            summary = "Get user's wishlist",
            description = "Returns all products saved in the authenticated user's wishlist.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Wishlist retrieved successfully",
                            content =
                            @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = WishlistDTO.class)))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Unauthorized\"}")))
            })
    @GetMapping
    public ResponseEntity<Set<WishlistDTO>> getWishlist(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(wishlistService.getWishlist(user));
    }

    @Operation(
            summary = "Add product to wishlist",
            description = "Saves a product to the authenticated user's wishlist by product ID.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product added to wishlist",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(value = "{\"message\": \"Product added to wishlist\"}"))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Product already in wishlist",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(
                                            value = "{\"message\": \"Product is already in wishlist\"}"))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Product not found\"}")))
            })
    @PostMapping("/{productId}")
    public ResponseEntity<MessageResponse> addToWishlist(
            @Parameter(description = "ID of the product to add", example = "1", required = true)
            @PathVariable
            Long productId,
            Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        boolean added = wishlistService.addToWishlist(user, productId);
        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponse("Product added to wishlist"));
        }
        throw new IllegalArgumentException("Product is already in wishlist or does not exist");
    }

    @Operation(
            summary = "Remove product from wishlist",
            description = "Removes a specific product from the authenticated user's wishlist.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product removed from wishlist",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(
                                            value = "{\"message\": \"Product removed from wishlist\"}"))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found in wishlist",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(value = "{\"message\": \"Product not found in wishlist\"}")))
            })
    @DeleteMapping("/{productId}")
    public ResponseEntity<MessageResponse> removeFromWishlist(
            @Parameter(description = "ID of the product to remove", example = "1", required = true)
            @PathVariable
            Long productId,
            Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        boolean removed = wishlistService.removeFromWishlist(user, productId);
        if (removed) {
            return ResponseEntity.ok(new MessageResponse("Product removed from wishlist"));
        }
        throw new ResourceNotFoundException("Product not found in wishlist");
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User not authenticated");
        }
        User user = userService.findByUsername(authentication.getName());
        if (user == null) throw new ResourceNotFoundException("User not found");
        return user;
    }
}
