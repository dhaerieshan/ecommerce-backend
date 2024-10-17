package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // For finding products by category
    List<Product> findByCategoryId(Long categoryId);

    // For finding suggested products based on category (optional)
    List<Product> findTop5ByCategoryAndIdNot(Category category, Long excludeProductId);
}