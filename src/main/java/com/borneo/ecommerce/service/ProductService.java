package com.borneo.ecommerce.service;

import com.borneo.ecommerce.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product createProduct(Product product);

    List<Product> getAllProducts();

    Optional<Product> getProductById(long id);

    Product updateProduct(Long id, Product productDetails);

    void deleteProduct(Long id);

    List<Product> getSuggestedProducts(Long productId); // Updated method

    List<Product> findByCategoryId(Long categoryId);
}