package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "User login request")
public class LoginRequest {

    @NotBlank
    @Schema(description = "Your registered username", example = "Dhaerieshan")
    private String username;

    @NotBlank
    @Schema(description = "Your password", example = "password@123")
    private String password;
}
