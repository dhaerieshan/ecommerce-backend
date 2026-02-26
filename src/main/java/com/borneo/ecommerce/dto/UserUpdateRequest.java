package com.borneo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "User update request - only provide fields you want to change")
public class UserUpdateRequest {

    @Size(min = 3, max = 40)
    @Schema(description = "New username", example = "Dhaerieshan_updated")
    private String username;

    @Email
    @Schema(description = "New email address", example = "Dhaerie_updated@example.com")
    private String email;

    @Size(min = 8, max = 50)
    @Schema(description = "New password (min 8 characters)", example = "newpassword@123")
    private String password;

    @Schema(description = "First name", example = "DhaerieShan")
    private String firstName;

    @Schema(description = "Last name", example = "M")
    private String lastName;

    @Schema(description = "Delivery address", example = "456 New Street, Madurai, Tamil Nadu")
    private String address;
}
