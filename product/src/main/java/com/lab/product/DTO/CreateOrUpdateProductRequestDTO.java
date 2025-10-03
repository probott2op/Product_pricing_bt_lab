package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_CURRENCY;
import com.lab.product.entity.ENUMS.PRODUCT_STATUS;
import com.lab.product.entity.ENUMS.PRODUCT_TYPE;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;

@Data
public class CreateOrUpdateProductRequestDTO {
    @NotBlank(message = "Product code is required")
    private String productCode;
    
    @NotBlank(message = "Product name is required")
    private String productName;
    
    @NotNull(message = "Product type is required")
    private String productType;
    
    private String description;
    
    @NotNull(message = "Currency is required")
    private String currency;
    
    private String status = PRODUCT_STATUS.DRAFT.name(); // Default status using enum
    
    @NotNull(message = "Effective date is required")
    private Date efctv_date;
    
    private Date expr_date;

    // Custom validation method for business rules
    public void validate() {
        // Validate enum values
        try {
            if (productType != null) {
                PRODUCT_TYPE.valueOf(productType);
            }
            if (currency != null) {
                PRODUCT_CURRENCY.valueOf(currency);
            }
            if (status != null) {
                PRODUCT_STATUS.valueOf(status);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid enum value: " + e.getMessage());
        }

        // Validate dates
        if (efctv_date != null && expr_date != null && expr_date.before(efctv_date)) {
            throw new IllegalArgumentException("Expiry date must be after effective date");
        }
    }
}
