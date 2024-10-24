package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.CategoryDTO;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.repository.CategoryRepository;
import com.borneo.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    @Value("${app.upload.dir}")
    private String UPLOAD_DIR;

    // Get All Categories
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Create a New Category
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    // Update a Category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    // Delete a Category
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Category deleted successfully.", HttpStatus.OK);
    }

    // Get Category By Id
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @GetMapping("/child/{id}")
    public ResponseEntity<List<CategoryDTO>> getSubCategoryById(@PathVariable Long id) {
        List<CategoryDTO> subCategories = categoryService.getSubcategories(id);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            CategoryDTO categoryDTO = categoryService.getCategoryById(id);
            if (categoryDTO == null) {
                throw new ResourceNotFoundException("Category not found");
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

            // Update the category's image path
            categoryDTO.setImagePath("/images/" + filename);

            // Now save the updated category
            categoryService.updateCategory(id, categoryDTO);

            return ResponseEntity.ok("Image uploaded successfully");

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body("Category not found");
        } catch (IOException e) {
            e.printStackTrace();
            String errorMessage = "Could not upload the image: " + e.getMessage();
            return ResponseEntity.status(500).body(errorMessage);
        }
    }

    @PostMapping("/{id}/upload-banner")
    public ResponseEntity<?> uploadBanner(@PathVariable Long id, @RequestParam("banner") MultipartFile file) {
        try {
            CategoryDTO categoryDTO = categoryService.getCategoryById(id);
            if (categoryDTO == null) {
                throw new ResourceNotFoundException("Category not found");
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

            // Update the category's image path
            categoryDTO.setBannerPath("/images/" + filename);

            // Now save the updated category
            categoryService.updateCategory(id, categoryDTO);

            return ResponseEntity.ok("Image uploaded successfully");

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body("Category not found");
        } catch (IOException e) {
            e.printStackTrace();
            String errorMessage = "Could not upload the image: " + e.getMessage();
            return ResponseEntity.status(500).body(errorMessage);
        }
    }

}