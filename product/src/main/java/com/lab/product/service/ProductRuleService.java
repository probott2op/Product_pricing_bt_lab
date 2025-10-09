package com.lab.product.service;

import com.lab.product.DTO.ProductRuleDTO;
import com.lab.product.DTO.ProductRuleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRuleService {
    ProductRuleDTO addRuleToProduct(String productCode, ProductRuleRequestDTO ruleDto);
    Page<ProductRuleDTO> getRulesForProduct(String productCode, Pageable pageable);
    ProductRuleDTO getRuleByCode(String productCode, String ruleCode);
    ProductRuleDTO updateRule(String productCode, String ruleCode, ProductRuleRequestDTO ruleDto);
    void deleteRule(String productCode, String ruleCode);
}