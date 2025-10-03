package com.lab.product.service;

import com.lab.product.DTO.ProductRuleDTO;
import com.lab.product.DTO.ProductRuleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductRuleService {
    ProductRuleDTO addRuleToProduct(UUID productId, ProductRuleRequestDTO ruleDto);
    Page<ProductRuleDTO> getRulesForProduct(UUID productId, Pageable pageable);
    ProductRuleDTO getRuleById(UUID productId, UUID ruleId);
    ProductRuleDTO updateRule(UUID productId, UUID ruleId, ProductRuleRequestDTO ruleDto);
    void deleteRule(UUID productId, UUID ruleId);
}