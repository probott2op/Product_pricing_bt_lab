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
@RestController
@RequestMapping("/api/products/{productCode}/rules")
@RequiredArgsConstructor
@Tag(
    name = "Product Business Rules",
    description = "This API is the definitive source for managing all business rules that govern a financial product."+
            " It allows you to configure these rules as key attributes, including balance limits (min/max),"+
            " conditional interest rate bonuses (e.g., for 'Gold members' or 'under 18'), and the primary interest"+
            " calculation method (simple vs. compound with its frequency). It's the central hub for a product's entire"+
            " configurable DNA and operational logic."
)
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

    @GetMapping("/{ruleCode}")
    @Operation(summary = "Get a specific rule")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rule found"),
        @ApiResponse(responseCode = "404", description = "Rule or product not found")
    })
    public ResponseEntity<ProductRuleDTO> getRuleByCode(
            @PathVariable String productCode,
            @PathVariable String ruleCode) {
        return ResponseEntity.ok(productRuleService.getRuleByCode(productCode, ruleCode));
    }

    @PutMapping("/{ruleCode}")
    @Operation(summary = "Update a rule")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rule updated successfully"),
        @ApiResponse(responseCode = "404", description = "Rule or product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid rule data")
    })
    public ResponseEntity<ProductRuleDTO> updateRule(
            @PathVariable String productCode,
            @PathVariable String ruleCode,
            @Valid @RequestBody ProductRuleRequestDTO ruleDto) {
        return ResponseEntity.ok(productRuleService.updateRule(productCode, ruleCode, ruleDto));
    }

    @DeleteMapping("/{ruleCode}")
    @Operation(summary = "Delete a rule")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Rule deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Rule or product not found")
    })
    public ResponseEntity<Void> deleteRule(
            @PathVariable String productCode,
            @PathVariable String ruleCode) {
        productRuleService.deleteRule(productCode, ruleCode);
        return ResponseEntity.noContent().build();
    }
}