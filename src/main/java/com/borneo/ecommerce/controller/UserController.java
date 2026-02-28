package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.MessageResponse;
import com.borneo.ecommerce.dto.UserProfileResponse;
import com.borneo.ecommerce.dto.UserUpdateRequest;
import com.borneo.ecommerce.exception.DuplicateResourceException;
import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.UserRepository;
import com.borneo.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "03. Users", description = "User profile management and account-related APIs")
@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired private UserRepository userRepository;
  @Autowired private UserService userService;
  @Autowired private PasswordEncoder passwordEncoder;

  @Operation(
      summary = "Get user dashboard",
      description = "Returns a welcome message for the authenticated user.",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Dashboard retrieved",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value = "{\"message\": \"Welcome to the user Dashboard!\"}"))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Unauthorized\"}")))
      })
  @GetMapping("/dashboard")
  public String userDashboard() {
    return "Welcome to the user Dashboard!";
  }

  @Operation(
      summary = "Get current user profile",
      description = "Returns the profile of the currently authenticated user.",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Profile retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"Unauthorized\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"User not found\"}")))
      })
  @GetMapping("/me")
  public ResponseEntity<UserProfileResponse> getProfile(
      @AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByUsername(userDetails.getUsername());
    if (user == null) throw new ResourceNotFoundException("User not found");

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
      description = "Allows the authenticated user to update their own profile details.",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Profile updated successfully",
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
  @PatchMapping("/update")
  public ResponseEntity<MessageResponse> userUpdate(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody UserUpdateRequest updateRequest) {
    User user = userRepository.findByUsername(userDetails.getUsername());
    if (user == null) throw new ResourceNotFoundException("User not found");

    if (updateRequest.getFirstName() != null) user.setFirstName(updateRequest.getFirstName());
    if (updateRequest.getLastName() != null) user.setLastName(updateRequest.getLastName());

    if (updateRequest.getEmail() != null) {
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
}
