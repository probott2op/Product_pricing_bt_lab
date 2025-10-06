package com.lab.product.controller;

import com.lab.product.DTO.ProductBalanceDTO;
import com.lab.product.DTO.ProductBalanceRequestDTO;
import com.lab.product.service.ProductBalanceService;
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
@RequestMapping("/api/products/{productCode}/balances")
@RequiredArgsConstructor
@Tag(name = "Product Balances", description = "Product balances management endpoints")
public class ProductBalanceController {

    @Autowired
    private final ProductBalanceService productBalanceService;

    @PostMapping
    @Operation(summary = "Add a new balance to a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Balance created successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid balance data")
    })
    public ResponseEntity<ProductBalanceDTO> addBalance(
            @PathVariable String productCode,
            @Valid @RequestBody ProductBalanceRequestDTO balanceDto) {
        return new ResponseEntity<>(productBalanceService.addBalanceToProduct(productCode, balanceDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all balances for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balances retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Page<ProductBalanceDTO>> getBalances(
            @PathVariable String productCode,
            Pageable pageable) {
        return ResponseEntity.ok(productBalanceService.getBalancesForProduct(productCode, pageable));
    }

    @GetMapping("/{balanceId}")
    @Operation(summary = "Get a specific balance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balance found"),
        @ApiResponse(responseCode = "404", description = "Balance or product not found")
    })
    public ResponseEntity<ProductBalanceDTO> getBalanceById(
            @PathVariable String productCode,
            @PathVariable UUID balanceId) {
        return ResponseEntity.ok(productBalanceService.getBalanceById(productCode, balanceId));
    }

    @PutMapping("/{balanceId}")
    @Operation(summary = "Update a balance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balance updated successfully"),
        @ApiResponse(responseCode = "404", description = "Balance or product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid balance data")
    })
    public ResponseEntity<ProductBalanceDTO> updateBalance(
            @PathVariable String productCode,
            @PathVariable UUID balanceId,
            @Valid @RequestBody ProductBalanceRequestDTO balanceDto) {
        return ResponseEntity.ok(productBalanceService.updateBalance(productCode, balanceId, balanceDto));
    }

    @DeleteMapping("/{balanceId}")
    @Operation(summary = "Delete a balance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Balance deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Balance or product not found")
    })
    public ResponseEntity<Void> deleteBalance(
            @PathVariable String productCode,
            @PathVariable UUID balanceId) {
        productBalanceService.deleteBalance(productCode, balanceId);
        return ResponseEntity.noContent().build();
    }
}