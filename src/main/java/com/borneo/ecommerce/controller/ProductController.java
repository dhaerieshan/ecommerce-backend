package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.ProductDTO;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Value("${app.upload.dir}")
    private String UPLOAD_DIR;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    @GetMapping
    public List<ProductDTO> getAllProducts(@RequestParam(required = false) Long categoryId) {
        List<Product> products;
        if (categoryId != null) {
            products = productService.findByCategoryId(categoryId);
        } else {
            products = productService.getAllProducts();
        }
        return products.stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Map Product to ProductDTO
        ProductDTO dto = new ProductDTO(product);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") Long productId,
                                                 @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(productId, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/suggestions")
    public ResponseEntity<List<ProductDTO>> getSuggestions(@RequestBody Map<String, Long> payload) {
        Long productId = payload.get("productId");
        if (productId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Product> suggestions = productService.getSuggestedProducts(productId);
        List<ProductDTO> suggestionDTOs = suggestions.stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(suggestionDTOs);
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        // Retrieve the product
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Create the filename
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        // Define the upload path
        Path uploadPath = Paths.get(UPLOAD_DIR);

        try {
            // Ensure the directory exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the new image file
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Update the product's image path
            product.setImagePath("/images/" + filename);
            productService.createProduct(product);

            return ResponseEntity.ok("Image uploaded successfully");

        } catch (IOException e) {
            e.printStackTrace();
            String errorMessage = "Could not upload the image: " + e.getMessage();
            return ResponseEntity.status(500).body(errorMessage);
        }
    }
}