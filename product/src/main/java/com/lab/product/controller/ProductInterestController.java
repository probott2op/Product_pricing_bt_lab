package com.lab.product.controller;

import com.lab.product.DTO.ProductInterestDTO;
import com.lab.product.DTO.ProductInterestRequestDTO;
import com.lab.product.service.ProductInterestService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productCode}/interest-rates")
@RequiredArgsConstructor
@Tag(name = "Product Interest Rates", description = "Product interest rates management endpoints")
public class ProductInterestController {

    @Autowired
    private final ProductInterestService productInterestService;

    @PostMapping
    @Operation(summary = "Add a new interest rate to a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Interest rate created successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid interest rate data")
    })
    public ResponseEntity<ProductInterestDTO> addInterestRate(
            @PathVariable String productCode,
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
