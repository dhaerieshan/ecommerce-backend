package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Authenticated user's profile")
public class UserProfileResponse {

    @Schema(description = "Username", example = "priya_user")
    private String username;

    @Schema(description = "Email address", example = "priya@example.com")
    private String email;

    @Schema(description = "First name", example = "Priya")
    private String firstName;

    @Schema(description = "Last name", example = "Sharma")
    private String lastName;

    @Schema(description = "Delivery address", example = "45 MG Road, Bangalore")
    private String address;
}
