package com.borneo.ecommerce.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.borneo.ecommerce.dto.LoginRequest;
import com.borneo.ecommerce.dto.SignupRequest;
import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.RoleRepository;
import com.borneo.ecommerce.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("AuthController Integration Tests")
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    roleRepository.deleteAll();
    Role userRole = new Role();
    userRole.setName("USER");
    userRole = roleRepository.save(userRole);

    // Create a default user for login tests
    User user = new User();
    user.setUsername("testuser");
    user.setEmail("testuser@example.com");
    user.setPassword(passwordEncoder.encode("password123"));
    user.setFirstName("Test");
    user.setLastName("User");
    user.setAddress("123 Street");
    user.setRoles(java.util.Set.of(userRole));
    userRepository.save(user);
  }

  @Test
  @DisplayName("Should register user successfully")
  void testSignupUser() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("newuser1234");
    signupRequest.setEmail("newuser@example.com");
    signupRequest.setPassword("password123");
    signupRequest.setFirstName("New");
    signupRequest.setLastName("User");
    signupRequest.setAddress("123 Street");

    mockMvc
        .perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("User registered successfully"));

    // Verify user was saved
    User savedUser = userRepository.findByUsername("newuser1234");
    assertNotNull(savedUser);
  }

  @Test
  @DisplayName("Should not register user with duplicate email")
  void testSignupDuplicateEmail() throws Exception {
    User existingUser = new User();
    existingUser.setUsername("existinguser");
    existingUser.setEmail("duplicate@example.com");
    existingUser.setPassword(passwordEncoder.encode("password123"));
    existingUser.setFirstName("Existing");
    existingUser.setLastName("User");
    existingUser.setAddress("123 Street");
    userRepository.save(existingUser);

    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("anotheruser");
    signupRequest.setEmail("duplicate@example.com");
    signupRequest.setPassword("password123");

    mockMvc
        .perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @DisplayName("Should login user successfully")
  void testLoginUser() throws Exception {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("testuser");
    loginRequest.setPassword("password123");

    mockMvc
        .perform(
            post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.username").value("testuser"));
  }

  @Test
  @DisplayName("Should fail login with wrong password")
  void testLoginWrongPassword() throws Exception {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("testuser");
    loginRequest.setPassword("wrongpassword");

    mockMvc
        .perform(
            post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Should fail login with non-existent user")
  void testLoginNonExistentUser() throws Exception {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("nonexistent");
    loginRequest.setPassword("password123");

    mockMvc
        .perform(
            post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Should validate signup request fields")
  void testSignupMissingFields() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("newuser");
    // Missing email, password, etc.

    mockMvc
        .perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().is4xxClientError());
  }
}
