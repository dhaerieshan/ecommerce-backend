package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "04. Vendors", description = "Vendor registration and vendor management APIs")
@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    @Operation(
            summary = "Get vendor dashboard",
            description = "Returns a welcome message and dashboard summary for the authenticated vendor.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vendor dashboard data retrieved",
                            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Access restricted to VENDOR role only")
            }
    )
    @GetMapping("/dashboard")
    public String vendorDashboard() {
        return "Welcome to the Vendor Dashboard!";
    }
}
