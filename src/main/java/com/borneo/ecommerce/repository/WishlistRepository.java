package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.model.Wishlist;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
  boolean existsByUserAndProductId(User user, Long productId);

  Wishlist findByUserAndProductId(User user, Long productId);

  Set<Wishlist> findByUser(User user);
}
