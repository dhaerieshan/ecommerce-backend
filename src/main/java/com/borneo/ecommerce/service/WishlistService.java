// src/main/java/com/borneo/ecommerce/service/WishlistService.java

package com.borneo.ecommerce.service;

import com.borneo.ecommerce.dto.WishlistDTO;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.model.Wishlist;
import com.borneo.ecommerce.repository.ProductRepository;
import com.borneo.ecommerce.repository.WishlistRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

  @Autowired private WishlistRepository wishlistRepository;

  @Autowired private ProductRepository productRepository;

  public boolean addToWishlist(User user, Long productId) {

    Product product = productRepository.findById(productId).orElse(null);
    if (product == null) {
      return false;
    }

    boolean exists = wishlistRepository.existsByUserAndProductId(user, productId);
    if (exists) {
      return false;
    }

    Wishlist wishlist =
        Wishlist.builder().user(user).product(product).dateAdded(LocalDateTime.now()).build();

    wishlistRepository.save(wishlist);
    return true;
  }

  public boolean removeFromWishlist(User user, Long productId) {

    Wishlist wishlist = wishlistRepository.findByUserAndProductId(user, productId);
    if (wishlist == null) {
      return false;
    }

    wishlistRepository.delete(wishlist);
    return true;
  }

  public Set<WishlistDTO> getWishlist(User user) {
    Set<Wishlist> wishlists = wishlistRepository.findByUser(user);
    return wishlists.stream()
        .map(
            wishlist -> {
              WishlistDTO dto = new WishlistDTO();
              dto.setId(wishlist.getId());
              dto.setUserId(wishlist.getUser().getId());
              dto.setProductId(wishlist.getProduct().getId());
              dto.setDateAdded(wishlist.getDateAdded());
              return dto;
            })
        .collect(Collectors.toSet());
  }


}
