package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_RULE_DATA;
import com.lab.product.entity.ENUMS.PRODUCT_RULE_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_RULE_VALIDATION;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRuleRequestDTO {

    @NotBlank(message = "Rule code is required")
    private String ruleCode;
    
    @NotBlank(message = "Rule name is required")
    private String ruleName;
    
    @NotNull(message = "Rule type is required")
    private PRODUCT_RULE_TYPE ruleType;
    
    @NotNull(message = "Data type is required")
    private PRODUCT_RULE_DATA dataType;
    
    @NotBlank(message = "Rule value is required")
    private String ruleValue;

    @NotNull(message = "Validation type is required")
    private PRODUCT_RULE_VALIDATION validationType;
    
}