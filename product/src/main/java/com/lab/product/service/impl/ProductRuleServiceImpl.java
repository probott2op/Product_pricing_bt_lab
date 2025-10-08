package com.lab.product.service.impl;

import com.lab.product.DTO.ProductRuleDTO;
import com.lab.product.DTO.ProductRuleRequestDTO;
import com.lab.product.entity.PRODUCT_RULES;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.Exception.ResourceNotFoundException;
import com.lab.product.DAO.ProductRuleRepository;
import com.lab.product.DAO.ProductDetailsRepository;
import com.lab.product.service.ProductRuleService;
import com.lab.product.service.helper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductRuleServiceImpl implements ProductRuleService {
    
    private final ProductRuleRepository ruleRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductRuleDTO addRuleToProduct(String productId, ProductRuleRequestDTO ruleDto) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        PRODUCT_RULES rule = new PRODUCT_RULES();
        rule.setProduct(product);
        rule.setRuleName(ruleDto.getRuleName());
        rule.setRuleCode(ruleDto.getRuleCode());
        rule.setRuleType(ruleDto.getRuleType());
        rule.setDataType(ruleDto.getDataType());
        rule.setRuleValue(ruleDto.getRuleValue());
        rule.setValidationType(ruleDto.getValidationType());
        
        // Fill audit fields
        mapper.fillAuditFields(rule);
        
        PRODUCT_RULES saved = ruleRepository.save(rule);
        return mapper.toRuleDto(saved);
    }

    @Override
    public Page<ProductRuleDTO> getRulesForProduct(String productCode, Pageable pageable) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        return ruleRepository.findByProduct(product, pageable)
                .map(mapper::toRuleDto);
    }

    @Override
    public ProductRuleDTO getRuleById(String productCode, UUID ruleId) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
        
        PRODUCT_RULES rule = ruleRepository.findByProductAndRuleId(product, ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("Rule not found: " + ruleId));
            
        return mapper.toRuleDto(rule);
    }

    @Override
    @Transactional
    public ProductRuleDTO updateRule(String productCode, UUID ruleId, ProductRuleRequestDTO ruleDto) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_RULES rule = ruleRepository.findByProductAndRuleId(product, ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("Rule not found: " + ruleId));

        rule.setRuleType(ruleDto.getRuleType());
        rule.setDataType(ruleDto.getDataType());
        rule.setRuleValue(ruleDto.getRuleValue());
        rule.setValidationType(ruleDto.getValidationType());
        
        // Update audit fields
        mapper.fillAuditFields(rule);
        
        PRODUCT_RULES updated = ruleRepository.save(rule);
        return mapper.toRuleDto(updated);
    }

    @Override
    @Transactional
    public void deleteRule(String productCode, UUID ruleId) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_RULES rule = ruleRepository.findByProductAndRuleId(product, ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("Rule not found: " + ruleId));

        ruleRepository.delete(rule);
    }
}