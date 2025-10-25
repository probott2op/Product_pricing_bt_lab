package com.lab.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI productPricingOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Product and Pricing Management API")
                .description(
                    "# Comprehensive Banking Product & Pricing Management System\n\n" +
                    "## Overview\n" +
                    "Enterprise-grade RESTful API for managing financial products, pricing structures, and product configurations " +
                    "in banking and financial institutions. This system provides complete lifecycle management for various " +
                    "financial products including Savings Accounts, Current Accounts, Fixed Deposits, Loans, Credit Cards, " +
                    "and more.\n\n" +
                    "## Key Features\n" +
                    "- **Product Management**: Create, update, retrieve, and manage financial products with comprehensive attributes\n" +
                    "- **Interest Rate Configuration**: Define simple and compound interest rates with tiered structures\n" +
                    "- **Charge & Fee Management**: Configure product charges, fees, and pricing models\n" +
                    "- **Balance Configuration**: Set minimum balance requirements, overdraft limits, and balance types\n" +
                    "- **Business Rules Engine**: Define eligibility criteria, validation rules, and business logic\n" +
                    "- **Transaction Types**: Configure allowed transaction types, limits, and channel availability\n" +
                    "- **Communication Templates**: Manage customer notifications and communication workflows\n" +
                    "- **Role-Based Access Control**: Define permissions and approval workflows\n\n" +
                    "## API Design Principles\n" +
                    "- **RESTful Architecture**: Standard HTTP methods (GET, POST, PUT, DELETE)\n" +
                    "- **Code-Based Routing**: All endpoints use business-friendly codes instead of technical IDs\n" +
                    "- **Pagination Support**: Efficient data retrieval with page, size, and sort parameters\n" +
                    "- **Comprehensive Validation**: Request validation using Jakarta Bean Validation\n" +
                    "- **Audit Trail**: Automatic tracking of created/updated by and timestamps\n" +
                    "- **Error Handling**: Standardized error responses with meaningful messages\n\n" +
                    "## Response Codes\n" +
                    "- **200 OK**: Successful GET/PUT request\n" +
                    "- **201 Created**: Successful POST request with resource creation\n" +
                    "- **204 No Content**: Successful DELETE request\n" +
                    "- **400 Bad Request**: Invalid request data or validation failure\n" +
                    "- **404 Not Found**: Resource not found\n" +
                    "- **500 Internal Server Error**: Server-side error\n\n" +
                    "## Getting Started\n" +
                    "1. Browse the API endpoints organized by functional areas\n" +
                    "2. Review the data models and required/optional fields\n" +
                    "3. Use the 'Try it out' feature to test endpoints\n" +
                    "4. Check example requests and responses for each operation\n\n" +
                    "## Authentication\n" +
                    "This API uses JWT Bearer token authentication from NEXA Bank Auth Service.\n\n" +
                    "**How to authenticate:**\n" +
                    "1. Click the **Authorize** button (🔓) at the top of this page\n" +
                    "2. Enter your JWT token in the format: `Bearer <your_token>`\n" +
                    "3. Click **Authorize** to apply the token to all requests\n\n" +
                    "**To get a JWT token:**\n" +
                    "```bash\n" +
                    "curl -X POST http://localhost:3020/api/auth/login \\\n" +
                    "  -H \"Content-Type: application/json\" \\\n" +
                    "  -d '{\"email\": \"admin@nexabank.com\", \"password\": \"YourPassword\"}'\n" +
                    "```\n\n" +
                    "**Access Rules:**\n" +
                    "- **GET requests**: Public (no authentication required)\n" +
                    "- **POST/PUT/DELETE requests**: Require ADMIN role in JWT token\n\n" +
                    "## Support\n" +
                    "For technical support, documentation, or API access requests, contact the Banking Labs support team."
                )
                .version("1.0.0")
                .contact(new Contact()
                    .name("Banking Labs Development Team")
                    .email("support@bankinglabs.com")
                    .url("https://www.bankinglabs.com/support"))
                .license(new License()
                    .name("Apache License 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Local Development Server"),
                new Server()
                    .url("https://api-dev.bankinglabs.com")
                    .description("Development Environment"),
                new Server()
                    .url("https://api-uat.bankinglabs.com")
                    .description("User Acceptance Testing Environment"),
                new Server()
                    .url("https://api.bankinglabs.com")
                    .description("Production Environment")
            ))
            .externalDocs(new ExternalDocumentation()
                .description("Complete API Documentation & User Guide")
                .url("https://docs.bankinglabs.com/product-pricing-api"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("JWT token from NEXA Bank Auth Service. " +
                        "Format: `Bearer <your_jwt_token>`. " +
                        "Get token from: http://localhost:3020/api/auth/login")))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}