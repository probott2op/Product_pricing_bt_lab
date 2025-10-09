package com.lab.product.controller;

import com.lab.product.DTO.ProductCommunicationDTO;
import com.lab.product.DTO.ProductCommunicationRequestDTO;
import com.lab.product.service.ProductCommunicationService;
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
@RequestMapping("/api/products/{productCode}/communications")
@RequiredArgsConstructor
@Tag(name = "Product Communications", description = "Product communications management endpoints")
public class ProductCommunicationController {

    @Autowired
    private final ProductCommunicationService productCommunicationService;

    @PostMapping
    @Operation(summary = "Add a new communication to a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Communication created successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid communication data")
    })
    public ResponseEntity<ProductCommunicationDTO> addCommunication(
            @PathVariable String productCode,
            @Valid @RequestBody ProductCommunicationRequestDTO communicationDto) {
        return new ResponseEntity<>(productCommunicationService.addCommunicationToProduct(productCode, communicationDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all communications for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Communications retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Page<ProductCommunicationDTO>> getCommunications(
            @PathVariable String productCode,
            Pageable pageable) {
        return ResponseEntity.ok(productCommunicationService.getCommunicationsForProduct(productCode, pageable));
    }

    @GetMapping("/{commCode}")
    @Operation(summary = "Get a specific communication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Communication found"),
        @ApiResponse(responseCode = "404", description = "Communication or product not found")
    })
    public ResponseEntity<ProductCommunicationDTO> getCommunicationByCode(
            @PathVariable String productCode,
            @PathVariable String commCode) {
        return ResponseEntity.ok(productCommunicationService.getCommunicationByCode(productCode, commCode));
    }

    @PutMapping("/{commCode}")
    @Operation(summary = "Update a communication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Communication updated successfully"),
        @ApiResponse(responseCode = "404", description = "Communication or product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid communication data")
    })
    public ResponseEntity<ProductCommunicationDTO> updateCommunication(
            @PathVariable String productCode,
            @PathVariable String commCode,
            @Valid @RequestBody ProductCommunicationRequestDTO communicationDto) {
        return ResponseEntity.ok(productCommunicationService.updateCommunication(productCode, commCode, communicationDto));
    }

    @DeleteMapping("/{commCode}")
    @Operation(summary = "Delete a communication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Communication deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Communication or product not found")
    })
    public ResponseEntity<Void> deleteCommunication(
            @PathVariable String productCode,
            @PathVariable String commCode) {
        productCommunicationService.deleteCommunication(productCode, commCode);
        return ResponseEntity.noContent().build();
    }
}