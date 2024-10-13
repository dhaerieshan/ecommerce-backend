package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.LoginRequest;
import com.borneo.ecommerce.dto.SignupRequest;
import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.RoleRepository;
import com.borneo.ecommerce.repository.UserRepository;
import com.borneo.ecommerce.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${admin.secret.code:defaultAdminCode}")
    private String adminSecretCode;

    @Value("${vendor.secret.code:defaultVendorCode}")
    private String vendorSecretCode;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequest signupRequest) {
        System.out.println("adminSecretCode: " + adminSecretCode);
        System.out.println("vendorSecretCode: " + vendorSecretCode);

        if (userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body("Error: Username is already in use");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body("Error: Email is already in use");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        String signUpType = signupRequest.getSignUpType();
        if (signUpType == null || signUpType.isEmpty()) {
            signUpType = "USER";
        }

        switch (signUpType.toUpperCase()) {
            case "ADMIN":
                if (signupRequest.getSecretCode() == null || !signupRequest.getSecretCode().equals(adminSecretCode)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: Invalid secret code for admin account creation.");
                }
                Role adminRole = roleRepository.findByName("ADMIN");
                roles.add(adminRole);
                break;
            case "VENDOR":
                if (signupRequest.getSecretCode() == null || !signupRequest.getSecretCode().equals(vendorSecretCode)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: Invalid secret code for vendor account creation.");
                }
                Role vendorRole = roleRepository.findByName("VENDOR");
                roles.add(vendorRole);
                break;
            default:
                // Default to USER role
                Role userRole = roleRepository.findByName("USER");
                roles.add(userRole);
                break;
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String name = authentication.getName();
        String role = jwtUtils.getUserRoles(name);
        String jwt = jwtUtils.generateJwtToken(name, role);

        Map<String, Object> response = new HashMap<>();
        response.put("token",jwt);
        response.put("username", authentication.getName());
        return ResponseEntity.ok(response);

    }
}
