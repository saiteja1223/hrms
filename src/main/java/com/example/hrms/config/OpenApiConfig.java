package com.example.hrms.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                // Define the security scheme (JWT Bearer token)
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                // Add the security requirement to all endpoints
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                // Add general API information
                .info(new Info()
                        .title("HRMS API - Human Resource Management System")
                        .version("1.0.0")
                        .description("This API provides a complete set of endpoints for managing the employee lifecycle, from onboarding to offboarding.")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
