// src/main/java/com/borneo/ecommerce/service/WishlistService.java

package com.borneo.ecommerce.service;

import com.borneo.ecommerce.dto.WishlistDTO;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.model.Wishlist;
import com.borneo.ecommerce.repository.ProductRepository;
import com.borneo.ecommerce.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    // Method to add a product to the wishlist
    public boolean addToWishlist(User user, Long productId) {
        // Check if product exists
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return false; // Product does not exist
        }

        // Check if already in wishlist
        boolean exists = wishlistRepository.existsByUserAndProductId(user, productId);
        if (exists) {
            return false; // Already in wishlist
        }

        // Create and save wishlist entry
        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .dateAdded(LocalDateTime.now())
                .build();

        wishlistRepository.save(wishlist);
        return true;
    }

    // Method to remove a product from the wishlist
    public boolean removeFromWishlist(User user, Long productId) {
        // Find the wishlist entry
        Wishlist wishlist = wishlistRepository.findByUserAndProductId(user, productId);
        if (wishlist == null) {
            return false; // Not found in wishlist
        }

        // Remove the wishlist entry
        wishlistRepository.delete(wishlist);
        return true;
    }

    // Method to get all wishlist products for a user
    public Set<WishlistDTO> getWishlist(User user) {
        Set<Wishlist> wishlists = wishlistRepository.findByUser(user);
        return wishlists.stream().map(wishlist -> {
            WishlistDTO dto = new WishlistDTO();
            dto.setId(wishlist.getId());
            dto.setUserId(wishlist.getUser().getId());
            dto.setProductId(wishlist.getProduct().getId());
            dto.setDateAdded(wishlist.getDateAdded());
            return dto;
        }).collect(Collectors.toSet());
    }
}