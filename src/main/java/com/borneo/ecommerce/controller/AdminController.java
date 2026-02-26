package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.MessageResponse;
import com.borneo.ecommerce.dto.UserResponseDTO;
import com.borneo.ecommerce.dto.UserUpdateRequest;
import com.borneo.ecommerce.exception.ErrorResponse;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.CartRepository;
import com.borneo.ecommerce.repository.UserRepository;
import com.borneo.ecommerce.service.CartService;
import com.borneo.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserResponseDTO> userDTOs = users.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Fetches a single user's details by their unique ID",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserResponseDTO dto = convertToDTO(user);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Get users by role",
            description = "Returns all users that match the specified role (ADMIN, USER, VENDOR)",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid role provided")
            }
    )

    @GetMapping("/users/role/{roleName}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable String roleName) {
        List<User> users = userService.findByRoleName(roleName);
        List<UserResponseDTO> userDTOs = users.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @Operation(
            summary = "Update user by ID",
            description = "Updates the details of an existing user",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> userUpdate(
            @PathVariable Long id,  //   Extract id from URL path
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserUpdateRequest updateRequest
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "User is not authenticated"));
        }


        User user = userRepository.findByUserId(id);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + id);
        }
        if (updateRequest.getUsername() != null && !Objects.equals(user.getUsername(), updateRequest.getUsername())) {
            user.setUsername(updateRequest.getUsername());
        }

        if (updateRequest.getFirstName() != null && !Objects.equals(user.getFirstName(), updateRequest.getFirstName())) {
            user.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null && !Objects.equals(user.getLastName(), updateRequest.getLastName())) {
            user.setLastName(updateRequest.getLastName());
        }


        if (updateRequest.getEmail() != null && !Objects.equals(user.getEmail(), updateRequest.getEmail())) {
            if (userRepository.existsByEmailAndUsernameNot(updateRequest.getEmail(), user.getUsername())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Email is already in use."));
            }
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        if (updateRequest.getAddress() != null && !Objects.equals(user.getAddress(), updateRequest.getAddress())) {
            user.setAddress(updateRequest.getAddress());
        }


        userRepository.save(user);
        return ResponseEntity.ok(Collections.singletonMap("message", "User updated successfully"));
    }

    @Operation(
            summary = "Delete user by ID",
            description = "Permanently deletes a user from the system. Admin only.",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User deleted successfully",
                            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        cartService.deleteByUser(user);
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }


    private UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())); // Convert roles properly
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());

        dto.setAddress(user.getAddress());
        return dto;
    }
}