package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;

@Data
@Schema(description = "User details (admin view)")
public class UserResponseDTO {

  @Schema(description = "User ID", example = "3")
  private Long id;

  @Schema(description = "Username", example = "priya_user")
  private String username;

  @Schema(description = "Email address", example = "priya@example.com")
  private String email;

  @Schema(description = "Assigned roles", example = "[\"USER\"]")
  private Set<String> roles;

  @Schema(description = "First name", example = "Priya")
  private String firstName;

  @Schema(description = "Last name", example = "Sharma")
  private String lastName;

  @Schema(description = "Address", example = "45 MG Road, Bangalore")
  private String address;
}
