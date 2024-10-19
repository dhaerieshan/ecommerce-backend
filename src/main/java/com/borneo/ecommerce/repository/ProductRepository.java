package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // For finding products by category
    List<Product> findByCategoryId(Long categoryId);

    // For finding suggested products based on category (optional)
    List<Product> findTop5ByCategoryAndIdNot(Category category, Long excludeProductId);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchByName(@Param("searchTerm") String searchTerm);
}