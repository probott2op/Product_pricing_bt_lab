package com.lab.product.service;

import com.lab.product.DTO.ProductRuleDTO;
import com.lab.product.DTO.ProductRuleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductRuleService {
    ProductRuleDTO addRuleToProduct(String productCode, ProductRuleRequestDTO ruleDto);
    Page<ProductRuleDTO> getRulesForProduct(String productCode, Pageable pageable);
    ProductRuleDTO getRuleById(String productCode, UUID ruleId);
    ProductRuleDTO updateRule(String productCode, UUID ruleId, ProductRuleRequestDTO ruleDto);
    void deleteRule(String productCode, UUID ruleId);
}