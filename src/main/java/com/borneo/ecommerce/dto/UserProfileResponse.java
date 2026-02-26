package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User profile response")
public class UserProfileResponse {

    @Schema(description = "Username", example = "Dhaerieshan")
    private String username;

    @Schema(description = "Valid email address", example = "Dhaerie@example.com")
    private String email;

    @Schema(description = "First name", example = "Dhaerieshan")
    private String firstName;

    @Schema(description = "Last name", example = "M")
    private String lastName;

    @Schema(description = "Delivery address", example = "123 Main Street, Chennai, Tamil Nadu")
    private String address;
}
