package com.lab.product.controller;

import com.lab.product.DTO.CreateOrUpdateProductRequestDTO;
import com.lab.product.DTO.ProductDetailsDTO;
import com.lab.product.entity.ENUMS.PRODUCT_TYPE;
import com.lab.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(
    name = "Product Management", 
    description = "Comprehensive API for managing financial products including creation, retrieval, update, deletion, and search operations. " +
                  "Products can be of various types (SAVINGS, CURRENT, LOAN, etc.) and contain detailed configuration for pricing, " +
                  "charges, balances, communication templates, and business rules."
)
public class ProductController {

    @Autowired
    private final ProductService productService;

    @PostMapping
    @Operation(
        summary = "Create a new financial product",
        description = "Creates a new product in the system with comprehensive details including product type, status, currency, " +
                      "pricing information, and effective/expiry dates. The product code must be unique across the system. " +
                      "Upon successful creation, the product will be assigned a unique identifier and audit timestamps.",
        tags = {"Product Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Product created successfully. Returns the complete product details including system-generated ID and audit information.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductDetailsDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data. Possible reasons: " +
                          "- Missing required fields (productCode, productName, productType) " +
                          "- Invalid enum values for productType, status, or currency " +
                          "- Product code already exists " +
                          "- Invalid date format or date range",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\": \"Validation failed\", \"message\": \"Product code is required\", \"timestamp\": \"2025-10-10T10:30:00\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while processing the request",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ProductDetailsDTO> createProduct(
            @Parameter(
                description = "Product creation request containing all required and optional product details. " +
                              "Required fields: productCode (unique), productName, productType. " +
                              "Optional fields: description, status, currency, dates, pricing information.",
                required = true
            )
            @Valid @RequestBody CreateOrUpdateProductRequestDTO requestDTO) {
        return new ResponseEntity<>(productService.createProduct(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "Retrieve all products with pagination",
        description = "Fetches a paginated list of all products in the system. Supports sorting and pagination parameters. " +
                      "Use query parameters to control page size, page number, and sort order. " +
                      "Default page size is 20. Results include complete product details along with all related entities " +
                      "(interest rates, charges, balances, roles, rules, transactions, communications).",
        tags = {"Product Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved paginated list of products. Returns a Page object containing product details, " +
                          "total elements, total pages, current page number, and pagination metadata.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while fetching products",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<Page<ProductDetailsDTO>> getAllProducts(
            @Parameter(
                description = "Pagination and sorting parameters. " +
                              "Example: ?page=0&size=20&sort=productName,asc " +
                              "- page: Page number (0-indexed, default: 0) " +
                              "- size: Number of items per page (default: 20, max: 100) " +
                              "- sort: Sort field and direction (e.g., productCode,desc)",
                example = "page=0&size=20&sort=productName,asc"
            )
            Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/{productCode}")
    @Operation(
        summary = "Retrieve a specific product by its unique code",
        description = "Fetches complete details of a product identified by its unique product code. " +
                      "Returns comprehensive product information including all related entities: " +
                      "interest rates, charges, balances, roles, rules, transactions, and communication templates. " +
                      "This is the primary endpoint for retrieving detailed product information.",
        tags = {"Product Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product found successfully. Returns complete product details with all nested entities.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductDetailsDTO.class),
                examples = @ExampleObject(
                    name = "Savings Product Example",
                    value = "{\"productId\":\"123e4567-e89b-12d3-a456-426614174000\",\"productCode\":\"SAV001\",\"productName\":\"Premium Savings Account\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found with the specified product code. Verify the product code and try again.",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\": \"Resource not found\", \"message\": \"Product not found: SAV001\", \"timestamp\": \"2025-10-10T10:30:00\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while retrieving the product",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ProductDetailsDTO> getProductByCode(
            @Parameter(
                description = "Unique product code identifier (e.g., SAV001, CURR001, LOAN001). " +
                              "This is a business-friendly identifier used across the system.",
                required = true,
                example = "SAV001"
            )
            @PathVariable String productCode) {
        return ResponseEntity.ok(productService.getProductByCode(productCode));
    }

    @PutMapping("/{productCode}")
    @Operation(
        summary = "Update an existing product",
        description = "Updates the details of an existing product identified by its product code. " +
                      "This endpoint allows modification of product attributes including name, description, status, " +
                      "pricing information, and effective dates. The product code in the path parameter identifies " +
                      "which product to update. Note: This updates only the core product details; " +
                      "use specific sub-resource endpoints to update interest rates, charges, balances, etc.",
        tags = {"Product Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product updated successfully. Returns the updated product details with latest audit information.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductDetailsDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data. Possible reasons: " +
                          "- Missing required fields " +
                          "- Invalid enum values " +
                          "- Invalid date format or range " +
                          "- Product code mismatch",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\": \"Validation failed\", \"message\": \"Invalid product type\", \"timestamp\": \"2025-10-10T10:30:00\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found with the specified product code",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\": \"Resource not found\", \"message\": \"Product not found: SAV001\", \"timestamp\": \"2025-10-10T10:30:00\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while updating the product",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ProductDetailsDTO> updateProduct(
            @Parameter(
                description = "Unique product code of the product to be updated",
                required = true,
                example = "SAV001"
            )
            @PathVariable String productCode,
            @Parameter(
                description = "Updated product details. All modifiable fields can be included in the request. " +
                              "Required fields: productCode, productName, productType",
                required = true
            )
            @Valid @RequestBody CreateOrUpdateProductRequestDTO requestDTO) {
        return ResponseEntity.ok(productService.updateProduct(productCode, requestDTO));
    }

    @DeleteMapping("/{productCode}")
    @Operation(
        summary = "Delete a product",
        description = "Permanently removes a product from the system identified by its product code. " +
                      "This operation is irreversible and will cascade delete all related entities including: " +
                      "interest rates, charges, balances, roles, rules, transactions, and communication templates. " +
                      "Use with caution in production environments. Consider deactivating products instead of deletion " +
                      "by updating their status to INACTIVE for audit trail purposes.",
        tags = {"Product Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Product deleted successfully. No content returned. " +
                          "All related entities (interest rates, charges, balances, etc.) have also been removed.",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found with the specified product code. The product may have already been deleted.",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\": \"Resource not found\", \"message\": \"Product not found: SAV001\", \"timestamp\": \"2025-10-10T10:30:00\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while deleting the product. " +
                          "The product may be referenced by other entities or systems.",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(
                description = "Unique product code of the product to be deleted. This operation cannot be undone.",
                required = true,
                example = "SAV001"
            )
            @PathVariable String productCode) {
        productService.deleteProduct(productCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(
        summary = "Search and filter products",
        description = "Advanced search functionality to filter products based on multiple criteria. " +
                      "Supports filtering by product type (SAVINGS, CURRENT, LOAN, etc.), status (ACTIVE, INACTIVE, PENDING), " +
                      "and date range (effective date between start and end dates). " +
                      "All parameters are optional and can be combined. If no parameters are provided, returns all products. " +
                      "Date parameters should be in ISO format (YYYY-MM-DD).",
        tags = {"Product Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully. Returns a list of products matching the specified criteria. " +
                          "Empty list is returned if no products match the search criteria.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = List.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid search parameters. Possible reasons: " +
                          "- Invalid product type or status enum value " +
                          "- Invalid date format (must be YYYY-MM-DD) " +
                          "- End date is before start date " +
                          "- Missing end date when start date is provided, or vice versa",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\": \"Validation failed\", \"message\": \"Invalid date format. Use ISO format (YYYY-MM-DD)\", \"timestamp\": \"2025-10-10T10:30:00\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while searching products",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<List<ProductDetailsDTO>> searchProducts(
            @Parameter(
                description = "Filter by product type. Valid values: SAVINGS, CURRENT, LOAN, CREDIT_CARD, FIXED_DEPOSIT, RECURRING_DEPOSIT. " +
                              "Case-insensitive. Returns all products of the specified type.",
                example = "SAVINGS"
            )
            @RequestParam(required = false) String productType,
            @Parameter(
                description = "Filter by product status. Valid values: ACTIVE, INACTIVE, PENDING, SUSPENDED, CLOSED. " +
                              "Case-insensitive. Returns all products with the specified status.",
                example = "ACTIVE"
            )
            @RequestParam(required = false) String status,
            @Parameter(
                description = "Start date for date range filter (inclusive). Format: YYYY-MM-DD. " +
                              "Filters products by effective date. Must be used together with endDate parameter.",
                example = "2025-01-01"
            )
            @RequestParam(required = false) String startDate,
            @Parameter(
                description = "End date for date range filter (inclusive). Format: YYYY-MM-DD. " +
                              "Filters products by effective date. Must be used together with startDate parameter. " +
                              "Must be equal to or after startDate.",
                example = "2025-12-31"
            )
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(productService.searchProducts(productType, status, startDate, endDate));
    }
}
