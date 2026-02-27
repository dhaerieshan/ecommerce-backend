package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.MessageResponse;
import com.borneo.ecommerce.dto.ProductDTO;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.repository.CategoryRepository;
import com.borneo.ecommerce.repository.ProductRepository;
import com.borneo.ecommerce.service.ProductMapper;
import com.borneo.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "06. Products", description = "Product creation, update, search, and listing APIs")
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
    private CategoryRepository categoryRepository;

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product listing. Admin only.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product created successfully",
                            content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid product data",
                            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
            })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(201).body(createdProduct);
    }

    @Operation(
            summary = "Get all products",
            description =
                    "Returns a paginated list of all available products. Filter by categoryId optionally.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Products retrieved successfully",
                            content =
                            @Content(schema = @Schema(implementation = MessageResponse.PageResponse.class)))
            })
    @GetMapping
    public ResponseEntity<MessageResponse.PageResponse<ProductDTO>> getAllProducts(
            @Parameter(description = "Filter by category ID") @RequestParam(required = false)
            Long categoryId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0")
            int page,
            @Parameter(description = "Number of items per page", example = "5")
            @RequestParam(defaultValue = "5")
            int size,
            @Parameter(description = "Sort by field", example = "id") @RequestParam(defaultValue = "id")
            String sortBy,
            @Parameter(description = "Sort direction: asc or desc", example = "asc")
            @RequestParam(defaultValue = "asc")
            String sortDir) {
        Sort sort =
                sortDir.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        List<ProductDTO> products =
                categoryId != null
                        ? productService.findByCategoryId(categoryId)
                        : productService.getAllProducts();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), products.size());
        List<ProductDTO> pageContent =
                start >= products.size() ? List.of() : products.subList(start, end);
        Page<ProductDTO> productPage = new PageImpl<>(pageContent, pageable, products.size());

        return ResponseEntity.ok(new MessageResponse.PageResponse<>(productPage));
    }

    @Operation(
            summary = "Get featured products",
            description = "Returns one random product from each category (up to 4 products)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Featured products returned",
                            content =
                            @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class))))
            })
    @GetMapping("/featured")
    public List<ProductDTO> featured() {
        List<ProductDTO> allProducts = productService.getAllProducts();
        Map<Long, List<ProductDTO>> categoryMap =
                allProducts.stream().collect(Collectors.groupingBy(ProductDTO::getCategoryId));

        List<ProductDTO> featuredProducts = new ArrayList<>();
        Random random = new Random();

        for (List<ProductDTO> products : categoryMap.values()) {
            if (!products.isEmpty()) {
                featuredProducts.add(products.get(random.nextInt(products.size())));
            }
            if (featuredProducts.size() >= 4) break;
        }
        return featuredProducts;
    }

    @Operation(
            summary = "Search products",
            description = "Search for products using a keyword query",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Search results returned",
                            content =
                            @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class))))
            })
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(
            @Parameter(description = "Search keyword", example = "Samsung") @RequestParam String query) {
        List<Product> products = productRepository.searchByNameDescriptionOrCategory(query);
        List<ProductDTO> productDTOs =
                products.stream().map(ProductMapper.INSTANCE::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @Operation(
            summary = "Get search suggestions",
            description = "Returns product suggestions based on a product ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Suggestions returned",
                            content =
                            @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    @PostMapping("/suggestions")
    public ResponseEntity<List<ProductDTO>> getSuggestions(@RequestBody Map<String, Long> payload) {
        Long productId = payload.get("productId");
        if (productId == null) {
            return ResponseEntity.badRequest().body(null);
        }
        List<ProductDTO> suggestions = productService.getSuggestedProducts(productId);
        return ResponseEntity.ok(suggestions);
    }

    @Operation(
            summary = "Get product by ID",
            description = "Fetches the full details of a single product by its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product found",
                            content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long id) {
        try {
            ProductDTO dto = productService.getProductById(id);
            return ResponseEntity.ok(dto);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @Operation(
            summary = "Update product",
            description = "Partially updates an existing product's information. Vendor only.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product updated successfully",
                            content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
            })
    @PreAuthorize("hasAuthority('VENDOR')")
    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = optionalProduct.get();
        updates.forEach(
                (key, value) -> {
                    if ("categoryId".equals(key)) {
                        Long categoryId = Long.valueOf((Integer) value);
                        Category category =
                                categoryRepository
                                        .findById(categoryId)
                                        .orElseThrow(
                                                () ->
                                                        new ResourceNotFoundException(
                                                                "Category not found for this id: " + categoryId));
                        product.setCategory(category);
                    } else {
                        Field field = ReflectionUtils.findField(Product.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, product, value);
                        }
                    }
        });

        productRepository.save(product);
        return ResponseEntity.ok(product);
    }

    @Operation(
            summary = "Upload product image",
            description = "Uploads an image for a product listing. Vendor only.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Image uploaded successfully",
                            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid file or format")
            })
    @PreAuthorize("hasAuthority('VENDOR')")
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long id,
            @RequestParam("image") MultipartFile file) {
        try {
            ProductDTO productDTO = productService.getProductById(id);
            if (productDTO == null) {
                throw new ResourceNotFoundException("Product not found");
            }

            String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            productDTO.setImagePath("/images/" + filename);
            productService.updateProduct(id, productDTO);

            return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(new MessageResponse("Product not found"));
        } catch (IOException e) {
            log.error("Product image upload failed: ", e);
            return ResponseEntity.status(500)
                    .body(new MessageResponse("Could not upload image: " + e.getMessage()));
        }
    }

    @Operation(
            summary = "Delete product",
            description = "Permanently removes a product from the system. Vendor only.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product deleted successfully",
                            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            })
    @PreAuthorize("hasAuthority('VENDOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok(new MessageResponse("Product deleted successfully"));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(new MessageResponse("Product not found"));
        }
    }
}
