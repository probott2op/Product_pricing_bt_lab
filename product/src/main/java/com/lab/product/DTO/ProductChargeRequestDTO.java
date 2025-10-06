package com.lab.product.DTO;

import java.math.BigDecimal;

import com.lab.product.entity.ENUMS.PRODUCT_CHARGE_CALCULATION_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_CHARGE_FREQUENCY;
import com.lab.product.entity.ENUMS.PRODUCT_CHARGE_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_DebitCredit;

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
    private PRODUCT_CHARGE_TYPE chargeType;
    
    @NotNull(message = "Calculation type is required")
    private PRODUCT_CHARGE_CALCULATION_TYPE calculationType;
    
    @NotNull(message = "Frequency is required")
    private PRODUCT_CHARGE_FREQUENCY frequency;

    @Positive(message = "Charge value must be positive")
    private BigDecimal chargeValue;

    private PRODUCT_DebitCredit debitCredit;

}