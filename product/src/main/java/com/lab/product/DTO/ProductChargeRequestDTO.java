package com.lab.product.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductChargeRequestDTO {
    @NotBlank(message = "Charge code is required")
    private String chargeCode;
    
    @NotBlank(message = "Charge name is required")
    private String chargeName;
    
    @NotNull(message = "Charge type is required")
    private String chargeType;
    
    @NotNull(message = "Calculation type is required")
    private String calculationType;
    
    @NotNull(message = "Frequency is required")
    private String frequency;
    
    @Positive(message = "Amount must be positive")
    private Double amount;
    
    @Positive(message = "Percentage must be positive")
    private Double percentage;
    
    private String description;
    private boolean isActive = true;
}