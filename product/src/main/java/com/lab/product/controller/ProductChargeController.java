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
            - **FLAT**: Fixed amount (e.g., $5 monthly maintenance fee)
            - **PERCENTAGE**: Percentage of amount (e.g., 2% transaction fee)
            
            **Frequency Options:**
            - **ONE_TIME**: Charged once (account opening, closure)
            - **MONTHLY**: Charged every month
            - **QUARTERLY**: Charged every 3 months
            - **ANNUALLY**: Charged every year
            - **PER_OCCURRENCE**: Charged per event (per transaction, per overdraft)
            
            **Common Charge Examples:**
            
            **Savings Account:**
            - Minimum balance fee: $5 FLAT, MONTHLY
            - ATM fee: $2 FLAT, PER_OCCURRENCE
            - Account closure: $25 FLAT, ONE_TIME
            
            **Loan Product:**
            - Origination fee: 2% PERCENTAGE, ONE_TIME
            - Late payment penalty: $35 FLAT, PER_OCCURRENCE
            - Prepayment penalty: 1% PERCENTAGE, PER_OCCURRENCE
            
            **Current Account:**
            - Monthly maintenance: $10 FLAT, MONTHLY
            - Checkbook fee: $15 FLAT, PER_OCCURRENCE
            - Wire transfer: $25 FLAT, PER_OCCURRENCE
            
            **Use Cases:**
            
            **Scenario 1: Account Maintenance Fee**
            - Monthly fee for low balance accounts
            - $5 flat fee charged monthly
            - Waived if min balance maintained
            
            **Scenario 2: Transaction Charges**
            - Fee per wire transfer
            - $25 flat fee per occurrence
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
                        name = "Monthly Maintenance Fee",
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
                    ),
                    @ExampleObject(
                        name = "Loan Origination Fee",
                        value = """
                            {
                              "chargeId": "772e8400-e29b-41d4-a716-446655440002",
                              "chargeName": "Origination Fee",
                              "chargeType": "PERCENTAGE",
                              "chargeAmount": 2.00,
                              "chargeFrequency": "ONE_TIME",
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
                examples = @ExampleObject(
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
}