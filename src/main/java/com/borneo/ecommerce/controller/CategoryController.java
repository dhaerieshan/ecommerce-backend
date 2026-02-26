package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.CategoryDTO;
import com.borneo.ecommerce.dto.MessageResponse;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Tag(name = "05. Categories", description = "Product category and subcategory management APIs")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @Value("${app.upload.dir}")
    private String UPLOAD_DIR;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new category",
            description = "Creates a new product category. Admin/Vendor only.",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Category created successfully",
                            content = @Content(schema = @Schema(implementation = Category.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )

    @PreAuthorize("hasAnyAuthority('ADMIN','VENDOR')")
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Upload category image",
            description = "Uploads and assigns an image to the specified category",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
                            content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid file format")
            }
    )

    @PreAuthorize("hasAnyAuthority('ADMIN','VENDOR')")
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            CategoryDTO categoryDTO = categoryService.getCategoryById(id);
            if (categoryDTO == null) {
                throw new ResourceNotFoundException("Category not found");
            }

            String filename = StringUtils.cleanPath(file.getOriginalFilename());

            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            categoryDTO.setImagePath("/images/" + filename);

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

    @Operation(
            summary = "Upload category page banner",
            description = "Uploads and assigns a banner image to the category's page",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Banner uploaded successfully",
                            content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid file format")
            }
    )
  
    @PreAuthorize("hasAnyAuthority('ADMIN','VENDOR')")
    @PostMapping("/{id}/upload-banner")
    public ResponseEntity<?> uploadBanner(@PathVariable Long id, @RequestParam("banner") MultipartFile file) {
        try {
            CategoryDTO categoryDTO = categoryService.getCategoryById(id);
            if (categoryDTO == null) {
                throw new ResourceNotFoundException("Category not found");
            }

            String filename = StringUtils.cleanPath(file.getOriginalFilename());

            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            categoryDTO.setBannerPath("/images/" + filename);

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

    @Operation(
            summary = "Get category by ID",
            description = "Fetches a single category and its subcategories by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category found",
                            content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }


    @Operation(
            summary = "Get subcategory by ID",
            description = "Fetches a specific subcategory within a parent category",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Subcategory found",
                            content = @Content(schema = @Schema(implementation = Category.class))),
                    @ApiResponse(responseCode = "404", description = "Subcategory not found")
            }
    )
    @GetMapping("/child/{id}")
    public ResponseEntity<List<CategoryDTO>> getSubCategoryById(@PathVariable Long id) {
        List<CategoryDTO> subCategories = categoryService.getSubcategories(id);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @Operation(
            summary = "Update category",
            description = "Updates an existing category's name, description, or other details",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category updated successfully",
                            content = @Content(schema = @Schema(implementation = Category.class))),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )

    @PreAuthorize("hasAnyAuthority('ADMIN','VENDOR')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete category",
            description = "Permanently deletes a category and its associated subcategories. Admin only.",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category deleted successfully",
                            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )

    @PreAuthorize("hasAnyAuthority('ADMIN','VENDOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Category deleted successfully.", HttpStatus.OK);
    }

}