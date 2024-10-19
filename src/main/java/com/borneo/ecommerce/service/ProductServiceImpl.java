package com.borneo.ecommerce.service;

import com.borneo.ecommerce.dto.ProductDTO;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.repository.CategoryRepository;
import com.borneo.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;  // Ensure @Autowired is present

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        // Map fields from DTO to entity
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());

        // Set category
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id: " + productDTO.getCategoryId()));
        product.setCategory(category);

        // Save product
        Product savedProduct = productRepository.save(product);

        // Convert to DTO and return
        return new ProductDTO(savedProduct);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));

        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setStock(productDTO.getStock());

        // Fetch and set the category
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id: " + productDTO.getCategoryId()));
        existingProduct.setCategory(category);

        // Save the updated product
        Product updatedProduct = productRepository.save(existingProduct);

        // Convert to ProductDTO before returning
        return new ProductDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));
        productRepository.delete(product);
    }

    @Override
    public List<ProductDTO> getSuggestedProducts(Long productId) {
        // Fetch the current product to get its category
        Product currentProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = currentProduct.getCategory();

        // Fetch top 5 products from the same category, excluding the current product
        return productRepository.findTop5ByCategoryAndIdNot(category, productId).stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return new ProductDTO(product);
    }

    @Override
    public List<ProductDTO> searchProducts(String query) {
        List<Product> products = productRepository.searchByName(query);
        return products.stream()
                .map(ProductMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }
}