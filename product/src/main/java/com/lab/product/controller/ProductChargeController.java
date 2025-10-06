package com.lab.product.controller;

import com.lab.product.DTO.ProductChargeDTO;
import com.lab.product.DTO.ProductChargeRequestDTO;
import com.lab.product.service.ProductChargeService;
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
@RequestMapping("/api/products/{productCode}/charges")
@RequiredArgsConstructor
@Tag(name = "Product Charges", description = "Product charges management endpoints")
public class ProductChargeController {

    @Autowired
    private final ProductChargeService productChargeService;

    @PostMapping
    @Operation(summary = "Add a new charge to a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Charge created successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid charge data")
    })
    public ResponseEntity<ProductChargeDTO> addCharge(
            @PathVariable String productCode,
            @Valid @RequestBody ProductChargeRequestDTO chargeDto) {
        return new ResponseEntity<>(productChargeService.addChargeToProduct(productCode, chargeDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all charges for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Charges retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Page<ProductChargeDTO>> getCharges(
            @PathVariable String productCode,
            Pageable pageable) {
        return ResponseEntity.ok(productChargeService.getChargesForProduct(productCode, pageable));
    }

    @GetMapping("/{chargeId}")
    @Operation(summary = "Get a specific charge")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Charge found"),
        @ApiResponse(responseCode = "404", description = "Charge or product not found")
    })
    public ResponseEntity<ProductChargeDTO> getChargeById(
            @PathVariable String productCode,
            @PathVariable UUID chargeId) {
        return ResponseEntity.ok(productChargeService.getChargeById(productCode, chargeId));
    }

    @PutMapping("/{chargeId}")
    @Operation(summary = "Update a charge")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Charge updated successfully"),
        @ApiResponse(responseCode = "404", description = "Charge or product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid charge data")
    })
    public ResponseEntity<ProductChargeDTO> updateCharge(
            @PathVariable String productCode,
            @PathVariable UUID chargeId,
            @Valid @RequestBody ProductChargeRequestDTO chargeDto) {
        return ResponseEntity.ok(productChargeService.updateCharge(productCode, chargeId, chargeDto));
    }

    @DeleteMapping("/{chargeId}")
    @Operation(summary = "Delete a charge")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Charge deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Charge or product not found")
    })
    public ResponseEntity<Void> deleteCharge(
            @PathVariable String productCode,
            @PathVariable UUID chargeId) {
        productChargeService.deleteCharge(productCode, chargeId);
        return ResponseEntity.noContent().build();
    }
}