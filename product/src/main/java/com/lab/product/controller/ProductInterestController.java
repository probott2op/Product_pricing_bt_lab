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
        summary = "Add a new interest rate configuration to a product",
        description = "Adds a specific interest rate to a product's rate card."+
                " This configuration is focused solely on the duration of the term and the credit frequency,"+
                " linking them to a specific interest percentage. The underlying calculation rules (e.g., simple/compound) and compounding frequency"+
                " are managed at the product level.",
        tags = {"Product Interest Rates"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Interest rate created successfully. Returns the complete rate configuration with system-generated ID.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductInterestDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid interest rate data. Possible reasons: " +
                          "- Missing required fields (rateCode, rateType, interestRate) " +
                          "- Invalid enum values " +
                          "- Rate code already exists for this product " +
                          "- Invalid date range " +
                          "- Invalid balance range (min > max)",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found with the specified product code",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ProductInterestDTO> addInterestRate(
            @Parameter(
                description = "Product code to which the interest rate will be added",
                required = true,
                example = "SAV001"
            )
            @PathVariable String productCode,
            @Parameter(
                description = "Interest rate configuration details including rate code, type, percentage, calculation method, " +
                              "balance ranges, and effective dates",
                required = true
            )
            @Valid @RequestBody ProductInterestRequestDTO interestDto) {
        return new ResponseEntity<>(productInterestService.addInterestToProduct(productCode, interestDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all interest rates for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Interest rates retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Page<ProductInterestDTO>> getInterestRates(
            @PathVariable String productCode,
            Pageable pageable) {
        return ResponseEntity.ok(productInterestService.getInterestRatesForProduct(productCode, pageable));
    }

    @GetMapping("/{rateCode}")
    @Operation(summary = "Get a specific interest rate")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Interest rate found"),
        @ApiResponse(responseCode = "404", description = "Interest rate or product not found")
    })
    public ResponseEntity<ProductInterestDTO> getInterestRateByCode(
            @PathVariable String productCode,
            @PathVariable String rateCode) {
        return ResponseEntity.ok(productInterestService.getInterestRateByCode(productCode, rateCode));
    }

    @PutMapping("/{rateCode}")
    @Operation(summary = "Update an interest rate")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Interest rate updated successfully"),
        @ApiResponse(responseCode = "404", description = "Interest rate or product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid interest rate data")
    })
    public ResponseEntity<ProductInterestDTO> updateInterestRate(
            @PathVariable String productCode,
            @PathVariable String rateCode,
            @Valid @RequestBody ProductInterestRequestDTO interestDto) {
        return ResponseEntity.ok(productInterestService.updateInterestRate(productCode, rateCode, interestDto));
    }

    @DeleteMapping("/{rateCode}")
    @Operation(summary = "Delete an interest rate")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Interest rate deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Interest rate or product not found")
    })
    public ResponseEntity<Void> deleteInterestRate(
            @PathVariable String productCode,
            @PathVariable String rateCode) {
        productInterestService.deleteInterestRate(productCode, rateCode);
        return ResponseEntity.noContent().build();
    }
}
