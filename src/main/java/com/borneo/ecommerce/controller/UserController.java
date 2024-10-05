package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.UserUpdateRequest;
import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsernameIgnoreCase(username);

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
    public ResponseEntity<?> UserUpdate(@RequestBody UserUpdateRequest userUpdateRequest, Authentication authentication){
        String username = authentication.getName();

        if (!username.equals(userUpdateRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("YOU can change your password only lol");
        }
        User user = userRepository.findByUsernameIgnoreCase(username);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userUpdateRequest.getUsername(),userUpdateRequest.getOldPassword())
            );
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password is incorrect.");
        }
        String encodedNewPassword = passwordEncoder.encode(userUpdateRequest.getNewPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
        return ResponseEntity.ok("password changed successfully");
    }
}