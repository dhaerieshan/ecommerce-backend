package com.borneo.ecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(
                    new Info()
                            .title("E-Commerce Backend API")
                            .version("v1.0")
                            .description(
                                    """
                                Complete REST API for E-Commerce platform.

                                Features:
                                - JWT Authentication
                                - Role-based Access (ADMIN, VENDOR, USER)
                                - Product & Category Management
                                - Cart & Order Processing
                                - OTP Verification
                                - Wishlist Management
                                """))
            .components(
                    new Components()
                            .addSecuritySchemes(
                                    "bearerAuth",
                                    new SecurityScheme()
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")));
  }
}
