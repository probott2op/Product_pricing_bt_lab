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

import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productId}/communications")
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
            @PathVariable UUID productId,
            @Valid @RequestBody ProductCommunicationRequestDTO communicationDto) {
        return new ResponseEntity<>(productCommunicationService.addCommunicationToProduct(productId, communicationDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all communications for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Communications retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Page<ProductCommunicationDTO>> getCommunications(
            @PathVariable UUID productId,
            Pageable pageable) {
        return ResponseEntity.ok(productCommunicationService.getCommunicationsForProduct(productId, pageable));
    }

    @GetMapping("/{communicationId}")
    @Operation(summary = "Get a specific communication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Communication found"),
        @ApiResponse(responseCode = "404", description = "Communication or product not found")
    })
    public ResponseEntity<ProductCommunicationDTO> getCommunicationById(
            @PathVariable UUID productId,
            @PathVariable UUID communicationId) {
        return ResponseEntity.ok(productCommunicationService.getCommunicationById(productId, communicationId));
    }

    @PutMapping("/{communicationId}")
    @Operation(summary = "Update a communication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Communication updated successfully"),
        @ApiResponse(responseCode = "404", description = "Communication or product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid communication data")
    })
    public ResponseEntity<ProductCommunicationDTO> updateCommunication(
            @PathVariable UUID productId,
            @PathVariable UUID communicationId,
            @Valid @RequestBody ProductCommunicationRequestDTO communicationDto) {
        return ResponseEntity.ok(productCommunicationService.updateCommunication(productId, communicationId, communicationDto));
    }

    @DeleteMapping("/{communicationId}")
    @Operation(summary = "Delete a communication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Communication deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Communication or product not found")
    })
    public ResponseEntity<Void> deleteCommunication(
            @PathVariable UUID productId,
            @PathVariable UUID communicationId) {
        productCommunicationService.deleteCommunication(productId, communicationId);
        return ResponseEntity.noContent().build();
    }
}