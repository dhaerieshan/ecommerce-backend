package com.borneo.ecommerce.security;

import com.borneo.ecommerce.model.Role;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

  private static final long JWT_EXPIRATION_MS = 86400000;

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  private Key key;

  @Autowired
  private UserRepository userRepository;

  @PostConstruct
  public void init() {
    key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateJwtToken(String username, String role) {
    return Jwts.builder()
            .setSubject(username)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION_MS))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    try {
      String username =
              Jwts.parserBuilder()
                      .setSigningKey(key)
                      .build()
                      .parseClaimsJws(token)
                      .getBody()
                      .getSubject();
      System.out.println(("Extracted username from token: {}" + username));
      return username;
    } catch (Exception e) {
      System.out.println("Error extracting username from token: {}" + e.getMessage());
      throw e;
    }
  }

  public String getRoleFromJwtToken(String token) {
    String role =
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role", String.class);
    System.out.println(("Extracted username from token: {}" + role));
    return role;
  }

  public String getUserRoles(String username) {

    Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      return user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", "));
    }
    return "";
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
      return true;
    } catch (JwtException e) {
      System.out.println("Invalid JWT token: " + e.getMessage());
    }
    return false;
  }
}
