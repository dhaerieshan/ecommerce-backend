package com.borneo.ecommerce.security;

import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class newtry {

    private static final long JWT_EXPIRATION_MS = 86400000;
    public static Key key;
    @Autowired
    private static UserRepository userRepository;
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public static String getRoleFromJwtTokenn(String token) {

        String role;
        try {
            String username = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            role = String.valueOf(getUserRolesn(username));
            System.out.println(role);

        } catch (Exception e) {
            System.out.println("Error extracting username from token: {}" + e.getMessage());
            throw e;
        }
        return role;
    }

    public static List<String> getUserRolesn(String username) {
        // Find the user by username
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));

        // If user exists, extract and return their roles
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getRoles().stream()
                    .map(Role::getName)  // Extract role names
                    .collect(Collectors.toList());
        }
        // Return empty list if user is not found
        return List.of();
    }

    public static void main(String[] args) {
        getRoleFromJwtTokenn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjUiLCJpYXQiOjE3Mjg4NDEzNTEsImV4cCI6MTcyODkyNzc1MX0.CnXav7xKlsFDAYA4TaTcb5PjGdv986scu_6XQUWtR6Q");
    }
}
