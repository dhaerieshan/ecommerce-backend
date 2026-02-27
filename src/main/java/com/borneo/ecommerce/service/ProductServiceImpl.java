package com.borneo.ecommerce.service;

import com.borneo.ecommerce.dto.ProductDTO;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.repository.CategoryRepository;
import com.borneo.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImagePath(productDTO.getImagePath());

        Category category =
                categoryRepository
                        .findById(productDTO.getCategoryId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Category not found for this id: " + productDTO.getCategoryId()));
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return new ProductDTO(savedProduct);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    @Override
    public void updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct =
                productRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Product not found for this id :: " + id));

        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setStock(productDTO.getStock());
        existingProduct.setImagePath(productDTO.getImagePath());

        Category category =
                categoryRepository
                        .findById(productDTO.getCategoryId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Category not found for this id: " + productDTO.getCategoryId()));
        existingProduct.setCategory(category);

        productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Product not found for this id :: " + id));
        productRepository.delete(product);
    }

    @Override
    public List<ProductDTO> getSuggestedProducts(Long productId) {
        Product currentProduct =
                productRepository
                        .findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = currentProduct.getCategory();
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

    public void reduceStock(Long productId, int quantity) {
        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        } else {
            throw new RuntimeException("Insufficient stock for product: " + productId);
        }
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return new ProductDTO(product);
    }

    @Override
    public List<ProductDTO> searchProducts(String query) {
        List<Product> products = productRepository.searchByNameDescriptionOrCategory(query);
        return products.stream().map(ProductMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findProductsByCategoryAndSubcategories(Long categoryId) {

        Category category =
                categoryRepository
                        .findById(categoryId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException("Category not found for this id: " + categoryId));

        List<Long> categoryIds = gatherCategoryAndSubcategoryIds(category);

        List<Product> products = productRepository.findByCategoryIds(categoryIds);
        return products.stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    private List<Long> gatherCategoryAndSubcategoryIds(Category category) {
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(category.getId());

        for (Category child : category.getSubcategories()) {
            categoryIds.addAll(gatherCategoryAndSubcategoryIds(child));
        }
        return categoryIds;
    }
}
