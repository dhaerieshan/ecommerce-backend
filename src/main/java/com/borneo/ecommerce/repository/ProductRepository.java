package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByCategoryId(Long categoryId);

  @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds")
  List<Product> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds);

  List<Product> findTop5ByCategoryAndIdNot(Category category, Long ProductId);

  @Query(
      "SELECT p FROM Product p "
          + "JOIN p.category c "
          + "LEFT JOIN c.parent pc "
          + "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
          + "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
          + "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
          + "OR LOWER(pc.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
          + "ORDER BY CASE "
          + "WHEN LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN 0 "
          + "WHEN LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN 1 "
          + "WHEN LOWER(pc.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN 2 "
          + "WHEN LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN 3 "
          + "ELSE 4 END")
  List<Product> searchByNameDescriptionOrCategory(@Param("searchTerm") String searchTerm);
}
