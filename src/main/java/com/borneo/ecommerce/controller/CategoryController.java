package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.CategoryDTO;
import com.borneo.ecommerce.dto.MessageResponse;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = "05. Categories", description = "Product category and subcategory management APIs")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @Value("${app.upload.dir}")
  private String UPLOAD_DIR;

  @Operation(
      summary = "Create a new category",
      description = "Creates a new product category. Admin/Vendor only.",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Category created successfully",
            content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Invalid input data\"}"))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Unauthorized\"}"))),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Admin or Vendor access required",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value = "{\"message\": \"Access denied: insufficient permissions\"}")))
      })
  @PreAuthorize("hasAnyAuthority('ADMIN','VENDOR')")
  @PostMapping
  public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
    CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
    return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
  }

  @Operation(
      summary = "Get all categories",
      description = "Returns a paginated list of all product categories",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Categories retrieved successfully",
            content =
                @Content(schema = @Schema(implementation = MessageResponse.PageResponse.class)))
      })
  @GetMapping
  public ResponseEntity<MessageResponse.PageResponse<CategoryDTO>> getAllCategories(
      @Parameter(description = "Page number (0-indexed)", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Number of items per page", example = "5")
          @RequestParam(defaultValue = "5")
          int size) {

    List<CategoryDTO> allCategories = categoryService.getAllCategories();
    PageRequest pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), allCategories.size());
    List<CategoryDTO> pageContent =
        start >= allCategories.size() ? List.of() : allCategories.subList(start, end);
    Page<CategoryDTO> categoryPage = new PageImpl<>(pageContent, pageable, allCategories.size());

    return ResponseEntity.ok(new MessageResponse.PageResponse<>(categoryPage));
  }

  @Operation(
      summary = "Get category by ID",
      description = "Fetches a single category and its subcategories by ID",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Category found",
            content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Category not found",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Category not found\"}")))
      })
  @GetMapping("/{id}")
  public ResponseEntity<CategoryDTO> getCategoryById(
      @Parameter(description = "Category ID", example = "1") @PathVariable Long id) {
    CategoryDTO categoryDTO = categoryService.getCategoryById(id);
    return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
  }

  @Operation(
      summary = "Get subcategories by parent ID",
      description = "Fetches all subcategories under a parent category",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Subcategories found",
            content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Category not found",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Category not found\"}")))
      })
  @GetMapping("/child/{id}")
  public ResponseEntity<List<CategoryDTO>> getSubCategoryById(
      @Parameter(description = "Parent Category ID", example = "1") @PathVariable Long id) {
    List<CategoryDTO> subCategories = categoryService.getSubcategories(id);
    return new ResponseEntity<>(subCategories, HttpStatus.OK);
  }

  @Operation(
      summary = "Update category",
      description = "Updates an existing category's details. Admin/Vendor only.",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Category updated successfully",
            content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Unauthorized\"}"))),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Admin or Vendor access required",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value = "{\"message\": \"Access denied: insufficient permissions\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "Category not found",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Category not found\"}")))
      })
  @PreAuthorize("hasAnyAuthority('ADMIN','VENDOR')")
  @PutMapping("/{id}")
  public ResponseEntity<CategoryDTO> updateCategory(
      @Parameter(description = "Category ID", example = "1") @PathVariable Long id,
      @RequestBody CategoryDTO categoryDTO) {
    CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
    return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
  }

  @Operation(
      summary = "Upload category image",
      description = "Uploads and assigns an image to the specified category",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Image uploaded successfully",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(value = "{\"message\": \"Image uploaded successfully\"}"))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Unauthorized\"}"))),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Admin or Vendor access required",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value = "{\"message\": \"Access denied: insufficient permissions\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "Category not found",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Category not found\"}"))),
        @ApiResponse(
            responseCode = "500",
            description = "Image upload failed",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"message\": \"Something went wrong. Please try again later.\"}")))
      })
  @PreAuthorize("hasAnyAuthority('ADMIN','VENDOR')")
  @PostMapping("/{id}/upload-image")
  public ResponseEntity<?> uploadImage(
      @Parameter(description = "Category ID", example = "1") @PathVariable Long id,
      @RequestParam("image") MultipartFile file) {
    try {
      CategoryDTO categoryDTO = categoryService.getCategoryById(id);
      if (categoryDTO == null) throw new ResourceNotFoundException("Category not found");

      String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
      Path uploadPath = Paths.get(UPLOAD_DIR);
      if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
      Files.copy(
          file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

      categoryDTO.setImagePath("/images/" + filename);
      categoryService.updateCategory(id, categoryDTO);
      return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));

    } catch (ResourceNotFoundException ex) {
      return ResponseEntity.status(404).body(new MessageResponse("Category not found"));
    } catch (IOException e) {
      log.error("Category image upload failed: ", e);
      return ResponseEntity.status(500)
          .body(new MessageResponse("Could not upload image: " + e.getMessage()));
    }
  }

  @Operation(
      summary = "Upload category page banner",
      description = "Uploads and assigns a banner image to the category's page",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Banner uploaded successfully",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(value = "{\"message\": \"Banner uploaded successfully\"}"))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Unauthorized\"}"))),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Admin or Vendor access required",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value = "{\"message\": \"Access denied: insufficient permissions\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "Category not found",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Category not found\"}"))),
        @ApiResponse(
            responseCode = "500",
            description = "Banner upload failed",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"message\": \"Something went wrong. Please try again later.\"}")))
      })
  @PreAuthorize("hasAnyAuthority('ADMIN','VENDOR')")
  @PostMapping("/{id}/upload-banner")
  public ResponseEntity<?> uploadBanner(
      @Parameter(description = "Category ID", example = "1") @PathVariable Long id,
      @RequestParam("banner") MultipartFile file) {
    try {
      CategoryDTO categoryDTO = categoryService.getCategoryById(id);
      if (categoryDTO == null) throw new ResourceNotFoundException("Category not found");

      String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
      Path uploadPath = Paths.get(UPLOAD_DIR);
      if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
      Files.copy(
          file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

      categoryDTO.setBannerPath("/images/" + filename);
      categoryService.updateCategory(id, categoryDTO);
      return ResponseEntity.ok(new MessageResponse("Banner uploaded successfully"));

    } catch (ResourceNotFoundException ex) {
      return ResponseEntity.status(404).body(new MessageResponse("Category not found"));
    } catch (IOException e) {
      log.error("Category banner upload failed: ", e);
      return ResponseEntity.status(500)
          .body(new MessageResponse("Could not upload banner: " + e.getMessage()));
    }
  }

  @Operation(
      summary = "Delete category",
      description = "Permanently deletes a category and its subcategories. Admin/Vendor only.",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Category deleted successfully",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value = "{\"message\": \"Category deleted successfully\"}"))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Unauthorized\"}"))),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Admin or Vendor access required",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value = "{\"message\": \"Access denied: insufficient permissions\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "Category not found",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Category not found\"}")))
      })
  @PreAuthorize("hasAnyAuthority('ADMIN','VENDOR')")
  @DeleteMapping("/{id}")
  public ResponseEntity<MessageResponse> deleteCategory(
      @Parameter(description = "Category ID", example = "1") @PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.ok(new MessageResponse("Category deleted successfully"));
  }
}
