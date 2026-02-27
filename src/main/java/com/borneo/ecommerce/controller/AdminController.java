package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.MessageResponse;
import com.borneo.ecommerce.dto.UserResponseDTO;
import com.borneo.ecommerce.dto.UserUpdateRequest;
import com.borneo.ecommerce.exception.DuplicateResourceException;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.CartRepository;
import com.borneo.ecommerce.repository.UserRepository;
import com.borneo.ecommerce.service.CartService;
import com.borneo.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Tag(name = "10. Admin", description = "Administrative management and system control APIs")
@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Get all users",
            description = "Returns a paginated list of all registered users. Admin only.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users retrieved successfully",
                            content =
                            @Content(schema = @Schema(implementation = MessageResponse.PageResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Unauthorized\"}"))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - Admin access required",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(
                                            value = "{\"message\": \"Access denied: insufficient permissions\"}")))
            })
    @GetMapping("/users")
    public ResponseEntity<MessageResponse.PageResponse<UserResponseDTO>> getAllUsers(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0")
            int page,
            @Parameter(description = "Number of items per page", example = "5")
            @RequestParam(defaultValue = "5")
            int size) {
        List<UserResponseDTO> userDTOs =
                userService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());

        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), userDTOs.size());
        List<UserResponseDTO> pageContent =
                start >= userDTOs.size() ? List.of() : userDTOs.subList(start, end);
        Page<UserResponseDTO> userPage = new PageImpl<>(pageContent, pageable, userDTOs.size());

        return ResponseEntity.ok(new MessageResponse.PageResponse<>(userPage));
    }

    @Operation(
            summary = "Get user by ID",
            description = "Fetches a single user's details by their unique ID.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User found",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"User not found\"}")))
            })
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "User ID", example = "1") @PathVariable Long id) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return ResponseEntity.ok(convertToDTO(user));
    }

    @Operation(
            summary = "Get users by role",
            description = "Returns all users matching the specified role.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users retrieved",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid role provided",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Invalid role\"}")))
            })
    @GetMapping("/users/role/{roleName}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(
            @Parameter(description = "Role: ADMIN, USER, VENDOR", example = "USER") @PathVariable
            String roleName) {
        List<UserResponseDTO> userDTOs =
                userService.findByRoleName(roleName).stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @Operation(
            summary = "Update user by ID",
            description = "Updates the details of an existing user. Admin only.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User updated successfully",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(value = "{\"message\": \"User updated successfully\"}"))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Email already in use",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(value = "{\"message\": \"Email is already in use\"}"))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"User not found\"}")))
            })
    @PatchMapping("/update/{id}")
    public ResponseEntity<MessageResponse> userUpdate(
            @Parameter(description = "User ID", example = "1") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserUpdateRequest updateRequest) {
        User user = userRepository.findByUserId(id);
        if (user == null) throw new ResourceNotFoundException("User not found with id: " + id);

        if (updateRequest.getUsername() != null
                && !Objects.equals(user.getUsername(), updateRequest.getUsername()))
            user.setUsername(updateRequest.getUsername());
        if (updateRequest.getFirstName() != null) user.setFirstName(updateRequest.getFirstName());
        if (updateRequest.getLastName() != null) user.setLastName(updateRequest.getLastName());
        if (updateRequest.getEmail() != null
                && !Objects.equals(user.getEmail(), updateRequest.getEmail())) {
            if (userRepository.existsByEmailAndUsernameNot(updateRequest.getEmail(), user.getUsername()))
                throw new DuplicateResourceException("Email is already in use");
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty())
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        if (updateRequest.getAddress() != null) user.setAddress(updateRequest.getAddress());

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User updated successfully"));
    }

    @Operation(
            summary = "Delete user by ID",
            description = "Permanently deletes a user from the system. Admin only.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User deleted successfully",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples =
                                    @ExampleObject(value = "{\"message\": \"User deleted successfully\"}"))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content =
                            @Content(
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"User not found\"}")))
            })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<MessageResponse> deleteUserById(
            @Parameter(description = "User ID", example = "1") @PathVariable Long id) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        cartService.deleteByUser(user);
        userService.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }

    private UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAddress(user.getAddress());
        return dto;
    }
}
