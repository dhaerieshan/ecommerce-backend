package com.borneo.ecommerce.controller;

import com.borneo.ecommerce.dto.UserProfileResponse;
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
            description = "Returns dashboard summary for the authenticated vendor including products and revenue",
            tags = {"Profile & Dashboard"},
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vendor dashboard data retrieved",
                            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Access restricted to vendors only")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/dashboard")
    public String vendorDashboard() {
        return "Welcome to the Vendor Dashboard!";
    }


}
