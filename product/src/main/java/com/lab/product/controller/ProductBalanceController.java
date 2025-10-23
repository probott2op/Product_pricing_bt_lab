package com.lab.product.controller;

import com.lab.product.DTO.ProductBalanceDTO;
import com.lab.product.DTO.ProductBalanceRequestDTO;
import com.lab.product.service.ProductBalanceService;
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

@RestController
@RequestMapping("/api/products/{productCode}/balances")
@RequiredArgsConstructor
@Tag(
    name = "Product Balance Types",
    description = "API for defining which balance types are supported/applicable for a financial product. " +
                  "For example, a Loan product would have: LOAN_PRINCIPAL, LOAN_INTEREST, OVERDRAFT, PENALTY. " +
                  "A Fixed Deposit product would have: FD_PRINCIPAL, FD_INTEREST. " +
                  "This simply indicates which balance types are relevant for the product."
)
public class ProductBalanceController {

    @Autowired
    private final ProductBalanceService productBalanceService;

    @PostMapping
    @Operation(
        summary = "Configure applicable balance type for product",
        description = """
            Mark a specific balance type as applicable/supported for this product. This defines which
            balance categories can be tracked for accounts using this product.
            
            **Available Balance Types:**
            - **LOAN_PRINCIPAL**: Main loan amount borrowed
            - **LOAN_INTEREST**: Accrued interest on loan
            - **FD_PRINCIPAL**: Fixed deposit principal amount
            - **FD_INTEREST**: Interest earned on FD
            - **OVERDRAFT**: Overdraft/credit facility amount
            - **PENALTY**: Penalty charges and fees
            
            **Product Type Examples:**
            
            **Personal Loan:**
            - LOAN_PRINCIPAL (required)
            - LOAN_INTEREST (required)
            - PENALTY (optional for late fees)
            
            **Fixed Deposit:**
            - FD_PRINCIPAL (required)
            - FD_INTEREST (required)
            
            **Savings Account with OD:**
            - FD_PRINCIPAL (for savings balance)
            - FD_INTEREST (for interest earned)
            - OVERDRAFT (for overdraft facility)
            - PENALTY (for overdraft charges)
            
            **Current Account:**
            - FD_PRINCIPAL (for current balance)
            - OVERDRAFT (if overdraft facility available)
            - PENALTY (for service charges)
            
            **Use Cases:**
            
            **Scenario 1: Configure Loan Product**
            - Add LOAN_PRINCIPAL to track loan amount
            - Add LOAN_INTEREST to track interest due
            - Add PENALTY to track late payment fees
            
            **Scenario 2: Configure FD Product**
            - Add FD_PRINCIPAL to track deposit amount
            - Add FD_INTEREST to track interest earned
            - Simple 2-balance configuration
            
            **Scenario 3: Enable Overdraft Feature**
            - Existing savings product
            - Add OVERDRAFT balance type
            - Add PENALTY for overdraft charges
            
            **Important Notes:**
            - Each balance type can only be added once per product
            - Balance types define structure, not actual amounts
            - Actual balances are tracked at account level
            - isActive flag can disable without removing
            
            **Related Endpoints:**
            - GET /api/products/{code}/balances - View all configured types
            - PUT /api/products/{code}/balances/{type} - Update balance config
            - DELETE /api/products/{code}/balances/{type} - Remove balance type
            """,
        tags = {"Product Balance Types"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Balance type added successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductBalanceDTO.class),
                examples = {
                    @ExampleObject(
                        name = "FD001 Principal Added",
                        summary = "Added FD_PRINCIPAL to FD001 product",
                        value = """
                            {
                              "balanceId": "4deff13c-a7de-4a01-b116-fae6f9ea6e17",
                              "balanceCode": "FD_PRINCIPAL",
                              "balanceType": "FD_PRINCIPAL",
                              "isActive": true,
                              "createdAt": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "FD001 Interest Added",
                        summary = "Added FD_INTEREST to FD001 product",
                        value = """
                            {
                              "balanceId": "4b0da79d-87d4-454a-bcdf-c0c9b06c0c16",
                              "balanceCode": "FD_INTEREST",
                              "balanceType": "FD_INTEREST",
                              "isActive": true,
                              "createdAt": "2025-10-15T10:31:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Loan Principal Added",
                        summary = "Added LOAN_PRINCIPAL to loan product",
                        value = """
                            {
                              "balanceId": "881e8400-e29b-41d4-a716-446655440001",
                              "balanceType": "LOAN_PRINCIPAL",
                              "isActive": true,
                              "createdAt": "2025-10-15T10:30:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request or duplicate balance type",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Duplicate Balance Type",
                        value = """
                            {
                              "error": "Conflict",
                              "message": "Balance type LOAN_PRINCIPAL already exists for product LOAN-PERSONAL-5Y",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Invalid Balance Type",
                        value = """
                            {
                              "error": "Validation Failed",
                              "message": "Invalid balanceType: INVALID_TYPE. Must be one of: LOAN_PRINCIPAL, LOAN_INTEREST, FD_PRINCIPAL, FD_INTEREST, OVERDRAFT, PENALTY",
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
                          "message": "Product not found with code: LOAN-INVALID-2025",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<ProductBalanceDTO> addBalance(
            @Parameter(
                description = """
                    Product code to add balance type to.
                    
                    Example: LOAN-PERSONAL-5Y
                    """,
                required = true,
                example = "LOAN-PERSONAL-5Y"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Balance type configuration.
                    
                    Required:
                    - balanceType: One of the 6 available types
                    
                    Optional:
                    - isActive: Enable/disable (default: true)
                    """,
                required = true
            )
            @Valid @RequestBody ProductBalanceRequestDTO balanceDto) {
        return new ResponseEntity<>(productBalanceService.addBalanceToProduct(productCode, balanceDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "Retrieve all balance types configured for product",
        description = """
            Get a complete list of balance types that are applicable/supported for this product.
            Shows which balance categories can be tracked for accounts using this product.
            
            **What it returns:**
            - All configured balance types for the product
            - Active/inactive status per type
            - Creation timestamps
            
            **Use Cases:**
            
            **Scenario 1: Account Opening**
            - System retrieves applicable balance types
            - Creates balance entries for new account
            - Initializes all required balance categories
            
            **Scenario 2: Product Configuration Review**
            - Admin reviews balance structure
            - Verifies all necessary types configured
            - Checks for missing balance categories
            
            **Scenario 3: Balance Report Generation**
            - Determine which balances to display
            - Show only applicable balance types
            - Hide non-configured categories
            
            **Scenario 4: Product Comparison**
            - Compare balance structures across products
            - Identify differences in tracking
            - Standardize configurations
            
            **Related Endpoints:**
            - POST /api/products/{code}/balances - Add new balance type
            - GET /api/products/{code}/balances/{type} - Get specific type
            - GET /api/products/{code} - View complete product config
            """,
        tags = {"Product Balance Types"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Balance types retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class),
                examples = {
                    @ExampleObject(
                        name = "FD001 Balance Types",
                        summary = "Fixed Deposit under 500000 - Principal, Interest, and Penalty balances",
                        value = """
                            {
                              "content": [
                                {
                                  "balanceId": "88b934e7-9688-49c1-9c59-0fbff87be971",
                                  "balanceType": "FD_PRINCIPAL",
                                  "isActive": true,
                                  "createdAt": "2025-10-19T18:45:23.47411"
                                },
                                {
                                  "balanceId": "1360a975-0102-40d1-87d9-047c927dc794",
                                  "balanceType": "FD_INTEREST",
                                  "isActive": true,
                                  "createdAt": "2025-10-19T18:44:59.9126"
                                },
                                {
                                  "balanceId": "bbb417ac-fd89-4243-a995-47746d0980b7",
                                  "balanceType": "PENALTY",
                                  "isActive": true,
                                  "createdAt": "2025-10-19T18:45:40.10681"
                                }
                              ],
                              "totalElements": 3,
                              "totalPages": 1,
                              "number": 0,
                              "size": 20
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Loan Product Balances",
                        summary = "3 balance types for personal loan",
                        value = """
                            {
                              "content": [
                                {
                                  "balanceId": "881e8400-e29b-41d4-a716-446655440001",
                                  "balanceType": "LOAN_PRINCIPAL",
                                  "isActive": true,
                                  "createdAt": "2025-01-01T00:00:00"
                                },
                                {
                                  "balanceId": "882e8400-e29b-41d4-a716-446655440002",
                                  "balanceType": "LOAN_INTEREST",
                                  "isActive": true,
                                  "createdAt": "2025-01-01T00:00:00"
                                },
                                {
                                  "balanceId": "883e8400-e29b-41d4-a716-446655440003",
                                  "balanceType": "PENALTY",
                                  "isActive": true,
                                  "createdAt": "2025-01-01T00:00:00"
                                }
                              ],
                              "totalElements": 3,
                              "totalPages": 1,
                              "number": 0,
                              "size": 20
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Savings Product Balances",
                        summary = "2 balance types for savings account",
                        value = """
                            {
                              "content": [
                                {
                                  "balanceId": "884e8400-e29b-41d4-a716-446655440004",
                                  "balanceType": "AVAILABLE_BALANCE",
                                  "isActive": true,
                                  "createdAt": "2025-01-01T00:00:00"
                                },
                                {
                                  "balanceId": "885e8400-e29b-41d4-a716-446655440005",
                                  "balanceType": "LEDGER_BALANCE",
                                  "isActive": true,
                                  "createdAt": "2025-01-01T00:00:00"
                                }
                              ],
                              "totalElements": 2,
                              "totalPages": 1,
                              "number": 0,
                              "size": 20
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
                          "message": "Product not found with code: LOAN-INVALID-2025",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<Page<ProductBalanceDTO>> getBalances(
            @Parameter(
                description = "Product code to retrieve balance types for",
                required = true,
                example = "LOAN-PERSONAL-5Y"
            )
            @PathVariable String productCode,
            @Parameter(
                description = "Pagination parameters",
                example = "page=0&size=20"
            )
            Pageable pageable) {
        return ResponseEntity.ok(productBalanceService.getBalancesForProduct(productCode, pageable));
    }

    @GetMapping("/{balanceType}")
    @Operation(
        summary = "Retrieve specific balance type configuration",
        description = """
            Get details of a specific balance type if it's configured for this product.
            
            **What it returns:**
            - Balance type identifier (enum value)
            - Active/inactive status
            - Balance ID (UUID)
            - Configuration timestamp
            
            **Use Cases:**
            
            **Scenario 1: Verify Balance Type Exists**
            - Check if product supports specific balance
            - Validate before creating account
            - Confirm balance type configuration
            
            **Scenario 2: Get Balance Type Details**
            - Retrieve balance configuration
            - Check active status
            - View creation timestamp
            
            **Scenario 3: Before Update Operations**
            - Get current configuration
            - Verify balance type still exists
            - Prepare for modifications
            
            **Related Endpoints:**
            - GET /api/products/{code}/balances - View all types
            - PUT /api/products/{code}/balances/{type} - Update this type
            - DELETE /api/products/{code}/balances/{type} - Remove this type
            """,
        tags = {"Product Balance Types"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Balance type found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductBalanceDTO.class),
                examples = @ExampleObject(
                    value = """
                        {
                          "balanceId": "881e8400-e29b-41d4-a716-446655440001",
                          "balanceType": "LOAN_PRINCIPAL",
                          "isActive": true,
                          "createdAt": "2025-01-01T00:00:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Balance type or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Balance Type Not Configured",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Balance type OVERDRAFT not configured for product LOAN-PERSONAL-5Y",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Product Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Product not found with code: LOAN-INVALID-2025",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ProductBalanceDTO> getBalanceByType(
            @Parameter(
                description = "Product code",
                required = true,
                example = "LOAN-PERSONAL-5Y"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Balance type to retrieve.
                    
                    Valid values:
                    - LOAN_PRINCIPAL
                    - LOAN_INTEREST
                    - FD_PRINCIPAL
                    - FD_INTEREST
                    - OVERDRAFT
                    - PENALTY
                    """,
                required = true,
                example = "LOAN_PRINCIPAL"
            )
            @PathVariable String balanceType) {
        return ResponseEntity.ok(productBalanceService.getBalanceByType(productCode, balanceType));
    }

    @PutMapping("/{balanceType}")
    @Operation(
        summary = "Update balance type configuration",
        description = """
            Modify the configuration of an existing balance type for this product.
            
            **What can be updated:**
            - Balance type (change to different type)
            - Active/inactive status
            
            **Common Update Scenarios:**
            
            **Scenario 1: Disable Balance Type**
            - Temporarily disable without removing
            - Set isActive = false
            - Preserve configuration for reactivation
            
            **Scenario 2: Enable Previously Disabled**
            - Reactivate disabled balance type
            - Set isActive = true
            - Resume balance tracking
            
            **Scenario 3: Change Balance Type**
            - Incorrect type was configured
            - Switch from LOAN_PRINCIPAL to FD_PRINCIPAL
            - Correct configuration error
            
            **Important Notes:**
            - Changing balance type may affect existing accounts
            - Disabling preserves configuration without deletion
            - Cannot change if accounts actively using this balance
            - Consider migration path for existing accounts
            
            **Related Endpoints:**
            - GET /api/products/{code}/balances/{type} - View current config
            - DELETE /api/products/{code}/balances/{type} - Remove type
            - POST /api/products/{code}/balances - Add new type
            """,
        tags = {"Product Balance Types"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Balance type updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductBalanceDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Disabled Balance Type",
                        summary = "Temporarily disabled OVERDRAFT balance",
                        value = """
                            {
                              "balanceId": "883e8400-e29b-41d4-a716-446655440003",
                              "balanceType": "OVERDRAFT",
                              "isActive": false,
                              "createdAt": "2025-01-01T00:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Changed Balance Type",
                        summary = "Changed from LOAN to FD balance type",
                        value = """
                            {
                              "balanceId": "881e8400-e29b-41d4-a716-446655440001",
                              "balanceType": "FD_PRINCIPAL",
                              "isActive": true,
                              "createdAt": "2025-01-01T00:00:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid balance type data",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Validation Failed",
                          "message": "Invalid balanceType: INVALID_TYPE. Must be one of: LOAN_PRINCIPAL, LOAN_INTEREST, FD_PRINCIPAL, FD_INTEREST, OVERDRAFT, PENALTY",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Balance type or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Resource Not Found",
                          "message": "Balance type OVERDRAFT not configured for product LOAN-PERSONAL-5Y",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<ProductBalanceDTO> updateBalance(
            @Parameter(
                description = "Product code",
                required = true,
                example = "LOAN-PERSONAL-5Y"
            )
            @PathVariable String productCode,
            @Parameter(
                description = "Current balance type to update",
                required = true,
                example = "OVERDRAFT"
            )
            @PathVariable String balanceType,
            @Parameter(
                description = """
                    Updated balance type configuration.
                    
                    Can change:
                    - balanceType: Switch to different type
                    - isActive: Enable/disable balance
                    """,
                required = true
            )
            @Valid @RequestBody ProductBalanceRequestDTO balanceDto) {
        return ResponseEntity.ok(productBalanceService.updateBalance(productCode, balanceType, balanceDto));
    }

    @DeleteMapping("/{balanceType}")
    @Operation(
        summary = "Remove balance type from product (permanent)",
        description = """
            Permanently remove a balance type from product configuration. This marks the balance type
            as no longer applicable for this product.
            
            **⚠️ WARNING: This operation is IRREVERSIBLE**
            
            **What gets deleted:**
            - Balance type configuration for product
            - Balance type mapping
            
            **What is NOT affected:**
            - Existing account balances (preserved)
            - Historical balance data
            - Transaction history
            - Audit trail
            
            **When to Delete:**
            - Incorrectly configured balance type
            - Product structure simplification
            - Balance type no longer needed
            - Configuration error correction
            
            **Recommended Alternative:**
            Instead of deletion, consider:
            1. Set isActive = false using PUT endpoint
            2. Preserves configuration and history
            3. Can be reactivated if needed
            4. Maintains audit trail
            
            **Common Use Cases:**
            
            **Scenario 1: Remove Unused Balance Type**
            - OVERDRAFT added but never used
            - Product doesn't offer overdraft
            - Clean up configuration
            
            **Scenario 2: Product Restructuring**
            - Changing product balance structure
            - Removing obsolete balance categories
            - Simplifying balance tracking
            
            **Scenario 3: Configuration Cleanup**
            - Test data removal
            - Duplicate balance types
            - Error correction
            
            **❌ WRONG USE CASES:**
            - Temporarily stopping balance tracking → Use isActive=false
            - Balance type restructuring → Use PUT to change type
            - Account closure → Leave configuration intact
            
            **Important Validation:**
            - Verify no active accounts using this balance
            - Check for dependent configurations
            - Document reason for removal
            - Backup configuration if needed
            
            **Related Endpoints:**
            - PUT /api/products/{code}/balances/{type} - Update (preferred)
            - GET /api/products/{code}/balances - View remaining types
            - POST /api/products/{code}/balances - Add replacement type
            """,
        tags = {"Product Balance Types"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Balance type removed successfully. No content returned."
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Balance type or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Balance Type Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Balance type OVERDRAFT not configured for product LOAN-PERSONAL-5Y",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Product Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Product not found with code: LOAN-INVALID-2025",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Cannot delete balance type (has active accounts)",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Conflict",
                          "message": "Cannot delete balance type LOAN_PRINCIPAL: 523 accounts actively using this balance. Set isActive=false instead.",
                          "timestamp": "2025-10-15T10:30:00",
                          "affectedAccounts": 523
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
                          "message": "Failed to delete balance type",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<Void> deleteBalance(
            @Parameter(
                description = "Product code",
                required = true,
                example = "LOAN-PERSONAL-5Y"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Balance type to permanently remove.
                    
                    ⚠️ WARNING: This cannot be undone!
                    
                    Consider using PUT with isActive=false instead.
                    
                    Valid values:
                    - LOAN_PRINCIPAL
                    - LOAN_INTEREST
                    - FD_PRINCIPAL
                    - FD_INTEREST
                    - OVERDRAFT
                    - PENALTY
                    """,
                required = true,
                example = "OVERDRAFT"
            )
            @PathVariable String balanceType) {
        productBalanceService.deleteBalance(productCode, balanceType);
        return ResponseEntity.noContent().build();
    }
}