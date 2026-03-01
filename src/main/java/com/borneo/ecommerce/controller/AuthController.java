package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.LoginRequest;
import com.borneo.ecommerce.dto.MessageResponse;
import com.borneo.ecommerce.dto.SignupRequest;
import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.RoleRepository;
import com.borneo.ecommerce.repository.UserRepository;
import com.borneo.ecommerce.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "01. Authentication",
    description = "User login, registration, and JWT authentication APIs")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private JwtUtils jwtUtils;

  @Value("${admin.secret.code:defaultAdminCode}")
  private String adminSecretCode;

  @Value("${vendor.secret.code:defaultVendorCode}")
  private String vendorSecretCode;

  @Operation(
      summary = "Register a new user",
      description =
          "Creates a new USER, VENDOR, or ADMIN account. VENDOR and ADMIN require a secret code.",
      requestBody =
          @RequestBody(
              description = "User registration details",
              required = true,
              content = @Content(schema = @Schema(implementation = SignupRequest.class))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "User registered successfully",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(value = "{\"message\": \"User registered successfully\"}"))),
        @ApiResponse(
            responseCode = "400",
            description = "Username or email already in use",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value = "{\"message\": \"Error: Username is already in use\"}"))),
        @ApiResponse(
            responseCode = "403",
            description = "Invalid secret code for ADMIN/VENDOR signup",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"message\": \"Error: Invalid secret code for admin account creation.\"}")))
      })
  @PostMapping("/signup")
  public ResponseEntity<MessageResponse> registerUser(
      @org.springframework.web.bind.annotation.RequestBody SignupRequest signupRequest) {
    if (userRepository.existsByUsername(signupRequest.getUsername()))
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Username is already in use"));
    if (userRepository.existsByEmail(signupRequest.getEmail()))
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Email is already in use"));

    User user = new User();
    user.setUsername(signupRequest.getUsername());
    user.setEmail(signupRequest.getEmail());
    user.setFirstName(
        signupRequest.getFirstName() == null || signupRequest.getFirstName().isEmpty()
            ? "test"
            : signupRequest.getFirstName());
    user.setLastName(
        signupRequest.getLastName() == null || signupRequest.getLastName().isEmpty()
            ? "test"
            : signupRequest.getLastName());
    user.setAddress(
        signupRequest.getAddress() == null || signupRequest.getAddress().isEmpty()
            ? "address"
            : signupRequest.getAddress());
    user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

    Set<Role> roles = new HashSet<>();
    String signUpType =
        (signupRequest.getSignUpType() == null || signupRequest.getSignUpType().isEmpty())
            ? "USER"
            : signupRequest.getSignUpType();

    switch (signUpType.toUpperCase()) {
      case "ADMIN":
        if (signupRequest.getSecretCode() == null
            || !signupRequest.getSecretCode().equals(adminSecretCode))
          return ResponseEntity.status(HttpStatus.FORBIDDEN)
              .body(new MessageResponse("Error: Invalid secret code for admin account creation."));
        roles.add(roleRepository.findByName("ADMIN"));
        break;
      case "VENDOR":
        if (signupRequest.getSecretCode() == null
            || !signupRequest.getSecretCode().equals(vendorSecretCode))
          return ResponseEntity.status(HttpStatus.FORBIDDEN)
              .body(new MessageResponse("Error: Invalid secret code for vendor account creation."));
        roles.add(roleRepository.findByName("VENDOR"));
        break;
      default:
        roles.add(roleRepository.findByName("USER"));
        break;
    }

    user.setRoles(roles);
    userRepository.save(user);
    return ResponseEntity.ok(new MessageResponse("User registered successfully"));
  }

  @Operation(
      summary = "Login user",
      description =
          "Authenticates user credentials and returns a JWT token. Use this token in the Authorize button above.",
      requestBody =
          @RequestBody(
              description = "Login credentials",
              required = true,
              content = @Content(schema = @Schema(implementation = LoginRequest.class))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Login successful - returns JWT token",
            content =
                @Content(
                    schema =
                        @Schema(
                            example = "{\"token\": \"eyJhbGci...\", \"username\": \"john_doe\"}"))),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid username or password",
            content =
                @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples =
                        @ExampleObject(value = "{\"message\": \"Invalid username or password\"}")))
      })
  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(
      @org.springframework.web.bind.annotation.RequestBody LoginRequest loginRequest) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String name = authentication.getName();
    String role = jwtUtils.getUserRoles(name);
    String jwt = jwtUtils.generateJwtToken(name, role);

    Map<String, Object> response = new HashMap<>();
    response.put("token", jwt);
    response.put("username", authentication.getName());
    return ResponseEntity.ok(response);
  }
}
