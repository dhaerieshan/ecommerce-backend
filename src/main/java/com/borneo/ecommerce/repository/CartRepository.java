package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.Cart;
import com.borneo.ecommerce.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @EntityGraph(attributePaths = {"items", "items.product"})
        // Eagerly fetch items and products
    Optional<Cart> findByUser(User user);

    boolean existsByUser(User user);


}