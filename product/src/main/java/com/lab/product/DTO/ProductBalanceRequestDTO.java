package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_BALANCE_TYPE;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductBalanceRequestDTO {
    @NotBlank(message = "Balance code is required")
    private String balanceCode;
    
    @NotBlank(message = "Balance name is required")
    private String balanceName;
    
    @NotNull(message = "Balance type is required")
    private PRODUCT_BALANCE_TYPE balanceType;
    
    private String description;
    private boolean isActive = true;
}