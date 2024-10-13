package com.borneo.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 40)
    private String username;

    @NotBlank
    @Size(min = 8, max = 50)
    private String password;

    @NotBlank
    @Email
    private String email;

    private String signUpType; // 'USER', 'ADMIN', 'VENDOR'

    private String secretCode; // Optional
}
