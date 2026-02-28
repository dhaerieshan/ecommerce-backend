package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.Cart;
import com.borneo.ecommerce.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

  @EntityGraph(attributePaths = {"items", "items.product"})
  Optional<Cart> findByUser(User user);

  boolean existsByUser(User user);
}
