package com.lab.product.controller;

import com.lab.product.DTO.ProductChargeDTO;
import com.lab.product.DTO.ProductChargeRequestDTO;
import com.lab.product.service.ProductChargeService;
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

@RestController
@RequestMapping("/api/products/{productCode}/charges")
@RequiredArgsConstructor
@Tag(
    name = "Product Charges & Fees",
    description = "Comprehensive API for managing product charges, fees, and pricing structures. " +
                  "Charges include account maintenance fees, transaction fees, processing charges, penalty fees, " +
                  "and service charges. Supports various charge types (FIXED, PERCENTAGE), calculation methods, " +
                  "frequency configurations (ONE_TIME, MONTHLY, QUARTERLY, ANNUALLY), and debit/credit indicators. " +
                  "Essential for revenue generation and transparent customer pricing."
)
public class ProductChargeController {

    @Autowired
    private final ProductChargeService productChargeService;

    @PostMapping
    @Operation(
        summary = "Configure fee or charge for product",
        description = """
            Add a new fee or charge to a product's pricing structure. Charges can be flat amounts or
            percentage-based, with various frequency options.
            
            **Charge Types:**
            - **FLAT**: Fixed amount (e.g., ₹500 monthly maintenance fee)
            - **PERCENTAGE**: Percentage of amount (e.g., 2% transaction fee)
            
            **Frequency Options:**
            - **ONE_TIME**: Charged once (account opening, closure)
            - **MONTHLY**: Charged every month
            - **QUARTERLY**: Charged every 3 months
            - **ANNUALLY**: Charged every year
            - **PER_OCCURRENCE**: Charged per event (per transaction, per overdraft)
            
            **Common Charge Examples:**
            
            **Savings Account:**
            - Minimum balance fee: ₹500 FLAT, MONTHLY
            - ATM fee: ₹200 FLAT, PER_OCCURRENCE
            - Account closure: ₹2,500 FLAT, ONE_TIME
            
            **Loan Product:**
            - Origination fee: 2% PERCENTAGE, ONE_TIME
            - Late payment penalty: ₹3,500 FLAT, PER_OCCURRENCE
            - Prepayment penalty: 1% PERCENTAGE, PER_OCCURRENCE
            
            **Current Account:**
            - Monthly maintenance: ₹1,000 FLAT, MONTHLY
            - Checkbook fee: ₹1,500 FLAT, PER_OCCURRENCE
            - Wire transfer: ₹2,500 FLAT, PER_OCCURRENCE
            
            **Use Cases:**
            
            **Scenario 1: Account Maintenance Fee**
            - Monthly fee for low balance accounts
            - ₹500 flat fee charged monthly
            - Waived if min balance maintained
            
            **Scenario 2: Transaction Charges**
            - Fee per wire transfer
            - ₹2,500 flat fee per occurrence
            - Applied immediately on transaction
            
            **Scenario 3: Loan Origination Fee**
            - One-time setup fee
            - 2% of loan amount
            - Charged when loan disbursed
            
            **Related Endpoints:**
            - GET /api/products/{code}/charges - View all charges
            - PUT /api/products/{code}/charges/{id} - Update charge
            - DELETE /api/products/{code}/charges/{id} - Remove charge
            """,
        tags = {"Product Charges & Fees"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Charge created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductChargeDTO.class),
                examples = {
                    @ExampleObject(
                        name = "FD001 Quarterly Fee Created",
                        summary = "Fixed Deposit quarterly maintenance fee",
                        value = """
                            {
                              "chargeId": "13c4bb9f-ae57-4c3c-95fd-2ae81be8c1e1",
                              "chargeCode": "FEE001",
                              "chargeName": "FD Quarterly Fee",
                              "chargeType": "FLAT",
                              "chargeAmount": 200.00,
                              "chargeFrequency": "QUARTERLY",
                              "isActive": true,
                              "createdAt": "2025-10-15T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "FD001 Low Penalty Created",
                        summary = "Fixed Deposit premature withdrawal penalty (low)",
                        value = """
                            {
                              "chargeId": "f72c49ef-e5eb-442c-b76d-14ad7e3ecbbf",
                              "chargeCode": "PEN-L-001",
                              "chargeName": "Penalty for premature withdrawal of FD - low",
                              "chargeType": "PERCENTAGE",
                              "chargeAmount": 0.5,
                              "chargeFrequency": "PER_OCCURRENCE",
                              "isActive": true,
                              "createdAt": "2025-10-15T10:31:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Savings Maintenance Fee",
                        value = """
                            {
                              "chargeId": "771e8400-e29b-41d4-a716-446655440001",
                              "chargeName": "Minimum Balance Fee",
                              "chargeType": "FLAT",
                              "chargeAmount": 5.00,
                              "chargeFrequency": "MONTHLY",
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
            description = "Invalid charge data",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Validation Failed",
                          "message": "chargeName is required",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
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
    public ResponseEntity<ProductChargeDTO> addCharge(
            @Parameter(description = "Product code", required = true, example = "SAV-HIGH-YIELD-2025")
            @PathVariable String productCode,
            @Parameter(description = "Charge configuration", required = true)
            @Valid @RequestBody ProductChargeRequestDTO chargeDto) {
        return new ResponseEntity<>(productChargeService.addChargeToProduct(productCode, chargeDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "Retrieve all fees and charges for product",
        description = """
            Get complete list of all fees and charges configured for this product.
            
            **Use Cases:**
            
            **Scenario 1: Display Fee Schedule to Customer**
            - Show all applicable fees
            - Display charge amounts and frequency
            - Provide transparent pricing
            
            **Scenario 2: Fee Calculation**
            - Retrieve applicable charges
            - Calculate total fees for account
            - Apply charges based on frequency
            
            **Scenario 3: Admin Fee Review**
            - Review current fee structure
            - Compare with competitors
            - Adjust pricing strategy
            """,
        tags = {"Product Charges & Fees"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Charges retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class),
                examples = {
                    @ExampleObject(
                        name = "FD001 Charges",
                        summary = "Fixed Deposit under 500000 - Penalty and maintenance fees",
                        value = """
                            {
                              "content": [
                                {
                                  "chargeId": "31435394-2af5-4118-9cc7-4616df6a64ee",
                                  "chargeCode": "FEE001",
                                  "chargeType": "FEE",
                                  "calculationType": "FLAT",
                                  "frequency": "QUARTERLY",
                                  "amount": 200
                                },
                                {
                                  "chargeId": "9953d914-423c-45af-936b-ebffbdd709be",
                                  "chargeCode": "PEN-L-001",
                                  "chargeType": "PENALTY",
                                  "calculationType": "PERCENTAGE",
                                  "frequency": "ONE_TIME",
                                  "amount": 0.5
                                },
                                {
                                  "chargeId": "e0167e4b-0102-4d8c-ad37-02ddb1e0f83d",
                                  "chargeCode": "PEN-H-001",
                                  "chargeType": "PENALTY",
                                  "calculationType": "PERCENTAGE",
                                  "frequency": "ONE_TIME",
                                  "amount": 1
                                }
                              ],
                              "totalElements": 3,
                              "totalPages": 1
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Savings Account Charges",
                        summary = "Standard savings account fees",
                        value = """
                            {
                              "content": [
                                {
                                  "chargeId": "771e8400-e29b-41d4-a716-446655440001",
                                  "chargeName": "Minimum Balance Fee",
                                  "chargeType": "FLAT",
                                  "chargeAmount": 5.00,
                                  "chargeFrequency": "MONTHLY",
                                  "isActive": true
                                },
                                {
                                  "chargeId": "772e8400-e29b-41d4-a716-446655440002",
                                  "chargeName": "ATM Withdrawal Fee",
                                  "chargeType": "FLAT",
                                  "chargeAmount": 2.00,
                                  "chargeFrequency": "PER_OCCURRENCE",
                                  "isActive": true
                                }
                              ],
                              "totalElements": 2,
                              "totalPages": 1
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Page<ProductChargeDTO>> getCharges(
            @Parameter(description = "Product code", required = true)
            @PathVariable String productCode,
            Pageable pageable) {
        return ResponseEntity.ok(productChargeService.getChargesForProduct(productCode, pageable));
    }

    @GetMapping("/{chargeId}")
    @Operation(
        summary = "Retrieve specific charge details",
        description = """
            Get detailed information about a specific fee or charge.
            
            **Use Cases:**
            - Verify charge configuration
            - Review charge details before update
            - Check charge amount and frequency
            """,
        tags = {"Product Charges & Fees"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Charge found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductChargeDTO.class),
                examples = @ExampleObject(
                    value = """
                        {
                          "chargeId": "771e8400-e29b-41d4-a716-446655440001",
                          "chargeName": "Minimum Balance Fee",
                          "chargeType": "FLAT",
                          "chargeAmount": 5.00,
                          "chargeFrequency": "MONTHLY",
                          "isActive": true,
                          "createdAt": "2025-01-01T00:00:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Charge or product not found")
    })
    public ResponseEntity<ProductChargeDTO> getChargeByCode(
            @Parameter(description = "Product code", required = true)
            @PathVariable String productCode,
            @Parameter(description = "Charge ID", required = true)
            @PathVariable String chargeId) {
        return ResponseEntity.ok(productChargeService.getChargeByCode(productCode, chargeId));
    }

    @PutMapping("/{chargeId}")
    @Operation(
        summary = "Update fee or charge configuration",
        description = """
            Modify an existing charge's amount, type, frequency, or status.
            
            **Common Updates:**
            - Adjust fee amount (rate changes)
            - Change frequency (monthly to quarterly)
            - Disable charge (set isActive=false)
            - Modify charge type
            
            **Use Cases:**
            - Market-based fee adjustments
            - Promotional fee waivers
            - Regulatory compliance changes
            """,
        tags = {"Product Charges & Fees"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Charge updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductChargeDTO.class),
                examples = @ExampleObject(
                    value = """
                        {
                          "chargeId": "771e8400-e29b-41d4-a716-446655440001",
                          "chargeName": "Minimum Balance Fee",
                          "chargeType": "FLAT",
                          "chargeAmount": 7.00,
                          "chargeFrequency": "MONTHLY",
                          "isActive": true,
                          "updatedAt": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Charge or product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid charge data")
    })
    public ResponseEntity<ProductChargeDTO> updateCharge(
            @Parameter(description = "Product code", required = true)
            @PathVariable String productCode,
            @Parameter(description = "Charge ID to update", required = true)
            @PathVariable String chargeId,
            @Parameter(description = "Updated charge configuration", required = true)
            @Valid @RequestBody ProductChargeRequestDTO chargeDto) {
        return ResponseEntity.ok(productChargeService.updateCharge(productCode, chargeId, chargeDto));
    }

    @DeleteMapping("/{chargeId}")
    @Operation(
        summary = "Remove charge from product (permanent)",
        description = """
            Permanently delete a fee or charge from product configuration.
            
            **⚠️ WARNING: Irreversible operation**
            
            **Recommended Alternative:**
            - Set isActive=false using PUT endpoint
            - Preserves configuration and history
            - Can be reactivated if needed
            
            **When to Delete:**
            - Incorrectly configured charge
            - Duplicate charges
            - Test data cleanup
            
            **Use Cases:**
            - Remove obsolete fees
            - Clean up incorrect configuration
            - Simplify fee structure
            """,
        tags = {"Product Charges & Fees"}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Charge deleted successfully"),
        @ApiResponse(
            responseCode = "404",
            description = "Charge or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "error": "Resource Not Found",
                          "message": "Charge not found with ID: 771e8400-invalid",
                          "timestamp": "2025-10-15T10:30:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<Void> deleteCharge(
            @Parameter(description = "Product code", required = true)
            @PathVariable String productCode,
            @Parameter(description = "Charge ID to delete (⚠️ Cannot be undone!)", required = true)
            @PathVariable String chargeId) {
        productChargeService.deleteCharge(productCode, chargeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/audit-trail")
    @Operation(
        summary = "Get complete audit trail of all charges for a product",
        description = "Retrieve ALL versions of ALL charges for a product for audit purposes.",
        tags = {"Product Charges & Fees"}
    )
    public ResponseEntity<List<ProductChargeDTO>> getChargesAuditTrail(
            @Parameter(description = "Product code", required = true, example = "FD001")
            @PathVariable String productCode) {
        return ResponseEntity.ok(productChargeService.getChargesAuditTrail(productCode));
    }

    @GetMapping("/{chargeCode}/audit-trail")
    @Operation(
        summary = "Get complete audit trail of a specific charge",
        description = "Retrieve ALL versions of a specific charge for audit purposes.",
        tags = {"Product Charges & Fees"}
    )
    public ResponseEntity<List<ProductChargeDTO>> getChargeAuditTrail(
            @Parameter(description = "Product code", required = true, example = "FD001")
            @PathVariable String productCode,
            @Parameter(description = "Charge code", required = true, example = "FEE001")
            @PathVariable String chargeCode) {
        return ResponseEntity.ok(productChargeService.getChargeAuditTrail(productCode, chargeCode));
    }
}