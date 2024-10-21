package com.borneo.ecommerce.service;

import com.borneo.ecommerce.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);

    List<ProductDTO> getAllProducts();

    void updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);

    List<ProductDTO> getSuggestedProducts(Long productId);

    List<ProductDTO> findByCategoryId(Long categoryId);

    ProductDTO getProductById(Long id);

    List<ProductDTO> searchProducts(String query);

    List<ProductDTO> findProductsByCategoryAndSubcategories(Long categoryId);

}