package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
@Schema(description = "User details response")
public class UserResponseDTO {

    @Schema(description = "User ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Username", example = "Dhaerieshan")
    private String username;

    @Schema(description = "Valid email address", example = "Dhaerie@example.com")
    private String email;

    @Schema(description = "Assigned roles", example = "[\"ROLE_USER\"]")
    private Set<String> roles;

    @Schema(description = "First name", example = "Dhaerieshan")
    private String firstName;

    @Schema(description = "Last name", example = "M")
    private String lastName;

    @Schema(description = "Delivery address", example = "123 Main Street, Chennai, Tamil Nadu")
    private String address;
}
