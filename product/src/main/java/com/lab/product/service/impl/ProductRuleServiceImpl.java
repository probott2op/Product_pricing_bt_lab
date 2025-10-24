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
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductRuleServiceImpl implements ProductRuleService {
    
    private final ProductRuleRepository ruleRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductRuleDTO addRuleToProduct(String productCode, ProductRuleRequestDTO ruleDto) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_RULES rule = new PRODUCT_RULES();
        rule.setProduct(product);
        // INSERT-ONLY Pattern: Set productCode for cross-version linking
        rule.setProductCode(productCode);
        rule.setRuleName(ruleDto.getRuleName());
        rule.setRuleCode(ruleDto.getRuleCode());
        rule.setRuleType(ruleDto.getRuleType());
        rule.setDataType(ruleDto.getDataType());
        rule.setRuleValue(ruleDto.getRuleValue());
        rule.setValidationType(ruleDto.getValidationType());
        
        // INSERT-ONLY Pattern: Fill audit fields for CREATE operation
        mapper.fillAuditFieldsForCreate(rule);
        
        PRODUCT_RULES saved = ruleRepository.save(rule);
        return mapper.toRuleDto(saved);
    }

    @Override
    public Page<ProductRuleDTO> getRulesForProduct(String productCode, Pageable pageable) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        return ruleRepository.findByProduct(product, pageable)
                .map(mapper::toRuleDto);
    }

    @Override
    public ProductRuleDTO getRuleByCode(String productCode, String ruleCode) {
        // INSERT-ONLY Pattern: Use productCode-based query to get latest version
        PRODUCT_RULES rule = ruleRepository.findByProductCodeAndRuleCode(productCode, ruleCode)
            .orElseThrow(() -> new ResourceNotFoundException("Rule not found: " + ruleCode));
            
        return mapper.toRuleDto(rule);
    }

    @Override
    @Transactional
    public ProductRuleDTO updateRule(String productCode, String ruleCode, ProductRuleRequestDTO ruleDto) {
        // INSERT-ONLY Pattern: Find existing rule by productCode and ruleCode
        PRODUCT_RULES existing = ruleRepository.findByProductCodeAndRuleCode(productCode, ruleCode)
            .orElseThrow(() -> new ResourceNotFoundException("Rule not found: " + ruleCode));

        // INSERT-ONLY Pattern: Create NEW object instead of modifying existing
        PRODUCT_RULES newVersion = new PRODUCT_RULES();
        // Copy all fields from existing (excluding ruleId and versionTimestamp)
        BeanUtils.copyProperties(existing, newVersion, "ruleId");

        // Apply updates from DTO
        newVersion.setRuleType(ruleDto.getRuleType());
        newVersion.setDataType(ruleDto.getDataType());
        newVersion.setRuleValue(ruleDto.getRuleValue());
        newVersion.setValidationType(ruleDto.getValidationType());
        newVersion.setRuleCode(ruleDto.getRuleCode());
        
        // INSERT-ONLY Pattern: Fill audit fields for UPDATE operation
        mapper.fillAuditFieldsForUpdate(newVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with same productCode
        PRODUCT_RULES updated = ruleRepository.save(newVersion);
        return mapper.toRuleDto(updated);
    }

    @Override
    @Transactional
    public void deleteRule(String productCode, String ruleCode) {
        // INSERT-ONLY Pattern: Find existing rule by productCode and ruleCode
        PRODUCT_RULES existing = ruleRepository.findByProductCodeAndRuleCode(productCode, ruleCode)
            .orElseThrow(() -> new ResourceNotFoundException("Rule not found: " + ruleCode));

        // INSERT-ONLY Pattern: Create NEW object for delete marker (soft delete)
        PRODUCT_RULES deleteVersion = new PRODUCT_RULES();
        // Copy all fields from existing (excluding ruleId and versionTimestamp)
        BeanUtils.copyProperties(existing, deleteVersion, "ruleId");
        
        // INSERT-ONLY Pattern: Fill audit fields for DELETE operation
        mapper.fillAuditFieldsForDelete(deleteVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with crud_value='D' (soft delete marker)
        ruleRepository.save(deleteVersion);
    }
}