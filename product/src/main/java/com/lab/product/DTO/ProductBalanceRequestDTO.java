package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_BALANCE_TYPE;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO for creating/updating Product Balance
 * Used to specify which balance types are applicable for a product
 * Example: A Loan product would have LOAN_PRINCIPAL, LOAN_INTEREST, PENALTY
 */
@Data
public class ProductBalanceRequestDTO {
    
    @NotNull(message = "Balance type is required")
    private PRODUCT_BALANCE_TYPE balanceType;
    
    private Boolean isActive = true;
}