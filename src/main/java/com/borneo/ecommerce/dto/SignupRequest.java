package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "User registration request")
public class SignupRequest {

  @NotBlank
  @Size(min = 3, max = 40)
  @Schema(description = "Unique username", example = "priya_user", minLength = 3, maxLength = 40)
  private String username;

  @NotBlank
  @Size(min = 8, max = 50)
  @Schema(
      description = "Password (min 8 characters)",
      example = "password@123",
      minLength = 8,
      maxLength = 50)
  private String password;

  @NotBlank
  @Email
  @Schema(description = "Valid email address", example = "priya@example.com")
  private String email;

  @Schema(
      description = "Account type: USER (default), VENDOR, or ADMIN",
      example = "USER",
      allowableValues = {"USER", "VENDOR", "ADMIN"})
  private String signUpType;

  @Schema(description = "Required only for VENDOR or ADMIN signup", example = "secret123")
  private String secretCode;

  @Schema(description = "First name", example = "Priya")
  private String firstName;

  @Schema(description = "Last name", example = "Sharma")
  private String lastName;

  @Schema(description = "Delivery address", example = "45 MG Road, Bangalore")
  private String address;
}
