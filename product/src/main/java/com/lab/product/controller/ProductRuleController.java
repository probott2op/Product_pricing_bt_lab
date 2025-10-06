package com.lab.product.controller;

import com.lab.product.DTO.ProductRuleDTO;
import com.lab.product.DTO.ProductRuleRequestDTO;
import com.lab.product.service.ProductRuleService;
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
@RequestMapping("/api/products/{productCode}/rules")
@RequiredArgsConstructor
@Tag(name = "Product Rules", description = "Product rules management endpoints")
public class ProductRuleController {

    @Autowired
    private final ProductRuleService productRuleService;

    @PostMapping
    @Operation(summary = "Add a new rule to a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Rule created successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid rule data")
    })
    public ResponseEntity<ProductRuleDTO> addRule(
            @PathVariable String productCode,
            @Valid @RequestBody ProductRuleRequestDTO ruleDto) {
        return new ResponseEntity<>(productRuleService.addRuleToProduct(productCode, ruleDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all rules for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rules retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Page<ProductRuleDTO>> getRules(
            @PathVariable String productCode,
            Pageable pageable) {
        return ResponseEntity.ok(productRuleService.getRulesForProduct(productCode, pageable));
    }

    @GetMapping("/{ruleId}")
    @Operation(summary = "Get a specific rule")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rule found"),
        @ApiResponse(responseCode = "404", description = "Rule or product not found")
    })
    public ResponseEntity<ProductRuleDTO> getRuleById(
            @PathVariable String productCode,
            @PathVariable UUID ruleId) {
        return ResponseEntity.ok(productRuleService.getRuleById(productCode, ruleId));
    }

    @PutMapping("/{ruleId}")
    @Operation(summary = "Update a rule")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rule updated successfully"),
        @ApiResponse(responseCode = "404", description = "Rule or product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid rule data")
    })
    public ResponseEntity<ProductRuleDTO> updateRule(
            @PathVariable String productCode,
            @PathVariable UUID ruleId,
            @Valid @RequestBody ProductRuleRequestDTO ruleDto) {
        return ResponseEntity.ok(productRuleService.updateRule(productCode, ruleId, ruleDto));
    }

    @DeleteMapping("/{ruleId}")
    @Operation(summary = "Delete a rule")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Rule deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Rule or product not found")
    })
    public ResponseEntity<Void> deleteRule(
            @PathVariable String productCode,
            @PathVariable UUID ruleId) {
        productRuleService.deleteRule(productCode, ruleId);
        return ResponseEntity.noContent().build();
    }
}