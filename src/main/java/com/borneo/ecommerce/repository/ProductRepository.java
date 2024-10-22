package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Fetch products by a single category ID
    List<Product> findByCategoryId(Long categoryId);

    // Fetch products by multiple category IDs (including subcategories)
    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds")
    List<Product> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds);

    List<Product> findTop5ByCategoryAndIdNot(Category category, Long excludeProductId);

    @Query("SELECT p FROM Product p " +
            "JOIN p.category c " + // Join with category to allow searching in category name
            "LEFT JOIN c.parent pc " +  // Join parent category if it exists
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(pc.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " + // Search in parent category name
            "ORDER BY CASE " +
            "WHEN LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN 0 " + // Prioritize name matches
            "WHEN LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN 1 " + // Then category matches
            "WHEN LOWER(pc.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN 2 " + // Then parent category matches
            "WHEN LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN 3 " + // Then description matches
            "ELSE 4 END")
    List<Product> searchByNameDescriptionOrCategory(@Param("searchTerm") String searchTerm);
}