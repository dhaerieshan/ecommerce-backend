package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    boolean existsByUserAndProductId(User user, Long productId);

    Wishlist findByUserAndProductId(User user, Long productId);

    Set<Wishlist> findByUser(User user);
}
