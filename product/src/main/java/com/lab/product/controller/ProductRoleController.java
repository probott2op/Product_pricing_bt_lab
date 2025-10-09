package com.lab.product.controller;

import com.lab.product.DTO.ProductRoleDTO;
import com.lab.product.DTO.ProductRoleRequestDTO;
import com.lab.product.service.ProductRoleService;
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
@RequestMapping("/api/products/{productCode}/roles")
@RequiredArgsConstructor
@Tag(name = "Product Roles", description = "Product roles management endpoints")
public class ProductRoleController {

    @Autowired
    private final ProductRoleService productRoleService;

    @PostMapping
    @Operation(summary = "Add a new role to a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Role created successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid role data")
    })
    public ResponseEntity<ProductRoleDTO> addRole(
            @PathVariable String productCode,
            @Valid @RequestBody ProductRoleRequestDTO roleDto) {
        return new ResponseEntity<>(productRoleService.addRoleToProduct(productCode, roleDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all roles for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Page<ProductRoleDTO>> getRoles(
            @PathVariable String productCode,
            Pageable pageable) {
        return ResponseEntity.ok(productRoleService.getRolesForProduct(productCode, pageable));
    }

    @GetMapping("/{roleCode}")
    @Operation(summary = "Get a specific role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role found"),
        @ApiResponse(responseCode = "404", description = "Role or product not found")
    })
    public ResponseEntity<ProductRoleDTO> getRoleByCode(
            @PathVariable String productCode,
            @PathVariable String roleCode) {
        return ResponseEntity.ok(productRoleService.getRoleByCode(productCode, roleCode));
    }

    @PutMapping("/{roleCode}")
    @Operation(summary = "Update a role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role updated successfully"),
        @ApiResponse(responseCode = "404", description = "Role or product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid role data")
    })
    public ResponseEntity<ProductRoleDTO> updateRole(
            @PathVariable String productCode,
            @PathVariable String roleCode,
            @Valid @RequestBody ProductRoleRequestDTO roleDto) {
        return ResponseEntity.ok(productRoleService.updateRole(productCode, roleCode, roleDto));
    }

    @DeleteMapping("/{roleCode}")
    @Operation(summary = "Delete a role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Role or product not found")
    })
    public ResponseEntity<Void> deleteRole(
            @PathVariable String productCode,
            @PathVariable String roleCode) {
        productRoleService.deleteRole(productCode, roleCode);
        return ResponseEntity.noContent().build();
    }
}