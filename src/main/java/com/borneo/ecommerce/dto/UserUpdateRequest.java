package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "User profile update request — all fields are optional")
public class UserUpdateRequest {

  @Size(min = 3, max = 40)
  @Schema(description = "New username", example = "priya_updated", minLength = 3, maxLength = 40)
  private String username;

  @Email
  @Schema(description = "New email address", example = "priya.new@example.com")
  private String email;

  @Size(min = 8, max = 50)
  @Schema(
      description = "New password (min 8 characters)",
      example = "newpass@123",
      minLength = 8,
      maxLength = 50)
  private String password;

  @Schema(description = "First name", example = "Priya")
  private String firstName;

  @Schema(description = "Last name", example = "Sharma")
  private String lastName;

  @Schema(description = "Delivery address", example = "12 New Street, Chennai")
  private String address;
}
