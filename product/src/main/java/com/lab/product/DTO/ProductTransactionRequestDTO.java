package com.lab.product.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductTransactionRequestDTO {
    @NotBlank(message = "Transaction code is required")
    private String transactionCode;
    
    @NotBlank(message = "Transaction name is required")
    private String transactionName;
    
    @NotNull(message = "Transaction type is required")
    private String transactionType;
    
    @NotNull(message = "Debit/Credit indicator is required")
    private String debitCreditIndicator;
    
    @Positive(message = "Minimum amount must be positive")
    private Double minimumAmount;
    
    @Positive(message = "Maximum amount must be positive")
    private Double maximumAmount;
    
    private String description;
    private boolean isActive = true;
}