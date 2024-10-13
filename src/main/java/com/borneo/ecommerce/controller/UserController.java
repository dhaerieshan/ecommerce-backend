package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.UserProfileResponse;
import com.borneo.ecommerce.dto.UserUpdateRequest;
import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String adminDashboard(){
        return "Welcome to the user Dashboard!";
    }
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User Not found with Username: " + username);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));

        return ResponseEntity.ok(response);
    }
    @GetMapping("/update")
    public ResponseEntity<?> UserUpdate(@RequestBody UserUpdateRequest updateRequest, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (updateRequest.getFirstName() != null) {
            user.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            user.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getEmail() != null) {
            // Check if email is already in use by another user
            if (userRepository.existsByEmailAndUsernameNot(updateRequest.getEmail(), username)) {
                return ResponseEntity.badRequest().body("Email is already in use by another account.");
            }
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        userRepository.save(user);
        return ResponseEntity.ok("password changed successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);

        // Map user entity to DTO if necessary
        UserProfileResponse profile = new UserProfileResponse();
        profile.setUsername(user.getUsername());
        profile.setEmail(user.getEmail());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        // ... other fields

        return ResponseEntity.ok(profile);
    }
}