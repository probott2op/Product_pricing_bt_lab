package com.lab.product.controller;

import com.lab.product.DTO.ProductInterestDTO;
import com.lab.product.DTO.ProductInterestRequestDTO;
import com.lab.product.service.ProductInterestService;
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
@RequestMapping("/api/products/{productCode}/interest-rates")
@RequiredArgsConstructor
@Tag(
    name = "Product Interest Rates",
    description = "This API provides a structured rate card for financial products like fixed deposits."+
                    " It allows for the retrieval of precise interest rates based on the product's term length."+
                    " For each term, the API returns a set of rates corresponding to various payout schedules,"+
                    " including cumulative and non-cumulative (monthly, quarterly, yearly) options."
)
public class ProductInterestController {

    @Autowired
    private final ProductInterestService productInterestService;

    @PostMapping
    @Operation(
        summary = "Configure interest rate tier for a product",
        description = """
            Add a new interest rate tier to a product's rate structure. This creates tiered interest configurations
            based on balance amounts, allowing different rates for different deposit/loan amounts.
            
            **What it configures:**
            - Interest rate percentage for a specific balance range
            - Minimum and maximum balance thresholds
            - Rate tier identification and naming
            - Active/inactive status for the tier
            
            **Important Notes:**
            - Interest calculation method (SIMPLE/COMPOUND) is set at product level
            - Compounding frequency (DAILY/MONTHLY/etc.) is set at product level
            - This endpoint only configures the RATE percentages per tier
            - Balance ranges should not overlap between tiers
            - Tiers typically start from 0 and cover all ranges
            
            **Use Cases:**
            
            **Scenario 1: Tiered Savings Interest**
            - Tier 1: ₹0 - ₹10,00,000 at 3.0% APY
            - Tier 2: $10,001 - ₹50,00,000 at 3.5% APY
            - Tier 3: $50,001+ at 4.0% APY
            - Higher balances earn better rates
            
            **Scenario 2: Fixed Deposit Tenure Rates**
            - 6 months FD at 5.0%
            - 1 year FD at 5.5%
            - 3 year FD at 6.0%
            - Longer tenure earns higher rates
            
            **Scenario 3: Loan Amount Tiers**
            - ₹0 - ₹25,00,000 at 8.5% APR
            - $25,001 - $100,000 at 7.5% APR
            - ₹1,00,00,100+ at 6.5% APR
            - Larger loans get better rates
            
            **Scenario 4: Promotional Rate Tier**
            - Special introductory rate for first ₹5,00,000
            - 5.0% for balances ₹0 - ₹5,00,000
            - Then regular tiers apply
            
            **Tier Design Best Practices:**
            - No gaps between tiers (continuous coverage)
            - No overlaps between tiers (exclusive ranges)
            - Use 0 for minimum of first tier
            - Use very high number (999999999.99) for unlimited max
            - Clearly name tiers (Tier 1, Tier 2, or Bronze, Silver, Gold)
            
            **Related Endpoints:**
            - GET /api/products/{code}/interest-rates - View all tiers
            - PUT /api/products/{code}/interest-rates/{id} - Update tier
            - DELETE /api/products/{code}/interest-rates/{id} - Remove tier
            - POST /api/products - Set interest type and compounding
            """,
        tags = {"Product Interest Rates"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Interest rate tier created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductInterestDTO.class),
                examples = {
                    @ExampleObject(
                        name = "FD001 12-Month Rate Created",
                        summary = "Fixed Deposit 12-month tenure rate - 7.6%",
                        value = """
                            {
                              "interestId": "0f2d02a8-c93b-4ae4-95b1-f38713891300",
                              "interestCode": "FD001_12M",
                              "term": "12M",
                              "interestRate": 7.6,
                              "compoundingType": "CUMULATIVE",
                              "createdAt": "2025-10-15T10:30:00",
                              "updatedAt": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "FD001 24-Month Rate Created",
                        summary = "Fixed Deposit 24-month tenure rate - 7.7%",
                        value = """
                            {
                              "interestId": "f0c0fc28-5e0e-4e2a-bf37-c0dd0f0c25c2",
                              "interestCode": "FD001_24M",
                              "term": "24M",
                              "interestRate": 7.7,
                              "compoundingType": "CUMULATIVE",
                              "createdAt": "2025-10-15T10:31:00",
                              "updatedAt": "2025-10-15T10:31:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Savings Tier Created",
                        summary = "Savings account base tier",
                        value = """
                            {
                              "interestId": "661e8400-e29b-41d4-a716-446655440001",
                              "tierName": "Tier 1 - Base Rate",
                              "minBalance": 0.00,
                              "maxBalance": 10000.00,
                              "interestRate": 3.50,
                              "isActive": true,
                              "createdAt": "2025-10-15T10:30:00",
                              "updatedAt": "2025-10-15T10:30:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid interest rate configuration",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Missing Required Field",
                        value = """
                            {
                              "error": "Validation Failed",
                              "message": "tierName is required",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Invalid Balance Range",
                        value = """
                            {
                              "error": "Validation Failed",
                              "message": "minBalance (15000) cannot be greater than maxBalance (10000)",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Overlapping Tier",
                        value = """
                            {
                              "error": "Conflict",
                              "message": "Balance range ₹5,00,000 - ₹15,00,000 overlaps with existing tier 'Tier 1' (₹0 - ₹10,00,000)",
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
                          "message": "Failed to create interest rate tier",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<ProductInterestDTO> addInterestRate(
            @Parameter(
                description = """
                    Product code to add interest tier to.
                    
                    Example: SAV-HIGH-YIELD-2025
                    
                    Product must exist and have interest type configured.
                    """,
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Interest rate tier configuration.
                    
                    Required fields:
                    - tierName: Descriptive name for the tier
                    - minBalance: Minimum balance for this tier
                    - maxBalance: Maximum balance for this tier
                    - interestRate: Annual percentage rate
                    
                    Optional fields:
                    - isActive: Enable/disable tier (default: true)
                    
                    See request examples for common tier configurations.
                    """,
                required = true
            )
            @Valid @RequestBody ProductInterestRequestDTO interestDto) {
        return new ResponseEntity<>(productInterestService.addInterestToProduct(productCode, interestDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "Retrieve all interest rate tiers for a product",
        description = """
            Get a paginated list of all interest rate tiers configured for a product. Shows the complete
            rate structure including all balance tiers and their respective interest rates.
            
            **What it returns:**
            - All interest rate tiers for the product
            - Balance ranges for each tier
            - Interest rate percentages
            - Active/inactive status per tier
            - Pagination metadata
            
            **Use Cases:**
            
            **Scenario 1: Display Rate Card to Customer**
            - Show all available interest rates
            - Display tiered structure
            - Help customer understand rate benefits
            
            **Scenario 2: Admin Rate Review**
            - Review current rate configuration
            - Verify tier coverage (no gaps/overlaps)
            - Check competitive positioning
            
            **Scenario 3: Interest Calculation**
            - System retrieves applicable rate
            - Matches customer balance to correct tier
            - Applies rate for interest computation
            
            **Related Endpoints:**
            - POST /api/products/{code}/interest-rates - Add new tier
            - GET /api/products/{code}/interest-rates/{id} - Get specific tier
            - GET /api/products/{code} - View product with interest type
            """,
        tags = {"Product Interest Rates"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Interest rate tiers retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class),
                examples = {
                    @ExampleObject(
                        name = "FD001 Interest Rates",
                        summary = "Fixed Deposit under 500000 - Multiple tenure interest rates",
                        value = """
                            {
                              "content": [
                                {
                                  "rateId": "e75bed67-db57-405d-9f35-0af9d0d62e70",
                                  "rateCode": "INT12M001",
                                  "termInMonths": 12,
                                  "rateCumulative": 7.6,
                                  "rateNonCumulativeMonthly": 7.4,
                                  "rateNonCumulativeQuarterly": 7.5,
                                  "rateNonCumulativeYearly": 7.6
                                },
                                {
                                  "rateId": "953cb07f-40e3-477e-93c4-21aab0a9d387",
                                  "rateCode": "INT24M001",
                                  "termInMonths": 24,
                                  "rateCumulative": 7.7,
                                  "rateNonCumulativeMonthly": 7.5,
                                  "rateNonCumulativeQuarterly": 7.6,
                                  "rateNonCumulativeYearly": 7.7
                                },
                                {
                                  "rateId": "6c433990-8040-47e9-ba5e-d609b2e273d6",
                                  "rateCode": "INT36M001",
                                  "termInMonths": 36,
                                  "rateCumulative": 8,
                                  "rateNonCumulativeMonthly": 7.85,
                                  "rateNonCumulativeQuarterly": 7.9,
                                  "rateNonCumulativeYearly": 7.8
                                },
                                {
                                  "rateId": "fd3feafa-b19b-4540-b42d-c0fa1efdadd6",
                                  "rateCode": "INT60M001",
                                  "termInMonths": 60,
                                  "rateCumulative": 8.5,
                                  "rateNonCumulativeMonthly": 8.3,
                                  "rateNonCumulativeQuarterly": 8.4,
                                  "rateNonCumulativeYearly": 8.5
                                }
                              ],
                              "pageable": {
                                "pageNumber": 0,
                                "pageSize": 20,
                                "sort": {
                                  "sorted": true,
                                  "unsorted": false
                                }
                              },
                              "totalElements": 4,
                              "totalPages": 1,
                              "last": true,
                              "first": true,
                              "empty": false
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Tiered Savings Rates",
                        summary = "3-tier savings rate structure",
                        value = """
                            {
                              "content": [
                                {
                                  "interestId": "661e8400-e29b-41d4-a716-446655440001",
                                  "tierName": "Tier 1 - Base",
                                  "minBalance": 0.00,
                                  "maxBalance": 10000.00,
                                  "interestRate": 3.50,
                                  "isActive": true,
                                  "createdAt": "2025-01-01T00:00:00"
                                },
                                {
                                  "interestId": "662e8400-e29b-41d4-a716-446655440002",
                                  "tierName": "Tier 2 - Silver",
                                  "minBalance": 10000.01,
                                  "maxBalance": 50000.00,
                                  "interestRate": 4.00,
                                  "isActive": true,
                                  "createdAt": "2025-01-01T00:00:00"
                                },
                                {
                                  "interestId": "663e8400-e29b-41d4-a716-446655440003",
                                  "tierName": "Tier 3 - Gold",
                                  "minBalance": 50000.01,
                                  "maxBalance": 999999999.99,
                                  "interestRate": 4.50,
                                  "isActive": true,
                                  "createdAt": "2025-01-01T00:00:00"
                                }
                              ],
                              "pageable": {
                                "pageNumber": 0,
                                "pageSize": 20,
                                "sort": {
                                  "sorted": true,
                                  "unsorted": false
                                }
                              },
                              "totalElements": 3,
                              "totalPages": 1,
                              "last": true,
                              "first": true,
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
    public ResponseEntity<Page<ProductInterestDTO>> getInterestRates(
            @Parameter(
                description = "Product code to retrieve interest tiers for",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = "Pagination parameters (page, size, sort)",
                example = "page=0&size=20"
            )
            Pageable pageable) {
        return ResponseEntity.ok(productInterestService.getInterestRatesForProduct(productCode, pageable));
    }

    @GetMapping("/{interestId}")
    @Operation(
        summary = "Retrieve a specific interest rate tier",
        description = """
            Get detailed information about a single interest rate tier by its unique identifier.
            
            **What it returns:**
            - Complete tier configuration
            - Balance range (min/max)
            - Interest rate percentage
            - Tier name and description
            - Active/inactive status
            - Audit timestamps
            
            **Use Cases:**
            
            **Scenario 1: Verify Rate Configuration**
            - Admin reviewing specific tier details
            - Confirm rate matches rate sheet
            - Verify balance range correctness
            
            **Scenario 2: Rate Quote Calculation**
            - Identify which tier applies to customer
            - Retrieve exact rate for quote
            - Calculate projected interest earnings
            
            **Scenario 3: Before Update Operations**
            - Retrieve current values before modification
            - Compare with proposed changes
            - Validate tier still exists
            
            **Related Endpoints:**
            - GET /api/products/{code}/interest-rates - List all tiers
            - PUT /api/products/{code}/interest-rates/{id} - Update this tier
            - DELETE /api/products/{code}/interest-rates/{id} - Remove this tier
            """,
        tags = {"Product Interest Rates"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Interest rate tier found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductInterestDTO.class),
                examples = @ExampleObject(
                    value = """
                        {
                          "interestId": "661e8400-e29b-41d4-a716-446655440001",
                          "tierName": "Tier 2 - Silver",
                          "minBalance": 10000.01,
                          "maxBalance": 50000.00,
                          "interestRate": 4.00,
                          "isActive": true,
                          "createdAt": "2025-01-01T00:00:00",
                          "updatedAt": "2025-03-15T14:20:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Interest rate tier or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Tier Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Interest rate tier not found with ID: 661e8400-e29b-41d4-a716-446655440999",
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
    public ResponseEntity<ProductInterestDTO> getInterestRateByCode(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = "Interest rate tier ID (UUID)",
                required = true,
                example = "661e8400-e29b-41d4-a716-446655440001"
            )
            @PathVariable String interestId) {
        return ResponseEntity.ok(productInterestService.getInterestRateByCode(productCode, interestId));
    }

    @PutMapping("/{interestId}")
    @Operation(
        summary = "Update an interest rate tier configuration",
        description = """
            Modify an existing interest rate tier. Update rate percentages, balance ranges, or tier status.
            
            **What can be updated:**
            - Interest rate percentage
            - Balance range (min/max)
            - Tier name/description
            - Active/inactive status
            
            **Common Update Scenarios:**
            
            **Scenario 1: Rate Adjustment**
            - Market conditions change
            - Adjust rate from 3.5% to 4.0%
            - Remain competitive with market
            
            **Scenario 2: Balance Range Modification**
            - Expand/contract tier coverage
            - Adjust min/max thresholds
            - Ensure no gaps/overlaps with other tiers
            
            **Scenario 3: Tier Deactivation**
            - Temporarily disable promotional tier
            - Set isActive = false
            - Preserve configuration for potential reactivation
            
            **Scenario 4: Tier Rename**
            - Rebrand tier names
            - "Tier 1" → "Bronze Level"
            - Improve marketing clarity
            
            **Important Notes:**
            - Cannot change tier ID (immutable)
            - Validate new range doesn't overlap with other tiers
            - Rate changes apply to future calculations only
            - Existing accounts continue with their locked-in rates
            - Consider effective date for rate changes
            
            **Related Endpoints:**
            - GET /api/products/{code}/interest-rates/{id} - View current tier
            - GET /api/products/{code}/interest-rates - View all tiers
            - DELETE /api/products/{code}/interest-rates/{id} - Remove tier
            """,
        tags = {"Product Interest Rates"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Interest rate tier updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductInterestDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Rate Increase",
                        summary = "Increased tier rate from 3.5% to 4.0%",
                        value = """
                            {
                              "interestId": "661e8400-e29b-41d4-a716-446655440001",
                              "tierName": "Tier 1 - Base",
                              "minBalance": 0.00,
                              "maxBalance": 10000.00,
                              "interestRate": 4.00,
                              "isActive": true,
                              "createdAt": "2025-01-01T00:00:00",
                              "updatedAt": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Tier Deactivation",
                        summary = "Disabled promotional tier",
                        value = """
                            {
                              "interestId": "664e8400-e29b-41d4-a716-446655440004",
                              "tierName": "Promotional - New Customers",
                              "minBalance": 0.00,
                              "maxBalance": 5000.00,
                              "interestRate": 5.00,
                              "isActive": false,
                              "createdAt": "2025-01-01T00:00:00",
                              "updatedAt": "2025-10-15T10:30:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid update data",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Balance Range",
                        value = """
                            {
                              "error": "Validation Failed",
                              "message": "minBalance (15000) cannot be greater than maxBalance (10000)",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Range Overlap",
                        value = """
                            {
                              "error": "Conflict",
                              "message": "Updated balance range ₹8,00,000 - ₹25,00,000 overlaps with Tier 2 (₹10,00,000 - ₹50,00,000)",
                              "timestamp": "2025-10-15T10:30:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Interest rate tier or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Resource Not Found",
                          "message": "Interest rate tier not found with ID: 661e8400-e29b-41d4-a716-446655440999",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<ProductInterestDTO> updateInterestRate(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = "Interest rate tier ID to update",
                required = true,
                example = "661e8400-e29b-41d4-a716-446655440001"
            )
            @PathVariable String interestId,
            @Parameter(
                description = """
                    Updated tier configuration (complete object with changes).
                    
                    All fields can be modified except tier ID.
                    Validate balance range doesn't overlap with other tiers.
                    """,
                required = true
            )
            @Valid @RequestBody ProductInterestRequestDTO interestDto) {
        return ResponseEntity.ok(productInterestService.updateInterestRate(productCode, interestId, interestDto));
    }

    @DeleteMapping("/{interestId}")
    @Operation(
        summary = "Delete an interest rate tier (permanent removal)",
        description = """
            Permanently remove an interest rate tier from a product's rate structure.
            
            **⚠️ WARNING: This operation is IRREVERSIBLE**
            
            **What gets deleted:**
            - The interest rate tier configuration
            - Balance range definition
            - Rate percentage setting
            
            **What is NOT affected:**
            - Existing customer accounts (continue with locked-in rates)
            - Historical interest calculations
            - Audit trail of past transactions
            
            **When to Delete:**
            - Incorrectly created tier (duplicate or error)
            - Restructuring rate tiers completely
            - Removing outdated promotional tiers
            - Test data cleanup
            
            **Recommended Alternative:**
            Instead of deletion, consider:
            1. Set isActive = false using PUT endpoint
            2. Preserves configuration and history
            3. Can be reactivated if needed
            4. Maintains audit trail
            
            **Common Use Cases:**
            
            **Scenario 1: Rate Structure Simplification**
            - Originally had 5 tiers
            - Simplifying to 3 tiers
            - Delete unnecessary intermediate tiers
            
            **Scenario 2: Promotional Tier Removal**
            - Limited-time promotional rate ended
            - Rate no longer offered
            - Remove promotional tier configuration
            
            **Scenario 3: Duplicate Cleanup**
            - Accidentally created duplicate tier
            - Remove the duplicate
            - Keep correct configuration
            
            **❌ WRONG USE CASES:**
            - Temporarily disabling a tier → Use isActive=false
            - Rate change → Use PUT to update rate
            - End of promotion → Use isActive=false
            
            **Important Validation:**
            - Verify this won't leave gaps in coverage
            - Ensure remaining tiers cover full balance range
            - Check if any accounts are currently using this tier
            - Document reason for deletion
            
            **Related Endpoints:**
            - PUT /api/products/{code}/interest-rates/{id} - Update tier (preferred)
            - GET /api/products/{code}/interest-rates - View remaining tiers
            - POST /api/products/{code}/interest-rates - Add replacement tier
            """,
        tags = {"Product Interest Rates"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Interest rate tier deleted successfully. No content returned."
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Interest rate tier or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Tier Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Interest rate tier not found with ID: 661e8400-e29b-41d4-a716-446655440999",
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
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Cannot delete tier (future: has active accounts)",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Conflict",
                          "message": "Cannot delete tier: 1,234 accounts are currently using this rate tier. Set isActive=false instead.",
                          "timestamp": "2025-10-15T10:30:00",
                          "affectedAccounts": 1234
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
                          "message": "Failed to delete interest rate tier",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<Void> deleteInterestRate(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Interest rate tier ID to permanently delete.
                    
                    ⚠️ WARNING: This cannot be undone!
                    
                    Consider using PUT with isActive=false instead.
                    """,
                required = true,
                example = "661e8400-e29b-41d4-a716-446655440001"
            )
            @PathVariable String interestId) {
        productInterestService.deleteInterestRate(productCode, interestId);
        return ResponseEntity.noContent().build();
    }
}
