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
        description = """
            Create a new financial product in the system with comprehensive configuration.
            
            **Product Configuration Includes:**
            
            1. **Basic Information:**
               - Unique product code (identifier)
               - Product name and description
               - Product type (SAVINGS, CURRENT, LOAN, DEPOSIT, etc.)
               - Currency and status
            
            2. **Interest Configuration:**
               - Interest type: SIMPLE or COMPOUND
               - Compounding frequency: DAILY, MONTHLY, QUARTERLY, SEMI_ANNUALLY, ANNUALLY
               - These settings define how interest is calculated for the product
            
            3. **Effective Dates:**
               - Effective date: When the product becomes available
               - Expiry date: When the product is discontinued (optional)
            
            **After Creation:**
            - Product receives a unique UUID
            - Child configurations can be added:
              * Interest rates (via /api/products/{code}/interest)
              * Charges/Fees (via /api/products/{code}/charges)
              * Balance types (via /api/products/{code}/balances)
              * Business rules (via /api/products/{code}/rules)
              * Allowed transactions (via /api/products/{code}/transactions)
              * User roles (via /api/products/{code}/roles)
              * Communication templates (via /api/products/{code}/communications)
            
            **Example Scenarios:**
            
            **Savings Account:**
            - Compound interest with daily compounding
            - No maintenance fees
            - Minimum balance requirements
            
            **Fixed Deposit:**
            - Simple or compound interest
            - Quarterly/Annual compounding
            - Penalty for early withdrawal
            
            **Loan Product:**
            - Compound interest with monthly compounding
            - Processing fees and penalties
            - Balance types: LOAN_PRINCIPAL, LOAN_INTEREST, PENALTY
            """,
        tags = {"Product Management"},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Product creation details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreateOrUpdateProductRequestDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Savings Account Product",
                        summary = "High-yield savings account with daily compound interest",
                        value = """
                            {
                              "productCode": "SAV-HIGH-YIELD-2025",
                              "productName": "High Yield Savings Account",
                              "productType": "SAVINGS",
                              "description": "Premium savings account with competitive interest rates",
                              "currency": "USD",
                              "status": "ACTIVE",
                              "interestType": "COMPOUND",
                              "compoundingFrequency": "DAILY",
                              "efctv_date": "2025-01-01",
                              "expr_date": "2026-12-31"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Fixed Deposit Product",
                        summary = "1-year fixed deposit with quarterly compounding",
                        value = """
                            {
                              "productCode": "FD-1Y-2025",
                              "productName": "1 Year Fixed Deposit",
                              "productType": "DEPOSIT",
                              "description": "Fixed deposit with guaranteed returns",
                              "currency": "USD",
                              "status": "ACTIVE",
                              "interestType": "COMPOUND",
                              "compoundingFrequency": "QUARTERLY",
                              "efctv_date": "2025-01-01"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Personal Loan Product",
                        summary = "Personal loan with monthly compound interest",
                        value = """
                            {
                              "productCode": "LOAN-PERSONAL-5Y",
                              "productName": "Personal Loan - 5 Years",
                              "productType": "LOAN",
                              "description": "Unsecured personal loan up to $50,000",
                              "currency": "USD",
                              "status": "ACTIVE",
                              "interestType": "COMPOUND",
                              "compoundingFrequency": "MONTHLY",
                              "efctv_date": "2025-01-01"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Current Account Product",
                        summary = "Business current account with no interest",
                        value = """
                            {
                              "productCode": "CUR-BUSINESS-2025",
                              "productName": "Business Current Account",
                              "productType": "CURRENT",
                              "description": "Zero-balance business account with unlimited transactions",
                              "currency": "USD",
                              "status": "ACTIVE",
                              "efctv_date": "2025-01-01"
                            }
                            """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Product created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductDetailsDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Created Savings Product",
                        summary = "Response after creating savings account product",
                        value = """
                            {
                              "productId": "550e8400-e29b-41d4-a716-446655440000",
                              "productCode": "SAV-HIGH-YIELD-2025",
                              "productName": "High Yield Savings Account",
                              "productType": "SAVINGS",
                              "currency": "USD",
                              "status": "ACTIVE",
                              "interestType": "COMPOUND",
                              "compoundingFrequency": "DAILY",
                              "productRules": [],
                              "productCharges": [],
                              "productRoles": [],
                              "productTransactions": [],
                              "productBalances": [],
                              "productCommunications": [],
                              "createdAt": "2025-10-15T10:30:00",
                              "efctv_date": "2025-01-01"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Created Loan Product",
                        summary = "Response after creating loan product",
                        value = """
                            {
                              "productId": "660e8400-e29b-41d4-a716-446655440111",
                              "productCode": "LOAN-PERSONAL-5Y",
                              "productName": "Personal Loan - 5 Years",
                              "productType": "LOAN",
                              "currency": "USD",
                              "status": "ACTIVE",
                              "interestType": "COMPOUND",
                              "compoundingFrequency": "MONTHLY",
                              "productRules": [],
                              "productCharges": [],
                              "productRoles": [],
                              "productTransactions": [],
                              "productBalances": [],
                              "productCommunications": [],
                              "createdAt": "2025-10-15T10:31:00",
                              "efctv_date": "2025-01-01"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Missing Required Field",
                        value = """
                            {
                              "error": "Validation failed",
                              "message": "Product code is required",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Invalid Enum Value",
                        value = """
                            {
                              "error": "Validation failed",
                              "message": "Invalid enum value: No enum constant INTEREST_TYPE.COMPLEX",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Duplicate Product Code",
                        value = """
                            {
                              "error": "Validation failed",
                              "message": "Product code SAV-HIGH-YIELD-2025 already exists",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Internal Server Error",
                          "message": "Database connection failed",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<ProductDetailsDTO> createProduct(
            @Valid @RequestBody CreateOrUpdateProductRequestDTO requestDTO) {
        return new ResponseEntity<>(productService.createProduct(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "Retrieve all products with pagination and sorting",
        description = """
            Retrieve a paginated list of all financial products in the system with flexible sorting options.
            
            **What it returns:**
            - Complete product details including all nested configurations
            - Pagination metadata (total elements, total pages, page number)
            - Sorted results based on specified criteria
            
            **Pagination Parameters:**
            - **page**: Page number (0-indexed, default: 0)
            - **size**: Items per page (default: 20, max: 100)
            - **sort**: Sort field and direction (e.g., productName,asc)
            
            **Sorting Options:**
            - productCode (asc/desc)
            - productName (asc/desc)
            - productType (asc/desc)
            - status (asc/desc)
            - createdAt (asc/desc)
            
            **Use Cases:**
            
            **Scenario 1: Product Catalog Display**
            - Retrieve first page with 20 products
            - Sort alphabetically by product name
            - Display in UI for customer selection
            
            **Scenario 2: Admin Dashboard**
            - Retrieve all ACTIVE products
            - Sort by creation date (newest first)
            - Monitor recently launched products
            
            **Scenario 3: Product Search Results**
            - Show filtered/searched products
            - Paginate large result sets
            - Sort by relevance or name
            
            **Related Endpoints:**
            - GET /api/products/{code} - Get specific product
            - GET /api/products/search - Search with filters
            - POST /api/products - Create new product
            
            **Performance Notes:**
            - Large page sizes may impact response time
            - Default page size (20) balances performance and usability
            - Use search endpoint for filtered queries
            """,
        tags = {"Product Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved paginated product list",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class),
                examples = {
                    @ExampleObject(
                        name = "First Page - 3 Products",
                        summary = "First page with 3 products (size=3)",
                        value = """
                            {
                              "content": [
                                {
                                  "productId": "550e8400-e29b-41d4-a716-446655440000",
                                  "productCode": "SAV-HIGH-YIELD-2025",
                                  "productName": "High Yield Savings Account",
                                  "productType": "SAVINGS",
                                  "currency": "USD",
                                  "status": "ACTIVE",
                                  "interestType": "COMPOUND",
                                  "compoundingFrequency": "DAILY",
                                  "productRules": [],
                                  "productCharges": [],
                                  "productBalances": [],
                                  "createdAt": "2025-10-15T10:30:00"
                                },
                                {
                                  "productId": "660e8400-e29b-41d4-a716-446655440111",
                                  "productCode": "FD-1Y-2025",
                                  "productName": "1 Year Fixed Deposit",
                                  "productType": "DEPOSIT",
                                  "currency": "USD",
                                  "status": "ACTIVE",
                                  "interestType": "COMPOUND",
                                  "compoundingFrequency": "QUARTERLY",
                                  "productRules": [],
                                  "productCharges": [],
                                  "productBalances": [],
                                  "createdAt": "2025-10-15T10:31:00"
                                },
                                {
                                  "productId": "770e8400-e29b-41d4-a716-446655440222",
                                  "productCode": "LOAN-PERSONAL-5Y",
                                  "productName": "Personal Loan - 5 Years",
                                  "productType": "LOAN",
                                  "currency": "USD",
                                  "status": "ACTIVE",
                                  "interestType": "COMPOUND",
                                  "compoundingFrequency": "MONTHLY",
                                  "productRules": [],
                                  "productCharges": [],
                                  "productBalances": [],
                                  "createdAt": "2025-10-15T10:32:00"
                                }
                              ],
                              "pageable": {
                                "pageNumber": 0,
                                "pageSize": 3,
                                "sort": {
                                  "sorted": true,
                                  "unsorted": false,
                                  "empty": false
                                },
                                "offset": 0,
                                "paged": true,
                                "unpaged": false
                              },
                              "totalElements": 15,
                              "totalPages": 5,
                              "last": false,
                              "first": true,
                              "size": 3,
                              "number": 0,
                              "numberOfElements": 3,
                              "empty": false
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Empty Result",
                        summary = "No products found (empty database or filtered out)",
                        value = """
                            {
                              "content": [],
                              "pageable": {
                                "pageNumber": 0,
                                "pageSize": 20,
                                "sort": {
                                  "sorted": false,
                                  "unsorted": true,
                                  "empty": true
                                },
                                "offset": 0,
                                "paged": true,
                                "unpaged": false
                              },
                              "totalElements": 0,
                              "totalPages": 0,
                              "last": true,
                              "first": true,
                              "size": 20,
                              "number": 0,
                              "numberOfElements": 0,
                              "empty": true
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Internal Server Error",
                          "message": "Database connection failed",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<Page<ProductDetailsDTO>> getAllProducts(
            @Parameter(
                description = """
                    Pagination and sorting parameters:
                    - page: Page number (0-indexed)
                    - size: Items per page (max 100)
                    - sort: field,direction (e.g., productName,asc)
                    
                    Examples:
                    - ?page=0&size=20
                    - ?page=1&size=10&sort=productName,asc
                    - ?sort=createdAt,desc&size=50
                    """,
                example = "page=0&size=20&sort=productName,asc"
            )
            Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/{productCode}")
    @Operation(
        summary = "Retrieve complete details of a specific product",
        description = """
            Fetch comprehensive information about a product using its unique product code.
            
            **What it returns:**
            - Complete product details (code, name, type, status, currency)
            - Interest configuration (type and compounding frequency)
            - All related entities:
              * Interest rate tiers and configurations
              * Fee and charge schedules
              * Applicable balance types
              * Associated roles and permissions
              * Business rules and validations
              * Transaction configurations
              * Communication templates
            
            **Use Cases:**
            
            **Scenario 1: Customer Product Inquiry**
            - Customer service agent needs product details
            - Retrieve complete product configuration
            - View interest rates, fees, terms
            
            **Scenario 2: Product Configuration Review**
            - Admin reviewing product setup
            - Verify all interest rates and charges configured
            - Check business rules and validations
            
            **Scenario 3: Account Opening Process**
            - System needs product details for new account
            - Retrieve interest calculation rules
            - Apply correct charges and balance types
            
            **Scenario 4: Product Comparison**
            - Customer comparing multiple products
            - Retrieve each product's full details
            - Compare interest rates, fees, features
            
            **Related Endpoints:**
            - GET /api/products - List all products
            - PUT /api/products/{code} - Update product
            - DELETE /api/products/{code} - Remove product
            - GET /api/products/search - Search products
            
            **Response Details:**
            - All nested collections included in single response
            - No need for separate calls to get related data
            - Complete configuration ready for use
            """,
        tags = {"Product Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product found successfully with complete details",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductDetailsDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Complete Savings Product",
                        summary = "High Yield Savings with daily compounding",
                        value = """
                            {
                              "productId": "550e8400-e29b-41d4-a716-446655440000",
                              "productCode": "SAV-HIGH-YIELD-2025",
                              "productName": "High Yield Savings Account",
                              "productType": "SAVINGS",
                              "productDescription": "Premium savings account with competitive interest rates",
                              "currency": "USD",
                              "status": "ACTIVE",
                              "interestType": "COMPOUND",
                              "compoundingFrequency": "DAILY",
                              "effectiveStartDate": "2025-01-01",
                              "effectiveEndDate": null,
                              "productInterest": [
                                {
                                  "interestId": "661e8400-e29b-41d4-a716-446655440001",
                                  "tierName": "Tier 1",
                                  "minBalance": 0.00,
                                  "maxBalance": 10000.00,
                                  "interestRate": 3.50,
                                  "isActive": true
                                },
                                {
                                  "interestId": "662e8400-e29b-41d4-a716-446655440002",
                                  "tierName": "Tier 2",
                                  "minBalance": 10000.01,
                                  "maxBalance": 50000.00,
                                  "interestRate": 4.00,
                                  "isActive": true
                                }
                              ],
                              "productCharges": [
                                {
                                  "chargeId": "771e8400-e29b-41d4-a716-446655440003",
                                  "chargeName": "Minimum Balance Fee",
                                  "chargeType": "FLAT",
                                  "chargeAmount": 5.00,
                                  "chargeFrequency": "MONTHLY",
                                  "isActive": true
                                }
                              ],
                              "productBalances": [
                                {
                                  "balanceId": "881e8400-e29b-41d4-a716-446655440004",
                                  "balanceType": "FD_PRINCIPAL",
                                  "isActive": true
                                },
                                {
                                  "balanceId": "882e8400-e29b-41d4-a716-446655440005",
                                  "balanceType": "FD_INTEREST",
                                  "isActive": true
                                }
                              ],
                              "productRules": [],
                              "productTransactions": [],
                              "productRoles": [],
                              "productCommunications": [],
                              "createdAt": "2025-01-01T00:00:00",
                              "updatedAt": "2025-01-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Personal Loan Product",
                        summary = "5-year personal loan with monthly compounding",
                        value = """
                            {
                              "productId": "770e8400-e29b-41d4-a716-446655440222",
                              "productCode": "LOAN-PERSONAL-5Y",
                              "productName": "Personal Loan - 5 Years",
                              "productType": "LOAN",
                              "productDescription": "Unsecured personal loan up to $50,000",
                              "currency": "USD",
                              "status": "ACTIVE",
                              "interestType": "COMPOUND",
                              "compoundingFrequency": "MONTHLY",
                              "effectiveStartDate": "2025-01-01",
                              "effectiveEndDate": null,
                              "productInterest": [
                                {
                                  "interestId": "663e8400-e29b-41d4-a716-446655440010",
                                  "tierName": "Standard Rate",
                                  "minBalance": 0.00,
                                  "maxBalance": 999999999.99,
                                  "interestRate": 8.50,
                                  "isActive": true
                                }
                              ],
                              "productCharges": [
                                {
                                  "chargeId": "772e8400-e29b-41d4-a716-446655440011",
                                  "chargeName": "Origination Fee",
                                  "chargeType": "PERCENTAGE",
                                  "chargeAmount": 2.00,
                                  "chargeFrequency": "ONE_TIME",
                                  "isActive": true
                                },
                                {
                                  "chargeId": "773e8400-e29b-41d4-a716-446655440012",
                                  "chargeName": "Late Payment Fee",
                                  "chargeType": "FLAT",
                                  "chargeAmount": 35.00,
                                  "chargeFrequency": "PER_OCCURRENCE",
                                  "isActive": true
                                }
                              ],
                              "productBalances": [
                                {
                                  "balanceId": "883e8400-e29b-41d4-a716-446655440013",
                                  "balanceType": "LOAN_PRINCIPAL",
                                  "isActive": true
                                },
                                {
                                  "balanceId": "884e8400-e29b-41d4-a716-446655440014",
                                  "balanceType": "LOAN_INTEREST",
                                  "isActive": true
                                },
                                {
                                  "balanceId": "885e8400-e29b-41d4-a716-446655440015",
                                  "balanceType": "PENALTY",
                                  "isActive": true
                                }
                              ],
                              "productRules": [],
                              "productTransactions": [],
                              "productRoles": [],
                              "productCommunications": [],
                              "createdAt": "2025-01-01T00:00:00",
                              "updatedAt": "2025-01-10T14:20:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Product Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Product not found with code: SAV-INVALID-2025",
                              "timestamp": "2025-10-15T10:30:00",
                              "path": "/api/products/SAV-INVALID-2025"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Internal Server Error",
                          "message": "Failed to retrieve product details",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<ProductDetailsDTO> getProductByCode(
            @Parameter(
                description = """
                    Unique product code identifier (business key).
                    
                    Examples:
                    - SAV-HIGH-YIELD-2025
                    - FD-1Y-2025
                    - LOAN-PERSONAL-5Y
                    - CURR-BUSINESS-2025
                    
                    Format: Usually {TYPE}-{VARIANT}-{YEAR}
                    """,
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode) {
        return ResponseEntity.ok(productService.getProductByCode(productCode));
    }

    @PutMapping("/{productCode}")
    @Operation(
        summary = "Update an existing product's core details",
        description = """
            Update the core configuration of an existing product. This endpoint modifies product metadata,
            status, interest configuration, and effective dates.
            
            **What can be updated:**
            - Product name and description
            - Product status (ACTIVE/INACTIVE/PENDING)
            - Interest type (SIMPLE/COMPOUND)
            - Compounding frequency (DAILY/MONTHLY/QUARTERLY/SEMI_ANNUALLY/ANNUALLY)
            - Effective start and end dates
            
            **What CANNOT be updated:**
            - Product code (immutable identifier)
            - Product type (structural change not allowed)
            - Product ID (system generated)
            - Creation timestamp
            
            **Important Notes:**
            - Related entities (interest rates, charges, balances) are updated via separate endpoints
            - Changing status to INACTIVE prevents new accounts but doesn't affect existing ones
            - End date must be after start date
            - Product code in path must match code in request body
            
            **Use Cases:**
            
            **Scenario 1: Update Product Status**
            - Deactivate outdated product
            - Change status from PENDING to ACTIVE
            - Set end date for product retirement
            
            **Scenario 2: Modify Interest Configuration**
            - Change from SIMPLE to COMPOUND interest
            - Update compounding frequency from QUARTERLY to MONTHLY
            - Adjust interest calculation method
            
            **Scenario 3: Update Product Metadata**
            - Rename product for clarity
            - Update product description
            - Modify marketing text
            
            **Scenario 4: Extend Product Lifecycle**
            - Update effective end date
            - Extend product availability
            - Modify effective date range
            
            **Related Endpoints:**
            - PUT /api/products/{code}/interest/{id} - Update interest rates
            - PUT /api/products/{code}/charge/{id} - Update charges
            - PUT /api/products/{code}/balance/{id} - Update balance config
            - GET /api/products/{code} - View current details
            
            **Validation Rules:**
            - Required: productCode, productName, productType
            - Enum values must be valid (SIMPLE/COMPOUND, DAILY/MONTHLY/etc.)
            - Dates must be in ISO-8601 format
            - effectiveEndDate > effectiveStartDate
            """,
        tags = {"Product Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductDetailsDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Status Change to INACTIVE",
                        summary = "Deactivated savings product with end date",
                        value = """
                            {
                              "productId": "550e8400-e29b-41d4-a716-446655440000",
                              "productCode": "SAV-HIGH-YIELD-2025",
                              "productName": "High Yield Savings Account",
                              "productType": "SAVINGS",
                              "productDescription": "Premium savings account (discontinued)",
                              "currency": "USD",
                              "status": "INACTIVE",
                              "interestType": "COMPOUND",
                              "compoundingFrequency": "DAILY",
                              "effectiveStartDate": "2025-01-01",
                              "effectiveEndDate": "2025-12-31",
                              "createdAt": "2025-01-01T00:00:00",
                              "updatedAt": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Interest Config Update",
                        summary = "Changed from quarterly to monthly compounding",
                        value = """
                            {
                              "productId": "660e8400-e29b-41d4-a716-446655440111",
                              "productCode": "FD-1Y-2025",
                              "productName": "1 Year Fixed Deposit - Enhanced",
                              "productType": "DEPOSIT",
                              "productDescription": "Now with monthly compounding for better returns",
                              "currency": "USD",
                              "status": "ACTIVE",
                              "interestType": "COMPOUND",
                              "compoundingFrequency": "MONTHLY",
                              "effectiveStartDate": "2025-01-01",
                              "effectiveEndDate": null,
                              "createdAt": "2025-01-01T00:00:00",
                              "updatedAt": "2025-10-15T10:35:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Enum Value",
                        value = """
                            {
                              "error": "Validation Failed",
                              "message": "Invalid interestType value: INVALID. Must be one of: SIMPLE, COMPOUND",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Invalid Date Range",
                        value = """
                            {
                              "error": "Validation Failed",
                              "message": "effectiveEndDate must be after effectiveStartDate",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Product Code Mismatch",
                        value = """
                            {
                              "error": "Validation Failed",
                              "message": "Product code in path (SAV-HIGH-YIELD-2025) does not match request body (SAV-OLD-2025)",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Resource Not Found",
                          "message": "Product not found with code: SAV-INVALID-2025",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Internal Server Error",
                          "message": "Failed to update product",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<ProductDetailsDTO> updateProduct(
            @Parameter(
                description = """
                    Product code identifier (must match code in request body).
                    
                    Example: SAV-HIGH-YIELD-2025
                    """,
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Updated product details (complete object with changes).
                    
                    Required fields:
                    - productCode (must match path parameter)
                    - productName
                    - productType
                    
                    Optional fields:
                    - productDescription
                    - status
                    - interestType
                    - compoundingFrequency
                    - effectiveStartDate
                    - effectiveEndDate
                    
                    See request examples for common update scenarios.
                    """,
                required = true
            )
            @Valid @RequestBody CreateOrUpdateProductRequestDTO requestDTO) {
        return ResponseEntity.ok(productService.updateProduct(productCode, requestDTO));
    }

    @DeleteMapping("/{productCode}")
    @Operation(
        summary = "Delete a product (permanent removal)",
        description = """
            Permanently remove a product and ALL related data from the system. This is a destructive operation
            that cannot be undone.
            
            **What gets deleted:**
            - Product core details
            - All interest rate tiers
            - All fee and charge configurations
            - All balance type mappings
            - All business rules
            - All transaction configurations
            - All role assignments
            - All communication templates
            
            **⚠️ IMPORTANT WARNINGS:**
            - This operation is **IRREVERSIBLE**
            - All related data is **PERMANENTLY DELETED**
            - Existing customer accounts using this product are NOT affected
            - Audit trail for past transactions is maintained
            - Cannot delete if active accounts exist (future enhancement)
            
            **Recommended Alternative:**
            Instead of deletion, consider:
            1. Set status to INACTIVE using PUT /api/products/{code}
            2. Set effectiveEndDate to retire product gracefully
            3. This preserves audit trail and historical data
            
            **Use Cases:**
            
            **Scenario 1: Test Data Cleanup**
            - Remove test products from development environment
            - Clean up incorrectly created products
            - Reset demo data
            
            **Scenario 2: Duplicate Product Removal**
            - Remove accidentally created duplicates
            - Delete products created in error
            - Clean up failed migrations
            
            **Scenario 3: Compliance Requirement**
            - GDPR/data deletion requests (rare)
            - Legal requirement to remove product
            - Must be documented and approved
            
            **❌ WRONG USE CASES:**
            - Retiring a product → Use status=INACTIVE instead
            - Temporary product suspension → Use status=INACTIVE
            - Product no longer offered → Set effectiveEndDate
            
            **Related Endpoints:**
            - PUT /api/products/{code} - Update status to INACTIVE (preferred)
            - GET /api/products/{code} - Verify product before deletion
            - POST /api/products - Create replacement product
            
            **Production Best Practices:**
            1. Always verify product code before deletion
            2. Export product configuration as backup
            3. Check for active accounts (manual verification)
            4. Document reason for deletion
            5. Get proper authorization
            6. Consider soft delete (INACTIVE status) instead
            """,
        tags = {"Product Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Product and all related data successfully deleted. No content returned.",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found (may already be deleted)",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Product Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Product not found with code: SAV-INVALID-2025",
                              "timestamp": "2025-10-15T10:30:00",
                              "path": "/api/products/SAV-INVALID-2025"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Cannot delete product (future: has active accounts)",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Has Active Accounts",
                        value = """
                            {
                              "error": "Conflict",
                              "message": "Cannot delete product SAV-HIGH-YIELD-2025: 1,523 active accounts exist. Set status to INACTIVE instead.",
                              "timestamp": "2025-10-15T10:30:00",
                              "activeAccountCount": 1523
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error during deletion",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Internal Server Error",
                          "message": "Failed to delete product: database constraint violation",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(
                description = """
                    Product code to permanently delete.
                    
                    ⚠️ WARNING: This cannot be undone!
                    
                    Example: SAV-HIGH-YIELD-2025
                    
                    Verify product code carefully before execution.
                    """,
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode) {
        productService.deleteProduct(productCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(
        summary = "Advanced product search with multiple filters",
        description = """
            Search for products using flexible filtering criteria. All parameters are optional and can be combined
            for precise results.
            
            **Filter Options:**
            - **productType**: Filter by product category (SAVINGS, LOAN, DEPOSIT, etc.)
            - **status**: Filter by lifecycle status (ACTIVE, INACTIVE, PENDING)
            - **Date Range**: Filter by effective date period (startDate + endDate)
            
            **Filter Combinations:**
            - No parameters → Returns all products
            - Single parameter → Filter by that criterion only
            - Multiple parameters → AND logic (all criteria must match)
            
            **Date Range Logic:**
            - Products where effectiveStartDate >= startDate
            - Products where effectiveEndDate <= endDate (or null)
            - Finds products active during the specified period
            
            **Use Cases:**
            
            **Scenario 1: Find All Active Savings Products**
            - productType=SAVINGS
            - status=ACTIVE
            - Use for customer product selection
            
            **Scenario 2: Find Products Launched This Year**
            - startDate=2025-01-01
            - endDate=2025-12-31
            - Review new product launches
            
            **Scenario 3: Find Inactive Loan Products**
            - productType=LOAN
            - status=INACTIVE
            - Identify discontinued loan offerings
            
            **Scenario 4: Find Current Active Products**
            - status=ACTIVE
            - No date filters
            - Get all currently offered products
            
            **Scenario 5: Product Type Comparison**
            - productType=DEPOSIT
            - Compare all deposit products
            - View interest rates across FD variants
            
            **Related Endpoints:**
            - GET /api/products - Get all products with pagination
            - GET /api/products/{code} - Get specific product details
            - POST /api/products - Create new product
            
            **Performance Notes:**
            - Indexed on productType and status
            - Date range queries may be slower on large datasets
            - Consider pagination for large result sets
            - Results are not paginated (returns all matches)
            """,
        tags = {"Product Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = List.class),
                examples = {
                    @ExampleObject(
                        name = "Active Savings Products",
                        summary = "Search result: Active savings products",
                        value = """
                            [
                              {
                                "productId": "550e8400-e29b-41d4-a716-446655440000",
                                "productCode": "SAV-HIGH-YIELD-2025",
                                "productName": "High Yield Savings Account",
                                "productType": "SAVINGS",
                                "currency": "USD",
                                "status": "ACTIVE",
                                "interestType": "COMPOUND",
                                "compoundingFrequency": "DAILY",
                                "effectiveStartDate": "2025-01-01",
                                "effectiveEndDate": null,
                                "createdAt": "2025-01-01T00:00:00"
                              },
                              {
                                "productId": "551e8400-e29b-41d4-a716-446655440001",
                                "productCode": "SAV-REGULAR-2025",
                                "productName": "Regular Savings Account",
                                "productType": "SAVINGS",
                                "currency": "USD",
                                "status": "ACTIVE",
                                "interestType": "SIMPLE",
                                "compoundingFrequency": null,
                                "effectiveStartDate": "2025-01-01",
                                "effectiveEndDate": null,
                                "createdAt": "2025-01-01T00:00:00"
                              }
                            ]
                            """
                    ),
                    @ExampleObject(
                        name = "Date Range Search",
                        summary = "Products launched in Q1 2025",
                        value = """
                            [
                              {
                                "productId": "660e8400-e29b-41d4-a716-446655440111",
                                "productCode": "FD-1Y-2025",
                                "productName": "1 Year Fixed Deposit",
                                "productType": "DEPOSIT",
                                "currency": "USD",
                                "status": "ACTIVE",
                                "interestType": "COMPOUND",
                                "compoundingFrequency": "QUARTERLY",
                                "effectiveStartDate": "2025-01-15",
                                "effectiveEndDate": null,
                                "createdAt": "2025-01-15T00:00:00"
                              },
                              {
                                "productId": "661e8400-e29b-41d4-a716-446655440112",
                                "productCode": "FD-3Y-2025",
                                "productName": "3 Year Fixed Deposit",
                                "productType": "DEPOSIT",
                                "currency": "USD",
                                "status": "ACTIVE",
                                "interestType": "COMPOUND",
                                "compoundingFrequency": "QUARTERLY",
                                "effectiveStartDate": "2025-02-01",
                                "effectiveEndDate": null,
                                "createdAt": "2025-02-01T00:00:00"
                              }
                            ]
                            """
                    ),
                    @ExampleObject(
                        name = "No Results",
                        summary = "Search with no matches",
                        value = "[]"
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid search parameters",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Product Type",
                        value = """
                            {
                              "error": "Validation Failed",
                              "message": "Invalid productType: INVALID_TYPE. Valid values: SAVINGS, CURRENT, LOAN, CREDIT_CARD, FIXED_DEPOSIT, RECURRING_DEPOSIT",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Invalid Date Format",
                        value = """
                            {
                              "error": "Validation Failed",
                              "message": "Invalid date format for startDate: '2025/01/01'. Expected format: YYYY-MM-DD",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Invalid Date Range",
                        value = """
                            {
                              "error": "Validation Failed",
                              "message": "endDate (2025-01-01) must be after startDate (2025-12-31)",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Internal Server Error",
                          "message": "Failed to execute search query",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<List<ProductDetailsDTO>> searchProducts(
            @Parameter(
                description = """
                    Filter by product type (optional).
                    
                    Valid values:
                    - SAVINGS
                    - CURRENT
                    - LOAN
                    - CREDIT_CARD
                    - FIXED_DEPOSIT
                    - RECURRING_DEPOSIT
                    
                    Case-insensitive. Omit to search all types.
                    """,
                example = "SAVINGS"
            )
            @RequestParam(required = false) String productType,
            @Parameter(
                description = """
                    Filter by status (optional).
                    
                    Valid values:
                    - ACTIVE (currently offered)
                    - INACTIVE (discontinued)
                    - PENDING (awaiting approval)
                    - SUSPENDED (temporarily unavailable)
                    - CLOSED (permanently unavailable)
                    
                    Case-insensitive. Omit to search all statuses.
                    """,
                example = "ACTIVE"
            )
            @RequestParam(required = false) String status,
            @Parameter(
                description = """
                    Start date for effective date range (optional).
                    
                    Format: YYYY-MM-DD (ISO-8601)
                    Example: 2025-01-01
                    
                    Must be used with endDate parameter.
                    Finds products effective from this date onward.
                    """,
                example = "2025-01-01"
            )
            @RequestParam(required = false) String startDate,
            @Parameter(
                description = """
                    End date for effective date range (optional).
                    
                    Format: YYYY-MM-DD (ISO-8601)
                    Example: 2025-12-31
                    
                    Must be used with startDate parameter.
                    Must be >= startDate.
                    Finds products effective until this date.
                    """,
                example = "2025-12-31"
            )
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(productService.searchProducts(productType, status, startDate, endDate));
    }
}
