package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_CURRENCY;
import com.lab.product.entity.ENUMS.PRODUCT_STATUS;
import com.lab.product.entity.ENUMS.PRODUCT_TYPE;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;

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

    // Getters and Setters
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getEfctv_date() {
        return efctv_date;
    }

    public void setEfctv_date(Date efctv_date) {
        this.efctv_date = efctv_date;
    }

    public Date getExpr_date() {
        return expr_date;
    }

    public void setExpr_date(Date expr_date) {
        this.expr_date = expr_date;
    }
}
