package com.lab.product.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRoleRequestDTO {
    @NotBlank(message = "Role code is required")
    private String roleCode;
    
    @NotBlank(message = "Role name is required")
    private String roleName;
    
    @NotNull(message = "Role type is required")
    private String roleType;
    
    private String description;
    private boolean isActive = true;
}