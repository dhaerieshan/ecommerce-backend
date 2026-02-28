package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Login credentials")
public class LoginRequest {

  @NotBlank
  @Schema(description = "Username", example = "priya_user")
  private String username;

  @NotBlank
  @Schema(description = "Password", example = "password")
  private String password;
}
