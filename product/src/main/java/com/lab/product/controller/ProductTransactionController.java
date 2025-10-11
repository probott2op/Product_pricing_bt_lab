package com.lab.product.controller;

import com.lab.product.DTO.ProductTransactionDTO;
import com.lab.product.DTO.ProductTransactionRequestDTO;
import com.lab.product.service.ProductTransactionService;
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
    @Operation(summary = "Add a new transaction to a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid transaction data")
    })
    public ResponseEntity<ProductTransactionDTO> addTransaction(
            @PathVariable String productCode,
            @Valid @RequestBody ProductTransactionRequestDTO transactionDto) {
        return new ResponseEntity<>(productTransactionService.addTransactionToProduct(productCode, transactionDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all transactions for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Page<ProductTransactionDTO>> getTransactions(
            @PathVariable String productCode,
            Pageable pageable) {
        return ResponseEntity.ok(productTransactionService.getTransactionsForProduct(productCode, pageable));
    }

    @GetMapping("/{transactionCode}")
    @Operation(summary = "Get a specific transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction found"),
        @ApiResponse(responseCode = "404", description = "Transaction or product not found")
    })
    public ResponseEntity<ProductTransactionDTO> getTransactionByCode(
            @PathVariable String productCode,
            @PathVariable String transactionCode) {
        return ResponseEntity.ok(productTransactionService.getTransactionByCode(productCode, transactionCode));
    }

    @PutMapping("/{transactionCode}")
    @Operation(summary = "Update a transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
        @ApiResponse(responseCode = "404", description = "Transaction or product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid transaction data")
    })
    public ResponseEntity<ProductTransactionDTO> updateTransaction(
            @PathVariable String productCode,
            @PathVariable String transactionCode,
            @Valid @RequestBody ProductTransactionRequestDTO transactionDto) {
        return ResponseEntity.ok(productTransactionService.updateTransaction(productCode, transactionCode, transactionDto));
    }

    @DeleteMapping("/{transactionCode}")
    @Operation(summary = "Delete a transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Transaction or product not found")
    })
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable String productCode,
            @PathVariable String transactionCode) {
        productTransactionService.deleteTransaction(productCode, transactionCode);
        return ResponseEntity.noContent().build();
    }
}