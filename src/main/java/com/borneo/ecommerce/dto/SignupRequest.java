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
    @Schema(description = "Unique username", example = "Dhaerieshan")
    private String username;

    @NotBlank
    @Size(min = 8, max = 50)
    @Schema(description = "Password (min 8 characters)", example = "password@123")
    private String password;

    @NotBlank
    @Email
    @Schema(description = "Valid email address", example = "Dhaerie@example.com")
    private String email;

    @Schema(description = "Signup type: USER, VENDOR", example = "USER")
    private String signUpType;

    @Schema(description = "Secret code required for ADMIN/VENDOR signup", example = "SECRET123")
    private String secretCode;

    @Schema(description = "First name", example = "Dhaerieshan")
    private String firstName;

    @Schema(description = "Last name", example = "M")
    private String lastName;

    @Schema(description = "Delivery address", example = "123 Main Street, Chennai, Tamil Nadu")
    private String address;
}
