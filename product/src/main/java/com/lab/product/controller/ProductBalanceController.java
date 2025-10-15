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
    @Operation(summary = "Add a balance type to a product", 
               description = "Marks a specific balance type as applicable for this product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Balance type added successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid balance type or already exists")
    })
    public ResponseEntity<ProductBalanceDTO> addBalance(
            @PathVariable String productCode,
            @Valid @RequestBody ProductBalanceRequestDTO balanceDto) {
        return new ResponseEntity<>(productBalanceService.addBalanceToProduct(productCode, balanceDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all balance types for a product",
               description = "Returns all balance types that are applicable/supported for this product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balance types retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Page<ProductBalanceDTO>> getBalances(
            @PathVariable String productCode,
            Pageable pageable) {
        return ResponseEntity.ok(productBalanceService.getBalancesForProduct(productCode, pageable));
    }

    @GetMapping("/{balanceType}")
    @Operation(summary = "Get a specific balance type for a product",
               description = "Returns details of a specific balance type if it's applicable for this product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balance type found"),
        @ApiResponse(responseCode = "404", description = "Balance type not found or product not found")
    })
    public ResponseEntity<ProductBalanceDTO> getBalanceByType(
            @PathVariable String productCode,
            @PathVariable String balanceType) {
        return ResponseEntity.ok(productBalanceService.getBalanceByType(productCode, balanceType));
    }

    @PutMapping("/{balanceType}")
    @Operation(summary = "Update a balance type",
               description = "Update properties of a balance type for this product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balance type updated successfully"),
        @ApiResponse(responseCode = "404", description = "Balance type not found or product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid balance type data")
    })
    public ResponseEntity<ProductBalanceDTO> updateBalance(
            @PathVariable String productCode,
            @PathVariable String balanceType,
            @Valid @RequestBody ProductBalanceRequestDTO balanceDto) {
        return ResponseEntity.ok(productBalanceService.updateBalance(productCode, balanceType, balanceDto));
    }

    @DeleteMapping("/{balanceType}")
    @Operation(summary = "Remove a balance type from a product",
               description = "Mark a balance type as no longer applicable for this product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Balance type removed successfully"),
        @ApiResponse(responseCode = "404", description = "Balance type not found or product not found")
    })
    public ResponseEntity<Void> deleteBalance(
            @PathVariable String productCode,
            @PathVariable String balanceType) {
        productBalanceService.deleteBalance(productCode, balanceType);
        return ResponseEntity.noContent().build();
    }
}