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

import java.time.LocalDate;
import java.util.*;

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

    private String generateRandomRationCardNumber() {
        Random random = new Random();
        int part1 = 1000 + random.nextInt(9000); // 4-digit number
        int part2 = 1000 + random.nextInt(9000); // 4-digit number
        int part3 = 1000 + random.nextInt(9000); // 4-digit number
        return String.format("%04d-%04d-%04d", part1, part2, part3);
    }

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
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setFatherName(signupRequest.getFatherName());
        user.setDOB(signupRequest.getDOB());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));


        if (user.getDOB() == null) {
            user.setDOB(LocalDate.of(2003, 3, 5));
        }


        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            user.setLastName("test");
        }

        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            user.setFirstName("test");
        }

        if (user.getFatherName() == null || user.getFatherName().isEmpty()) {
            user.setFatherName("test");
        }

        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            user.setAddress("address");
        }
        if (user.getRationCardNumber() == null || user.getRationCardNumber().isEmpty()) {
            user.setRationCardNumber(generateRandomRationCardNumber());
        }

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
