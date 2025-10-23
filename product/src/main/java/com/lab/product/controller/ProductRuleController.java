package com.lab.product.controller;

import com.lab.product.DTO.ProductRuleDTO;
import com.lab.product.DTO.ProductRuleRequestDTO;
import com.lab.product.service.ProductRuleService;
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
@RequestMapping("/api/products/{productCode}/rules")
@RequiredArgsConstructor
@Tag(
    name = "Product Business Rules",
    description = "This API is the definitive source for managing all business rules that govern a financial product."+
            " It allows you to configure these rules as key attributes, including balance limits (min/max),"+
            " conditional interest rate bonuses (e.g., for 'Gold members' or 'under 18'), and the primary interest"+
            " calculation method (simple vs. compound with its frequency). It's the central hub for a product's entire"+
            " configurable DNA and operational logic."
)
public class ProductRuleController {

    @Autowired
    private final ProductRuleService productRuleService;

    @PostMapping
    @Operation(
        summary = "Configure business rule for product",
        description = """
            Add a new business rule to govern product behavior and operations. Rules define constraints,
            validations, limits, and conditional logic for the product.
            
            **Common Business Rules:**
            
            **Balance Limits:**
            - Minimum opening balance requirement
            - Maximum balance allowed
            - Minimum monthly balance
            - Overdraft limit
            
            **Transaction Limits:**
            - Daily withdrawal limit
            - Per-transaction maximum
            - Monthly transaction count limit
            - ATM withdrawal restrictions
            
            **Eligibility Rules:**
            - Age restrictions (18+, under 60, etc.)
            - Income requirements (min annual income)
            - Credit score minimums
            - Geographic restrictions
            
            **Operational Rules:**
            - Account closure restrictions (min 90 days)
            - Dormancy period (inactive after 12 months)
            - Statement frequency requirements
            - Auto-sweep rules
            
            **Penalty Rules:**
            - Minimum balance penalty triggers
            - Late payment conditions
            - Early closure penalties
            - Overdraft penalty rules
            
            **Use Cases:**
            
            **Scenario 1: Minimum Balance Rule**
            - Rule: Minimum balance must be ₹10,000
            - Type: VALIDATION
            - Action: Reject if balance < ₹10,000
            - Penalty: $5 fee if falls below
            
            **Scenario 2: Age Restriction Rule**
            - Rule: Account holder must be 18+ years
            - Type: ELIGIBILITY
            - Action: Block account opening if under 18
            - Exception: Joint account with parent
            
            **Scenario 3: Daily Withdrawal Limit**
            - Rule: Maximum ₹1,00,000 withdrawal per day
            - Type: TRANSACTION_LIMIT
            - Action: Decline if exceeds limit
            - Reset: Daily at midnight
            
            **Scenario 4: Loan Amount Limit**
            - Rule: Maximum loan 5x annual income
            - Type: LENDING_RULE
            - Action: Reject if exceeds ratio
            - Override: Manager approval required
            
            **Rule Components:**
            - **Rule Name**: Descriptive identifier
            - **Rule Type**: Category (VALIDATION, LIMIT, ELIGIBILITY)
            - **Rule Expression**: Logic/condition (JSON or expression)
            - **Rule Priority**: Execution order
            - **Active Status**: Enable/disable rule
            - **Error Message**: User-friendly message on violation
            
            **Related Endpoints:**
            - GET /api/products/{code}/rules - View all rules
            - PUT /api/products/{code}/rules/{id} - Update rule
            - DELETE /api/products/{code}/rules/{id} - Remove rule
            - POST /api/products - Create product first
            """,
        tags = {"Product Business Rules"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Business rule created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductRuleDTO.class),
                examples = {
                    @ExampleObject(
                        name = "FD001 Minimum Amount Rule",
                        summary = "FD minimum 10,000 INR requirement",
                        value = """
                            {
                              "ruleId": "a9e3f3f3-a120-41a3-8e5e-b2c1f0b5e080",
                              "ruleCode": "MIN001",
                              "ruleName": "Minimum for FD001",
                              "ruleType": "SIMPLE",
                              "dataType": "NUMBER",
                              "ruleValue": "10000",
                              "validationType": "MIN_MAX",
                              "isActive": true,
                              "createdAt": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "FD001 Maximum Amount Rule",
                        summary = "FD maximum 500,000 INR limit",
                        value = """
                            {
                              "ruleId": "37fe7f8a-d9ea-4661-8ac9-dc3dc3ea5dcd",
                              "ruleCode": "MAX001",
                              "ruleName": "Maximum for FD001",
                              "ruleType": "SIMPLE",
                              "dataType": "NUMBER",
                              "ruleValue": "500000",
                              "validationType": "MIN_MAX",
                              "isActive": true,
                              "createdAt": "2025-10-15T10:31:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Savings Age Rule",
                        summary = "Must be 18 years or older",
                        value = """
                            {
                              "ruleId": "992e8400-e29b-41d4-a716-446655440002",
                              "ruleName": "Age Eligibility Check",
                              "ruleType": "ELIGIBILITY",
                              "ruleExpression": "age >= 18",
                              "rulePriority": 1,
                              "errorMessage": "Account holder must be 18 years or older",
                              "isActive": true,
                              "createdAt": "2025-10-15T10:31:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid rule configuration",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Missing Required Field",
                        value = """
                            {
                              "error": "Validation Failed",
                              "message": "ruleName is required",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Invalid Rule Expression",
                        value = """
                            {
                              "error": "Validation Failed",
                              "message": "Invalid rule expression syntax: 'balance > invalid'",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Duplicate Rule Name",
                        value = """
                            {
                              "error": "Conflict",
                              "message": "Rule 'Minimum Balance Requirement' already exists for product SAV-HIGH-YIELD-2025",
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
        )
    })
    public ResponseEntity<ProductRuleDTO> addRule(
            @Parameter(
                description = """
                    Product code to add business rule to.
                    
                    Example: SAV-HIGH-YIELD-2025
                    """,
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Business rule configuration.
                    
                    Required fields:
                    - ruleName: Descriptive name
                    - ruleType: VALIDATION, LIMIT, ELIGIBILITY, etc.
                    - ruleExpression: Logic/condition
                    
                    Optional fields:
                    - rulePriority: Execution order (default: 999)
                    - errorMessage: User-friendly violation message
                    - isActive: Enable/disable (default: true)
                    
                    See request examples for common rule patterns.
                    """,
                required = true
            )
            @Valid @RequestBody ProductRuleRequestDTO ruleDto) {
        return new ResponseEntity<>(productRuleService.addRuleToProduct(productCode, ruleDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "Retrieve all business rules for product",
        description = """
            Get complete list of all business rules configured for this product. Shows all constraints,
            validations, limits, and operational rules.
            
            **What it returns:**
            - All configured business rules
            - Rule types and priorities
            - Rule expressions/logic
            - Active/inactive status
            - Error messages
            - Execution order (by priority)
            
            **Use Cases:**
            
            **Scenario 1: Account Opening Validation**
            - System retrieves all eligibility rules
            - Validates applicant against each rule
            - Blocks if any rule fails
            - Shows error message to user
            
            **Scenario 2: Transaction Processing**
            - Retrieve transaction limit rules
            - Check against current transaction
            - Decline if exceeds limits
            - Log rule violation
            
            **Scenario 3: Admin Rule Review**
            - Review all product rules
            - Verify rule configuration
            - Check for conflicts or gaps
            - Plan rule updates
            
            **Scenario 4: Compliance Audit**
            - Export all rule configurations
            - Document business logic
            - Verify regulatory compliance
            - Compare across products
            
            **Response Ordering:**
            - Rules sorted by priority (ascending)
            - Lower number = higher priority
            - Priority 1 executes before Priority 2
            - Helps understand rule execution flow
            
            **Related Endpoints:**
            - POST /api/products/{code}/rules - Add new rule
            - GET /api/products/{code}/rules/{id} - Get specific rule
            - PUT /api/products/{code}/rules/{id} - Update rule
            - GET /api/products/{code} - View product configuration
            """,
        tags = {"Product Business Rules"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Business rules retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class),
                examples = {
                    @ExampleObject(
                        name = "FD001 Business Rules",
                        summary = "Fixed Deposit under 500000 - Comprehensive rule set",
                        value = """
                            {
                              "content": [
                                {
                                  "ruleId": "a9e3f3f3-a120-41a3-8e5e-b2c1f0b5e080",
                                  "ruleCode": "MIN001",
                                  "ruleName": "Minimum for FD001",
                                  "ruleType": "SIMPLE",
                                  "dataType": "NUMBER",
                                  "ruleValue": "10000",
                                  "validationType": "MIN_MAX"
                                },
                                {
                                  "ruleId": "37fe7f8a-d9ea-4661-8ac9-dc3dc3ea5dcd",
                                  "ruleCode": "MAX001",
                                  "ruleName": "Maximum for FD001",
                                  "ruleType": "SIMPLE",
                                  "dataType": "NUMBER",
                                  "ruleValue": "500000",
                                  "validationType": "MIN_MAX"
                                },
                                {
                                  "ruleId": "1baae6cf-106a-444d-b7a7-85b889f329d8",
                                  "ruleCode": "JR001",
                                  "ruleName": "Extra Interest for under 18",
                                  "ruleType": "SIMPLE",
                                  "dataType": "PERCENTAGE",
                                  "ruleValue": "0.5",
                                  "validationType": "EXACT"
                                },
                                {
                                  "ruleId": "c478c7af-9d64-41a6-b2e2-45552054c85d",
                                  "ruleCode": "SR001",
                                  "ruleName": "Extra Interest for sr",
                                  "ruleType": "SIMPLE",
                                  "dataType": "PERCENTAGE",
                                  "ruleValue": "0.75",
                                  "validationType": "EXACT"
                                },
                                {
                                  "ruleId": "fb409ac1-149f-4163-8e88-835da99bb8bc",
                                  "ruleCode": "DY001",
                                  "ruleName": "Extra Interest for Digi Youth",
                                  "ruleType": "SIMPLE",
                                  "dataType": "PERCENTAGE",
                                  "ruleValue": "0.25",
                                  "validationType": "EXACT"
                                },
                                {
                                  "ruleId": "6c4879fc-9650-4762-bdff-28c3fbf8177f",
                                  "ruleCode": "SIL001",
                                  "ruleName": "Silver members extra interest",
                                  "ruleType": "SIMPLE",
                                  "dataType": "NUMBER",
                                  "ruleValue": "0.5",
                                  "validationType": "EXACT"
                                },
                                {
                                  "ruleId": "473f6407-6378-412e-8ef4-b579e362d83a",
                                  "ruleCode": "GOLD001",
                                  "ruleName": "Gold members extra interest",
                                  "ruleType": "SIMPLE",
                                  "dataType": "NUMBER",
                                  "ruleValue": "1",
                                  "validationType": "EXACT"
                                },
                                {
                                  "ruleId": "fe956b24-a359-4c26-bafd-05855e669851",
                                  "ruleCode": "PLAT001",
                                  "ruleName": "Platinum members extra interest",
                                  "ruleType": "SIMPLE",
                                  "dataType": "NUMBER",
                                  "ruleValue": "1.5",
                                  "validationType": "EXACT"
                                },
                                {
                                  "ruleId": "d77a6842-2296-40cc-af4b-81b72c770e61",
                                  "ruleCode": "EMP001",
                                  "ruleName": "Employee members extra interest",
                                  "ruleType": "SIMPLE",
                                  "dataType": "NUMBER",
                                  "ruleValue": "1.5",
                                  "validationType": "EXACT"
                                },
                                {
                                  "ruleId": "363ffabc-0827-46d3-bcd1-adae3f42ffe3",
                                  "ruleCode": "MAXINT001",
                                  "ruleName": "Maximum excess interest",
                                  "ruleType": "SIMPLE",
                                  "dataType": "PERCENTAGE",
                                  "ruleValue": "2",
                                  "validationType": "MIN_MAX"
                                }
                              ],
                              "totalElements": 10,
                              "totalPages": 1,
                              "number": 0,
                              "size": 20
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Savings Account Rules",
                        summary = "3 business rules for savings product",
                        value = """
                            {
                              "content": [
                                {
                                  "ruleId": "991e8400-e29b-41d4-a716-446655440001",
                                  "ruleName": "Age Eligibility",
                                  "ruleType": "ELIGIBILITY",
                                  "ruleExpression": "age >= 18",
                                  "rulePriority": 1,
                                  "errorMessage": "Must be 18+ years old",
                                  "isActive": true
                                },
                                {
                                  "ruleId": "992e8400-e29b-41d4-a716-446655440002",
                                  "ruleName": "Minimum Opening Balance",
                                  "ruleType": "VALIDATION",
                                  "ruleExpression": "opening_balance >= 100",
                                  "rulePriority": 2,
                                  "errorMessage": "Minimum opening balance: ₹10,000",
                                  "isActive": true
                                },
                                {
                                  "ruleId": "993e8400-e29b-41d4-a716-446655440003",
                                  "ruleName": "Daily Withdrawal Limit",
                                  "ruleType": "TRANSACTION_LIMIT",
                                  "ruleExpression": "daily_withdrawals <= 1000",
                                  "rulePriority": 3,
                                  "errorMessage": "Daily limit: ₹1,00,000",
                                  "isActive": true
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
                        name = "Loan Product Rules",
                        summary = "Complex loan validation rules",
                        value = """
                            {
                              "content": [
                                {
                                  "ruleId": "994e8400-e29b-41d4-a716-446655440004",
                                  "ruleName": "Credit Score Minimum",
                                  "ruleType": "ELIGIBILITY",
                                  "ruleExpression": "credit_score >= 650",
                                  "rulePriority": 1,
                                  "errorMessage": "Minimum credit score: 650",
                                  "isActive": true
                                },
                                {
                                  "ruleId": "995e8400-e29b-41d4-a716-446655440005",
                                  "ruleName": "Debt-to-Income Ratio",
                                  "ruleType": "VALIDATION",
                                  "ruleExpression": "debt_to_income <= 0.43",
                                  "rulePriority": 2,
                                  "errorMessage": "DTI cannot exceed 43%",
                                  "isActive": true
                                },
                                {
                                  "ruleId": "996e8400-e29b-41d4-a716-446655440006",
                                  "ruleName": "Maximum Loan Amount",
                                  "ruleType": "LIMIT",
                                  "ruleExpression": "loan_amount <= 5 * annual_income",
                                  "rulePriority": 3,
                                  "errorMessage": "Max loan: 5x annual income",
                                  "isActive": true
                                }
                              ],
                              "totalElements": 3,
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
                          "message": "Product not found with code: SAV-INVALID-2025",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<Page<ProductRuleDTO>> getRules(
            @Parameter(
                description = """
                    Product code to retrieve rules for.
                    
                    Returns all business rules sorted by priority.
                    """,
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = "Pagination parameters (page, size, sort)",
                example = "page=0&size=20&sort=rulePriority,asc"
            )
            Pageable pageable) {
        return ResponseEntity.ok(productRuleService.getRulesForProduct(productCode, pageable));
    }

    @GetMapping("/{ruleId}")
    @Operation(
        summary = "Retrieve specific business rule details",
        description = """
            Get detailed information about a single business rule by its unique identifier.
            
            **What it returns:**
            - Complete rule configuration
            - Rule name and type
            - Rule expression/logic
            - Priority and error message
            - Active/inactive status
            - Creation and update timestamps
            
            **Use Cases:**
            
            **Scenario 1: Rule Validation Before Update**
            - Retrieve current rule configuration
            - Review existing logic
            - Plan modifications
            - Verify rule still exists
            
            **Scenario 2: Debug Rule Failure**
            - Transaction failed due to rule
            - Retrieve rule details
            - Understand why it failed
            - Check rule expression
            
            **Scenario 3: Rule Documentation**
            - Export specific rule details
            - Document business logic
            - Create training materials
            - Share with stakeholders
            
            **Scenario 4: Rule Testing**
            - Get rule expression
            - Test with sample data
            - Verify expected behavior
            - Validate edge cases
            
            **Related Endpoints:**
            - GET /api/products/{code}/rules - View all rules
            - PUT /api/products/{code}/rules/{id} - Update this rule
            - DELETE /api/products/{code}/rules/{id} - Remove this rule
            - POST /api/products/{code}/rules - Add new rule
            """,
        tags = {"Product Business Rules"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Business rule found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductRuleDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Balance Rule Details",
                        summary = "Complete minimum balance rule",
                        value = """
                            {
                              "ruleId": "991e8400-e29b-41d4-a716-446655440001",
                              "ruleName": "Minimum Balance Requirement",
                              "ruleType": "VALIDATION",
                              "ruleExpression": "balance >= 100",
                              "rulePriority": 2,
                              "errorMessage": "Account balance must be at least ₹10,000",
                              "isActive": true,
                              "createdAt": "2025-01-01T00:00:00",
                              "updatedAt": "2025-03-15T14:20:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Transaction Limit Rule",
                        summary = "Daily withdrawal limit details",
                        value = """
                            {
                              "ruleId": "993e8400-e29b-41d4-a716-446655440003",
                              "ruleName": "Daily ATM Withdrawal Limit",
                              "ruleType": "TRANSACTION_LIMIT",
                              "ruleExpression": "sum(daily_withdrawals) <= 1000",
                              "rulePriority": 3,
                              "errorMessage": "Daily withdrawal limit of ₹1,00,000 has been exceeded. Please try again tomorrow.",
                              "isActive": true,
                              "createdAt": "2025-01-01T00:00:00",
                              "updatedAt": "2025-01-01T00:00:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Rule or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Rule Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Business rule not found with ID: 991e8400-e29b-41d4-a716-446655440999",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Product Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Product not found with code: SAV-INVALID-2025",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ProductRuleDTO> getRuleByCode(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Business rule ID (UUID).
                    
                    Unique identifier for the rule.
                    """,
                required = true,
                example = "991e8400-e29b-41d4-a716-446655440001"
            )
            @PathVariable String ruleId) {
        return ResponseEntity.ok(productRuleService.getRuleByCode(productCode, ruleId));
    }

    @PutMapping("/{ruleId}")
    @Operation(
        summary = "Update business rule configuration",
        description = """
            Modify an existing business rule's configuration, logic, or settings.
            
            **What you can update:**
            - Rule name and type
            - Rule expression/logic
            - Priority order
            - Error message text
            - Active/inactive status
            
            **Update Scenarios:**
            
            **Scenario 1: Adjust Rule Limits**
            Update thresholds when business requirements change:
            - Increase minimum balance requirement
            - Modify daily transaction limits
            - Change withdrawal amounts
            - Update age restrictions
            
            **Scenario 2: Modify Error Messages**
            Improve customer communication:
            - Make messages more customer-friendly
            - Add help information
            - Translate to different languages
            - Include contact details
            
            **Scenario 3: Change Rule Priority**
            Adjust execution order:
            - Critical rules first
            - Group related rules
            - Optimize performance
            - Handle dependencies
            
            **Scenario 4: Temporarily Disable Rule**
            Suspend rule without deletion:
            - Set isActive to false
            - Keep rule configuration
            - Maintain audit history
            - Easy re-activation
            
            **Best Practices:**
            - Test rule logic before updating
            - Update during low-traffic periods
            - Communicate changes to users
            - Keep audit trail of changes
            - Verify no conflicts with other rules
            
            **Warning:**
            Changes to rule expressions affect all future validations immediately.
            Test thoroughly before updating critical rules.
            
            **Related Endpoints:**
            - GET /api/products/{code}/rules/{id} - View current configuration
            - DELETE /api/products/{code}/rules/{id} - Remove rule
            - GET /api/products/{code}/rules - View all rules
            """,
        tags = {"Product Business Rules"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Rule updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductRuleDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Updated Balance Rule",
                        summary = "Increased minimum balance requirement",
                        value = """
                            {
                              "ruleId": "991e8400-e29b-41d4-a716-446655440001",
                              "ruleName": "Minimum Balance Requirement",
                              "ruleType": "VALIDATION",
                              "ruleExpression": "balance >= 500",
                              "rulePriority": 2,
                              "errorMessage": "Account balance must be at least ₹50,000 to avoid monthly fees",
                              "isActive": true,
                              "createdAt": "2025-01-01T00:00:00",
                              "updatedAt": "2025-10-15T14:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Disabled Transaction Rule",
                        summary = "Temporarily suspended withdrawal limit",
                        value = """
                            {
                              "ruleId": "993e8400-e29b-41d4-a716-446655440003",
                              "ruleName": "Daily ATM Withdrawal Limit",
                              "ruleType": "TRANSACTION_LIMIT",
                              "ruleExpression": "sum(daily_withdrawals) <= 1000",
                              "rulePriority": 3,
                              "errorMessage": "Daily withdrawal limit of ₹1,00,000 has been exceeded.",
                              "isActive": false,
                              "createdAt": "2025-01-01T00:00:00",
                              "updatedAt": "2025-10-15T14:35:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Rule or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Rule Not Found",
                    value = """
                        {
                          "error": "Resource Not Found",
                          "message": "Business rule not found with ID: 991e8400-e29b-41d4-a716-446655440999",
                          "timestamp": "2025-10-15T14:30:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid rule data or validation error",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Expression",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Invalid rule expression syntax: balance >= [missing value]",
                              "timestamp": "2025-10-15T14:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Duplicate Rule Name",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Rule with name 'Minimum Balance Requirement' already exists for this product",
                              "timestamp": "2025-10-15T14:30:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ProductRuleDTO> updateRule(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = "Business rule ID (UUID) to update",
                required = true,
                example = "991e8400-e29b-41d4-a716-446655440001"
            )
            @PathVariable String ruleId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = """
                    Updated rule configuration.
                    
                    Any omitted fields will retain their current values.
                    """,
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductRuleRequestDTO.class),
                    examples = {
                        @ExampleObject(
                            name = "Increase Balance Requirement",
                            summary = "Update minimum balance from ₹10,000 to ₹50,000",
                            value = """
                                {
                                  "ruleName": "Minimum Balance Requirement",
                                  "ruleType": "VALIDATION",
                                  "ruleExpression": "balance >= 500",
                                  "rulePriority": 2,
                                  "errorMessage": "Account balance must be at least ₹50,000 to avoid monthly fees",
                                  "isActive": true
                                }
                                """
                        ),
                        @ExampleObject(
                            name = "Disable Rule",
                            summary = "Temporarily suspend rule without deletion",
                            value = """
                                {
                                  "ruleName": "Daily ATM Withdrawal Limit",
                                  "ruleType": "TRANSACTION_LIMIT",
                                  "ruleExpression": "sum(daily_withdrawals) <= 1000",
                                  "rulePriority": 3,
                                  "errorMessage": "Daily withdrawal limit of ₹1,00,000 has been exceeded.",
                                  "isActive": false
                                }
                                """
                        )
                    }
                )
            )
            @RequestBody ProductRuleRequestDTO request) {
        return ResponseEntity.ok(productRuleService.updateRule(productCode, ruleId, request));
    }

    @DeleteMapping("/{ruleId}")
    @Operation(
        summary = "Permanently remove business rule",
        description = """
            Delete a business rule from the product configuration.
            
            **⚠️ CRITICAL WARNING:**
            This action is PERMANENT and IRREVERSIBLE!
            - Rule configuration will be lost
            - Cannot be recovered
            - Affects all future validations
            - No undo functionality
            
            **Before Deleting:**
            1. **Verify Impact:** Check if rule is actively used
            2. **Document Reason:** Keep audit trail why rule was removed
            3. **Consider Disable:** Set isActive=false instead of deleting
            4. **Test Impact:** Verify no critical dependencies
            5. **Notify Stakeholders:** Inform affected parties
            
            **When to DELETE:**
            - Rule is completely obsolete
            - Business logic permanently changed
            - Rule was created by mistake
            - Product being decommissioned
            - Duplicate rule cleanup
            
            **When to DISABLE (Recommended Alternative):**
            - Temporary suspension needed
            - Testing new logic
            - Seasonal rules (may reactivate)
            - Uncertain if rule still needed
            - Want to preserve history
            
            **Use PATCH /api/products/{code}/rules/{id} to disable:**
            ```
            {
              "isActive": false
            }
            ```
            
            **Recommended Workflow:**
            1. Get rule details (verify it's correct rule)
            2. Document deletion reason
            3. Disable rule first (test impact)
            4. Monitor for 30 days
            5. Delete if no issues arise
            
            **What Happens:**
            - Rule removed from database
            - No longer enforced on transactions
            - Future validations won't check this rule
            - Existing accounts/transactions unaffected
            - Rule ID becomes invalid
            
            **Related Endpoints:**
            - PUT /api/products/{code}/rules/{id} - Disable instead of delete
            - GET /api/products/{code}/rules/{id} - Verify before deletion
            - GET /api/products/{code}/rules - View remaining rules
            """,
        tags = {"Product Business Rules"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Rule permanently deleted (no content returned)"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Rule or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Rule Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Business rule not found with ID: 991e8400-e29b-41d4-a716-446655440999",
                              "timestamp": "2025-10-15T14:45:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Product Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Product not found with code: SAV-INVALID-2025",
                              "timestamp": "2025-10-15T14:45:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<Void> deleteRule(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Business rule ID (UUID) to permanently delete.
                    
                    ⚠️ WARNING: This action cannot be undone!
                    Consider disabling the rule instead using PUT endpoint with isActive=false.
                    """,
                required = true,
                example = "991e8400-e29b-41d4-a716-446655440001"
            )
            @PathVariable String ruleId) {
        productRuleService.deleteRule(productCode, ruleId);
        return ResponseEntity.noContent().build();
    }
}