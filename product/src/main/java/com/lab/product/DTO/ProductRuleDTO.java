package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_RULE_DATA;
import com.lab.product.entity.ENUMS.PRODUCT_RULE_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_RULE_VALIDATION;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductRuleDTO {
    private UUID ruleId;
    private String ruleCode;
    private String ruleName;
    private PRODUCT_RULE_TYPE ruleType;
    private PRODUCT_RULE_DATA dataType;
    private String ruleValue; // could be JSON
    private PRODUCT_RULE_VALIDATION validationType;
}
