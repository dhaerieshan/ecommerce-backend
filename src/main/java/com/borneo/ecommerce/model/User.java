package com.borneo.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Schema(description = "Represents a registered user in the e-commerce platform")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Unique identifier of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(nullable = false, unique = true)
  @Schema(description = "Unique username for login", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
  private String username;

  @Column(nullable = false)
  @Schema(description = "Hashed password of the user", example = "hashed_password_here", accessMode = Schema.AccessMode.WRITE_ONLY)
  private String password;

  @Column(nullable = false, unique = true)
  @Schema(description = "Unique email address of the user", example = "john@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
  private String email;

  @Column(length = 50)
  @Schema(description = "First name of the user", example = "John")
  private String firstName;

  @Column(length = 50)
  @Schema(description = "Last name of the user", example = "Doe")
  private String lastName;

  @Column(nullable = false)
  @Schema(description = "Shipping or billing address of the user", example = "123 Main St, Jakarta, Indonesia", requiredMode = Schema.RequiredMode.REQUIRED)
  private String address;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  @Schema(description = "Set of roles assigned to the user")
  private Set<Role> roles = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Schema(description = "List of orders placed by the user")
  private List<Order> orders;
}
