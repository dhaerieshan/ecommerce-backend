package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.ProductDTO;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.repository.CategoryRepository;
import com.borneo.ecommerce.repository.ProductRepository;
import com.borneo.ecommerce.repository.UserRepository;
import com.borneo.ecommerce.service.ProductMapper;
import com.borneo.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Value("${app.upload.dir}")
    private String UPLOAD_DIR;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Create Product
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // Get All Products
    @GetMapping
    public List<ProductDTO> getAllProducts(@RequestParam(required = false) Long categoryId) {
        if (categoryId != null) {
            return productService.findByCategoryId(categoryId);
        } else {
            return productService.getAllProducts();
        }
    }

    // Get Product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        try {
            ProductDTO dto = productService.getProductById(id);
            return ResponseEntity.ok(dto);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Update Product
    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = optionalProduct.get();

        updates.forEach((key, value) -> {
            if ("categoryId".equals(key)) {
                // Handle categoryId separately
                Long categoryId = Long.valueOf((Integer) value); // Convert value to Long if it's in Integer format
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id: " + categoryId));
                product.setCategory(category); // Set the fetched Category object
            } else {
                // Use ReflectionUtils for other fields
                Field field = ReflectionUtils.findField(Product.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, product, value);
                }
            }
        });

        // Save the updated product
        productRepository.save(product);
        return ResponseEntity.ok(product);
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Get Suggested Products
    @PostMapping("/suggestions")
    public ResponseEntity<List<ProductDTO>> getSuggestions(@RequestBody Map<String, Long> payload) {
        Long productId = payload.get("productId");
        if (productId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<ProductDTO> suggestions = productService.getSuggestedProducts(productId);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query) {
        List<Product> products = productRepository.searchByNameOrDescription(query);
        List<ProductDTO> productDTOs = products.stream()
                .map(ProductMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    // Upload Image
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        // Retrieve the product
        try {
            ProductDTO productDTO = productService.getProductById(id);
            if (productDTO == null) {
                throw new ResourceNotFoundException("Product not found");
            }

            // Create the filename
            String filename = StringUtils.cleanPath(file.getOriginalFilename());

            // Define the upload path
            Path uploadPath = Paths.get(UPLOAD_DIR);

            // Ensure the directory exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the new image file
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Update the product's image path
            productDTO.setImagePath("/images/" + filename);
            productService.updateProduct(id, productDTO);

            return ResponseEntity.ok("Image uploaded successfully");

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body("Product not found");
        } catch (IOException e) {
            e.printStackTrace();
            String errorMessage = "Could not upload the image: " + e.getMessage();
            return ResponseEntity.status(500).body(errorMessage);
        }
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategoryId(@PathVariable Long id) {
        try {
            // Fetch products in the category and all its subcategories
            List<ProductDTO> products = productService.findProductsByCategoryAndSubcategories(id);
            return ResponseEntity.ok(products);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(null);
        }
    }
}