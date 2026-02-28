package com.borneo.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
@Schema(description = "Represents a user role for access control (e.g., ROLE_USER, ROLE_ADMIN)")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(
          description = "Unique identifier of the role",
          example = "1",
          accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(nullable = false)
  @Schema(
          description = "Name of the role",
          example = "ROLE_ADMIN",
          requiredMode = Schema.RequiredMode.REQUIRED)
  private String name;
}
