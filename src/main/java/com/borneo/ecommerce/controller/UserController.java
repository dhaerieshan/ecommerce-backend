

package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.UserProfileResponse;
import com.borneo.ecommerce.dto.UserUpdateRequest;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.UserRepository;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Tag(name = "03. Users", description = "User profile management and account-related APIs")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Get user dashboard",
            description = "Returns dashboard summary for the authenticated user including orders and wishlist",
            tags = {"Profile & Dashboard"},
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dashboard data retrieved",
                            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/dashboard")
    public String userDashboard() {
        return "Welcome to the user Dashboard!";
    }

    @Operation(
            summary = "Get current user profile",
            description = "Returns the profile of the currently authenticated user",
            tags = {"Profile & Dashboard"},
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "User is not authenticated"));
        }

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "User not found"));
        }

        UserProfileResponse profile = new UserProfileResponse();
        profile.setUsername(user.getUsername());
        profile.setEmail(user.getEmail());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        profile.setAddress(user.getAddress());
        return ResponseEntity.ok(profile);
    }

    @Operation(
            summary = "Update current user profile",
            description = "Allows the authenticated user to update their own profile details",
            tags = {"Profile & Dashboard"},
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                            content = @Content(schema = @Schema(implementation = UserUpdateRequest.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/update")
    public ResponseEntity<?> userUpdate(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserUpdateRequest updateRequest
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "User is not authenticated"));
        }

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        if (updateRequest.getFirstName() != null) {
            user.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            user.setLastName(updateRequest.getLastName());
        }

        if (updateRequest.getEmail() != null) {
            if (userRepository.existsByEmailAndUsernameNot(updateRequest.getEmail(), username)) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Email is already in use."));
            }
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        if (updateRequest.getAddress() != null) {
            user.setAddress(updateRequest.getAddress());
        }


        userRepository.save(user);
        return ResponseEntity.ok(Collections.singletonMap("message", "User updated successfully"));
    }
}


