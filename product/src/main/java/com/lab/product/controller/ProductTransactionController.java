package com.lab.product.controller;

import com.lab.product.DTO.ProductTransactionDTO;
import com.lab.product.DTO.ProductTransactionRequestDTO;
import com.lab.product.service.ProductTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/products/{productCode}/transactions")
@RequiredArgsConstructor
@Tag(
    name = "Product Transaction Types",
    description = "API for managing allowed transaction types and transaction configuration for products. " +
                  "Transaction configurations define which operations are permitted on a product (DEPOSIT, WITHDRAWAL, " +
                  "TRANSFER, PAYMENT, etc.), transaction limits, processing rules, and channel availability " +
                  "(ATM, ONLINE, BRANCH, MOBILE). Each transaction type includes minimum/maximum amount limits, " +
                  "daily limits, and frequency restrictions. Essential for channel management, fraud prevention, " +
                  "and customer service optimization."
)
public class ProductTransactionController {

    @Autowired
    private final ProductTransactionService productTransactionService;

    @PostMapping
    @Operation(
        summary = "Configure allowed transaction type for product",
        description = """
            Add a new transaction type configuration to a product, defining what operations are allowed.
            
            **Transaction Types Supported:**
            - **DEPOSIT** - Cash or check deposits, credit transactions
            - **WITHDRAWAL** - Cash withdrawals, debit transactions
            - **INTEREST_ACCRUED** - Automatic interest posting/calculation
            - **DISBURSEMENT** - Loan disbursements, fund releases
            - **PAYMENT** - Bill payments, loan repayments
            
            **Configuration Includes:**
            - Unique transaction code (identifier)
            - Transaction name (display name)
            - Transaction type (enum value)
            - Debit/Credit indicator (D for debit, C for credit)
            - Minimum transaction amount
            - Maximum transaction amount
            - Description and notes
            - Active/inactive status
            
            **Use Cases:**
            
            **Scenario 1: Savings Account Setup**
            Configure basic transaction types:
            - DEPOSIT (credit): min ₹1,000, max ₹1,0000,000
            - WITHDRAWAL (debit): min ₹2,000, max ₹2,00,000 per transaction
            - INTEREST_ACCRUED (credit): automated monthly interest posting
            
            **Scenario 2: Fixed Deposit Product**
            Configure limited transaction types:
            - DEPOSIT allowed only (initial investment)
            - WITHDRAWAL restricted (until maturity)
            - INTEREST_ACCRUED enabled for automatic interest
            
            **Scenario 3: Loan Product**
            Configure loan-specific transactions:
            - DISBURSEMENT (debit): one-time loan amount
            - PAYMENT (credit): regular repayments
            - INTEREST_ACCRUED (debit): interest calculations
            
            **Scenario 4: Current Account**
            Full transaction capabilities:
            - DEPOSIT: unlimited frequency, high limits
            - WITHDRAWAL: high daily limits
            - PAYMENT: bill payments enabled
            
            **Scenario 5: Minor Account (Restricted)**
            Limited transaction types:
            - DEPOSIT only (no withdrawals)
            - Lower transaction limits
            - Parental approval required
            
            **Related Endpoints:**
            - GET /api/products/{code}/transactions - View all transaction configurations
            - PUT /api/products/{code}/transactions/{code} - Update transaction config
            - DELETE /api/products/{code}/transactions/{code} - Remove transaction type
            """,
        tags = {"Product Transaction Types"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Transaction type configured successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductTransactionDTO.class),
                examples = {
                    @ExampleObject(
                        name = "FD001 Deposit Configuration",
                        summary = "Fixed Deposit initial deposit transaction",
                        value = """
                            {
                              "transactionId": "3e6dcd5d-1615-4e93-8cd3-dc9a2bf8713f",
                              "transactionCode": "FD_DEPOSIT",
                              "transactionType": "DEPOSIT",
                              "amountLimit": null
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Savings Withdrawal Configuration",
                        summary = "Cash withdrawal transaction type",
                        value = """
                            {
                              "transactionId": "991e8400-e29b-41d4-a716-446655440001",
                              "transactionCode": "TXN_WITHDRAWAL",
                              "transactionType": "WITHDRAWAL",
                              "amountLimit": 500.00
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Interest Accrual Configuration",
                        summary = "Automatic interest posting",
                        value = """
                            {
                              "transactionId": "993e8400-e29b-41d4-a716-446655440003",
                              "transactionCode": "TXN_INTEREST_ACCRUED",
                              "transactionType": "INTEREST_ACCRUED",
                              "amountLimit": null
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
                    name = "Product Not Found",
                    value = """
                        {
                          "error": "Resource Not Found",
                          "message": "Product not found with code: SAV-INVALID-2025",
                          "timestamp": "2025-10-18T10:00:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid transaction configuration",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Missing Required Field",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Transaction type is required",
                              "timestamp": "2025-10-18T10:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Invalid Amount Range",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Maximum amount must be greater than minimum amount",
                              "timestamp": "2025-10-18T10:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Duplicate Transaction Type",
                        value = """
                            {
                              "error": "Duplicate Entry",
                              "message": "Transaction type 'WITHDRAWAL' with channel 'ATM' already exists for this product",
                              "timestamp": "2025-10-18T10:00:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ProductTransactionDTO> addTransaction(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = """
                    Transaction type configuration.
                    
                    Required fields:
                    - transactionCode: Unique identifier (e.g., "TXN_WITHDRAWAL")
                    - transactionName: Display name
                    - transactionType: DEPOSIT, WITHDRAWAL, INTEREST_ACCRUED, DISBURSEMENT, or PAYMENT
                    - debitCreditIndicator: "D" for debit, "C" for credit
                    - minimumAmount: Minimum transaction amount
                    - maximumAmount: Maximum transaction amount
                    
                    Optional:
                    - description: Additional notes
                    - isActive: Enable/disable transaction type (default: true)
                    """,
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductTransactionRequestDTO.class),
                    examples = {
                        @ExampleObject(
                            name = "Withdrawal Configuration",
                            summary = "Standard withdrawal with limits",
                            value = """
                                {
                                  "transactionCode": "TXN_WITHDRAWAL",
                                  "transactionName": "Account Withdrawal",
                                  "transactionType": "WITHDRAWAL",
                                  "debitCreditIndicator": "D",
                                  "minimumAmount": 20.00,
                                  "maximumAmount": 2000.00,
                                  "description": "Standard cash withdrawal from account",
                                  "isActive": true
                                }
                                """
                        ),
                        @ExampleObject(
                            name = "Deposit Configuration",
                            summary = "High-limit deposit capability",
                            value = """
                                {
                                  "transactionCode": "TXN_DEPOSIT",
                                  "transactionName": "Account Deposit",
                                  "transactionType": "DEPOSIT",
                                  "debitCreditIndicator": "C",
                                  "minimumAmount": 10.00,
                                  "maximumAmount": 100000.00,
                                  "description": "Cash or check deposit into account",
                                  "isActive": true
                                }
                                """
                        ),
                        @ExampleObject(
                            name = "Interest Accrual",
                            summary = "Automatic interest posting",
                            value = """
                                {
                                  "transactionCode": "TXN_INTEREST_ACCRUED",
                                  "transactionName": "Interest Posting",
                                  "transactionType": "INTEREST_ACCRUED",
                                  "debitCreditIndicator": "C",
                                  "minimumAmount": 0.01,
                                  "maximumAmount": 999999.99,
                                  "description": "Automatic monthly interest accrual",
                                  "isActive": true
                                }
                                """
                        )
                    }
                )
            )
            @Valid @RequestBody ProductTransactionRequestDTO transactionDto) {
        return new ResponseEntity<>(productTransactionService.addTransactionToProduct(productCode, transactionDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "Retrieve all transaction configurations for product",
        description = """
            Get a paginated list of all transaction types configured for a specific product.
            
            **What You Get:**
            Complete list of configured transaction types with:
            - Transaction codes and types
            - Amount limits for each transaction
            - Transaction identifiers
            
            **Use Cases:**
            
            **Scenario 1: Product Setup Verification**
            After configuring new product:
            - Review all allowed transaction types
            - Verify DEPOSIT and WITHDRAWAL are configured
            - Check if INTEREST_ACCRUED is enabled
            - Ensure amount limits are correct
            
            **Scenario 2: Customer Service**
            Customer asks "What transactions can I perform?":
            - Show allowed transaction types
            - Explain DEPOSIT vs WITHDRAWAL
            - Clarify PAYMENT capabilities
            - Identify DISBURSEMENT options (for loans)
            
            **Scenario 3: Compliance Audit**
            Regulatory review requires:
            - Document all transaction types
            - Verify transaction limits
            - Check which operations are enabled
            - Validate configurations
            
            **Scenario 4: Product Comparison**
            Compare transaction capabilities:
            - Basic vs Premium accounts
            - Savings vs Current accounts
            - Loan vs Deposit products
            
            **Scenario 5: Transaction Matrix Review**
            Analyze product capabilities:
            - Which transaction types are configured
            - What are the amount limits
            - Are all necessary types present
            
            **Response Includes:**
            - Transaction configurations
            - Pagination information
            - Total count of transaction types
            
            **Related Endpoints:**
            - GET /api/products/{code}/transactions/{code} - View specific transaction
            - POST /api/products/{code}/transactions - Add new transaction type
            - PUT /api/products/{code}/transactions/{code} - Update transaction
            """,
        tags = {"Product Transaction Types"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Transaction configurations retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "FD001 Transaction Types",
                        summary = "Fixed Deposit under 500000 - transaction configuration",
                        value = """
                            {
                              "content": [
                                {
                                  "transactionId": "3e6dcd5d-1615-4e93-8cd3-dc9a2bf8713f",
                                  "transactionCode": "FD_DEPOSIT",
                                  "transactionType": "DEPOSIT",
                                  "amountLimit": null
                                }
                              ],
                              "pageable": {
                                "pageNumber": 0,
                                "pageSize": 10,
                                "sort": {
                                  "sorted": false,
                                  "empty": true
                                }
                              },
                              "totalElements": 1,
                              "totalPages": 1,
                              "last": true,
                              "first": true,
                              "number": 0,
                              "size": 10,
                              "numberOfElements": 1,
                              "empty": false
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Savings Account Transactions",
                        summary = "Standard savings account transaction types",
                        value = """
                            {
                              "content": [
                                {
                                  "transactionId": "991e8400-e29b-41d4-a716-446655440001",
                                  "transactionCode": "TXN_DEPOSIT",
                                  "transactionType": "DEPOSIT",
                                  "amountLimit": 50000.00
                                },
                                {
                                  "transactionId": "992e8400-e29b-41d4-a716-446655440002",
                                  "transactionCode": "TXN_WITHDRAWAL",
                                  "transactionType": "WITHDRAWAL",
                                  "amountLimit": 2000.00
                                },
                                {
                                  "transactionId": "993e8400-e29b-41d4-a716-446655440003",
                                  "transactionCode": "TXN_INTEREST_ACCRUED",
                                  "transactionType": "INTEREST_ACCRUED",
                                  "amountLimit": null
                                }
                              ],
                              "pageable": {
                                "pageNumber": 0,
                                "pageSize": 10,
                                "sort": {
                                  "sorted": false,
                                  "empty": true
                                }
                              },
                              "totalElements": 3,
                              "totalPages": 1,
                              "last": true,
                              "first": true,
                              "number": 0,
                              "size": 10,
                              "numberOfElements": 3,
                              "empty": false
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Loan Product Transactions",
                        summary = "Loan-specific transaction types",
                        value = """
                            {
                              "content": [
                                {
                                  "transactionId": "994e8400-e29b-41d4-a716-446655440004",
                                  "transactionCode": "TXN_DISBURSEMENT",
                                  "transactionType": "DISBURSEMENT",
                                  "amountLimit": 1000000.00
                                },
                                {
                                  "transactionId": "995e8400-e29b-41d4-a716-446655440005",
                                  "transactionCode": "TXN_PAYMENT",
                                  "transactionType": "PAYMENT",
                                  "amountLimit": 50000.00
                                },
                                {
                                  "transactionId": "996e8400-e29b-41d4-a716-446655440006",
                                  "transactionCode": "TXN_INTEREST_ACCRUED",
                                  "transactionType": "INTEREST_ACCRUED",
                                  "amountLimit": null
                                }
                              ],
                              "totalElements": 3,
                              "totalPages": 1,
                              "number": 0,
                              "size": 10,
                              "numberOfElements": 3,
                              "empty": false
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
                    name = "Product Not Found",
                    value = """
                        {
                          "error": "Resource Not Found",
                          "message": "Product not found with code: SAV-INVALID-2025",
                          "timestamp": "2025-10-18T10:00:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<Page<ProductTransactionDTO>> getTransactions(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Pagination parameters:
                    - page: Page number (0-indexed)
                    - size: Items per page
                    - sort: Sort field and direction
                    
                    Example: ?page=0&size=10&sort=transactionType,asc
                    """,
                example = "page=0&size=10"
            )
            Pageable pageable) {
        return ResponseEntity.ok(productTransactionService.getTransactionsForProduct(productCode, pageable));
    }

    @GetMapping("/{transactionCode}")
    @Operation(
        summary = "Retrieve specific transaction type configuration",
        description = """
            Get detailed configuration for a single transaction type by its transaction code.
            
            **What You Get:**
            Complete transaction configuration including:
            - Transaction ID and code
            - Transaction type (enum)
            - Amount limit for the transaction
            
            **Use Cases:**
            
            **Scenario 1: Customer Inquiry**
            Customer asks about specific transaction:
            - "What's my withdrawal limit?"
            - "Can I make deposits?"
            - "What's the maximum payment amount?"
            - Retrieve exact configuration
            
            **Scenario 2: Before Configuration Update**
            Verify current settings before modifying:
            - Review existing limit
            - Check transaction type
            - Understand current configuration
            - Plan the update
            
            **Scenario 3: Troubleshooting Transaction Failure**
            Transaction declined, need to know why:
            - Check transaction amount limit
            - Verify transaction type exists
            - Review configuration
            
            **Scenario 4: Compliance Documentation**
            Audit requires transaction details:
            - Document specific limits
            - Verify transaction types
            - Export configuration
            
            **Related Endpoints:**
            - GET /api/products/{code}/transactions - View all transaction types
            - PUT /api/products/{code}/transactions/{id} - Update this configuration
            - DELETE /api/products/{code}/transactions/{id} - Remove transaction type
            """,
        tags = {"Product Transaction Types"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Transaction configuration found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductTransactionDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Withdrawal Configuration",
                        summary = "Withdrawal transaction type details",
                        value = """
                            {
                              "transactionId": "991e8400-e29b-41d4-a716-446655440001",
                              "transactionCode": "TXN_WITHDRAWAL",
                              "transactionType": "WITHDRAWAL",
                              "amountLimit": 2000.00
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Deposit Configuration",
                        summary = "Deposit transaction type details",
                        value = """
                            {
                              "transactionId": "992e8400-e29b-41d4-a716-446655440002",
                              "transactionCode": "TXN_DEPOSIT",
                              "transactionType": "DEPOSIT",
                              "amountLimit": 50000.00
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Transaction configuration or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Transaction Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Transaction configuration not found with ID: tx-999e8400-invalid",
                              "timestamp": "2025-10-18T10:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Product Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Product not found with code: SAV-INVALID-2025",
                              "timestamp": "2025-10-18T10:00:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ProductTransactionDTO> getTransactionByCode(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Transaction code identifier.
                    
                    Unique code for the transaction type (e.g., TXN_WITHDRAWAL, TXN_DEPOSIT).
                    """,
                required = true,
                example = "TXN_WITHDRAWAL"
            )
            @PathVariable String transactionCode) {
        return ResponseEntity.ok(productTransactionService.getTransactionByCode(productCode, transactionCode));
    }

    @PutMapping("/{transactionCode}")
    @Operation(
        summary = "Update transaction type configuration",
        description = """
            Modify an existing transaction type configuration.
            
            **What You Can Update:**
            - Transaction name
            - Transaction type (enum value)
            - Debit/Credit indicator
            - Minimum and maximum amounts
            - Description
            - Active/inactive status
            
            **Update Scenarios:**
            
            **Scenario 1: Increase Withdrawal Limits**
            Customer tier upgrade requires higher limits:
            - Increase maximumAmount from ₹2,00,000 to ₹5,00,000
            - Update minimumAmount if needed
            - Keep transaction type as WITHDRAWAL
            - Immediate effect on all customers
            
            **Scenario 2: Adjust Deposit Limits**
            Compliance requirement changes:
            - Reduce maximumAmount for AML compliance
            - Update minimumAmount threshold
            - Modify description to note compliance
            - Document the regulatory reason
            
            **Scenario 3: Temporarily Disable Transaction Type**
            Maintenance or security concern:
            - Set isActive to false
            - Keep configuration intact
            - No permanent deletion
            - Easy re-activation
            
            **Scenario 4: Update Transaction Details**
            Product refinement:
            - Change transaction name for clarity
            - Update description with more details
            - Adjust debit/credit indicator if needed
            - Maintain existing limits
            
            **Best Practices:**
            - Update during low-traffic periods
            - Test in staging environment first
            - Communicate changes to customers
            - Monitor transaction success rates
            - Keep audit trail of changes
            
            **Warning:**
            Changes take effect immediately for all customers. Reducing limits may cause
            previously successful transactions to fail.
            
            **Related Endpoints:**
            - GET /api/products/{code}/transactions/{code} - View current configuration
            - DELETE /api/products/{code}/transactions/{code} - Remove transaction type
            - GET /api/products/{code}/transactions - View all configurations
            """,
        tags = {"Product Transaction Types"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Transaction configuration updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductTransactionDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Increased Withdrawal Limits",
                        summary = "Higher withdrawal limits for premium tier",
                        value = """
                            {
                              "transactionId": "991e8400-e29b-41d4-a716-446655440001",
                              "transactionCode": "TXN_WITHDRAWAL",
                              "transactionType": "WITHDRAWAL",
                              "amountLimit": 5000.00
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Updated Deposit Configuration",
                        summary = "Adjusted deposit limits for compliance",
                        value = """
                            {
                              "transactionId": "992e8400-e29b-41d4-a716-446655440002",
                              "transactionCode": "TXN_DEPOSIT",
                              "transactionType": "DEPOSIT",
                              "amountLimit": 75000.00
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Transaction configuration or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Transaction Not Found",
                    value = """
                        {
                          "error": "Resource Not Found",
                          "message": "Transaction configuration not found with ID: tx-999e8400-invalid",
                          "timestamp": "2025-10-18T10:30:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid transaction configuration",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Amount Range",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Daily limit must be greater than or equal to maximum amount",
                              "timestamp": "2025-10-18T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Invalid Channel",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Invalid channel: INVALID_CHANNEL. Allowed: ATM, ONLINE, MOBILE, BRANCH, PHONE, POS",
                              "timestamp": "2025-10-18T10:30:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ProductTransactionDTO> updateTransaction(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = "Transaction code to update",
                required = true,
                example = "TXN_WITHDRAWAL"
            )
            @PathVariable String transactionCode,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = """
                    Updated transaction configuration.
                    
                    All fields are required even when updating.
                    """,
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductTransactionRequestDTO.class),
                    examples = {
                        @ExampleObject(
                            name = "Increase Withdrawal Limits",
                            summary = "Higher limits for premium accounts",
                            value = """
                                {
                                  "transactionCode": "TXN_WITHDRAWAL",
                                  "transactionName": "Premium Withdrawal",
                                  "transactionType": "WITHDRAWAL",
                                  "debitCreditIndicator": "D",
                                  "minimumAmount": 20.00,
                                  "maximumAmount": 5000.00,
                                  "description": "Enhanced withdrawal limits for premium tier customers",
                                  "isActive": true
                                }
                                """
                        ),
                        @ExampleObject(
                            name = "Update Deposit Configuration",
                            summary = "Adjusted deposit limits",
                            value = """
                                {
                                  "transactionCode": "TXN_DEPOSIT",
                                  "transactionName": "Standard Deposit",
                                  "transactionType": "DEPOSIT",
                                  "debitCreditIndicator": "C",
                                  "minimumAmount": 10.00,
                                  "maximumAmount": 75000.00,
                                  "description": "Updated deposit limits per compliance requirements",
                                  "isActive": true
                                }
                                """
                        )
                    }
                )
            )
            @Valid @RequestBody ProductTransactionRequestDTO transactionDto) {
        return ResponseEntity.ok(productTransactionService.updateTransaction(productCode, transactionCode, transactionDto));
    }

    @DeleteMapping("/{transactionCode}")
    @Operation(
        summary = "Permanently remove transaction type configuration",
        description = """
            Delete a transaction type configuration from the product.
            
            **⚠️ CRITICAL WARNING:**
            This action is PERMANENT and IRREVERSIBLE!
            - Transaction type will be completely removed
            - Configuration cannot be recovered
            - No undo functionality
            
            **Before Deleting:**
            1. **Verify Impact:** Check if transaction type is actively used
            2. **Consider Disable:** Set isActive=false instead of deleting (via PUT)
            3. **Document Reason:** Keep audit trail of why transaction was removed
            4. **Test Impact:** Verify in staging environment first
            
            **When to DELETE:**
            - Transaction type permanently discontinued
            - Removing test/development configurations
            - Product being decommissioned
            - Consolidating duplicate transaction types
            
            **When to DISABLE (Recommended Alternative):**
            - Temporary suspension needed
            - Maintenance or security concern
            - Testing new configurations
            - Want to preserve configuration
            
            **Use PUT /api/products/{code}/transactions/{code} to disable:**
            Set isActive field to false in the request body
            
            **Recommended Workflow:**
            1. Get transaction details (verify correct one)
            2. Document deletion reason
            3. Disable transaction first (test impact via PUT)
            4. Monitor for issues
            5. Delete if confirmed no longer needed
            
            **What Happens:**
            - Configuration removed from database
            - Transaction type no longer available
            - Transaction code becomes invalid
            
            **Impact Examples:**
            - Delete WITHDRAWAL → No withdrawals possible
            - Delete DEPOSIT → Can't add funds (usually BAD idea!)
            - Delete INTEREST_ACCRUED → No automatic interest
            
            **Related Endpoints:**
            - PUT /api/products/{code}/transactions/{code} - Disable instead of delete
            - GET /api/products/{code}/transactions/{code} - Verify before deletion
            - GET /api/products/{code}/transactions - View remaining transaction types
            """,
        tags = {"Product Transaction Types"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Transaction type permanently deleted (no content returned)"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Transaction configuration or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Transaction Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Transaction configuration not found with ID: tx-999e8400-invalid",
                              "timestamp": "2025-10-18T10:45:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Product Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Product not found with code: SAV-INVALID-2025",
                              "timestamp": "2025-10-18T10:45:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Transaction code to permanently delete.
                    
                    ⚠️ WARNING: This action cannot be undone!
                    Consider disabling the transaction instead using PUT endpoint with isActive=false.
                    """,
                required = true,
                example = "TXN_WITHDRAWAL"
            )
            @PathVariable String transactionCode) {
        productTransactionService.deleteTransaction(productCode, transactionCode);
        return ResponseEntity.noContent().build();
    }
}