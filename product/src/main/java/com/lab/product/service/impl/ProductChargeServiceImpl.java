package com.lab.product.service.impl;

import com.lab.product.DTO.ProductChargeDTO;
import com.lab.product.DTO.ProductChargeRequestDTO;
import com.lab.product.entity.PRODUCT_CHARGES;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.Exception.ResourceNotFoundException;
import com.lab.product.DAO.ProductChargeRepository;
import com.lab.product.DAO.ProductDetailsRepository;
import com.lab.product.service.ProductChargeService;
import com.lab.product.service.helper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductChargeServiceImpl implements ProductChargeService {
    
    private final ProductChargeRepository chargeRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductChargeDTO addChargeToProduct(String productCode, ProductChargeRequestDTO chargeDto) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_CHARGES charge = new PRODUCT_CHARGES();
        charge.setProduct(product);
        // INSERT-ONLY Pattern: Set productCode for cross-version linking
        charge.setProductCode(productCode);
        charge.setChargeType(chargeDto.getChargeType());
        charge.setChargeName(chargeDto.getChargeName());
        charge.setChargeCode(chargeDto.getChargeCode());
        charge.setChargeValue(chargeDto.getChargeValue());
        charge.setCalculationType(chargeDto.getCalculationType());
        charge.setFrequency(chargeDto.getFrequency());
        charge.setDebitCredit(chargeDto.getDebitCredit());
        
        // INSERT-ONLY Pattern: Fill audit fields for CREATE operation
        mapper.fillAuditFieldsForCreate(charge);
        
        PRODUCT_CHARGES saved = chargeRepository.save(charge);
        return mapper.toChargeDto(saved);
    }

    @Override
    public Page<ProductChargeDTO> getChargesForProduct(String productCode, Pageable pageable) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        return chargeRepository.findByProduct(product, pageable)
                .map(mapper::toChargeDto);
    }

    @Override
    public ProductChargeDTO getChargeByCode(String productCode, String chargeCode) {
        // INSERT-ONLY Pattern: Use productCode-based query to get latest version
        PRODUCT_CHARGES charge = chargeRepository.findByProductCodeAndChargeCode(productCode, chargeCode)
            .orElseThrow(() -> new ResourceNotFoundException("Charge not found: " + chargeCode));
            
        return mapper.toChargeDto(charge);
    }

    @Override
    @Transactional
    public ProductChargeDTO updateCharge(String productCode, String chargeCode, ProductChargeRequestDTO chargeDto) {
        // INSERT-ONLY Pattern: Find existing charge by productCode and chargeCode
        PRODUCT_CHARGES existing = chargeRepository.findByProductCodeAndChargeCode(productCode, chargeCode)
            .orElseThrow(() -> new ResourceNotFoundException("Charge not found: " + chargeCode));

        // INSERT-ONLY Pattern: Create NEW object instead of modifying existing
        PRODUCT_CHARGES newVersion = new PRODUCT_CHARGES();
        // Copy all fields from existing (excluding chargeId and versionTimestamp)
        BeanUtils.copyProperties(existing, newVersion, "chargeId");

        // Apply updates from DTO
        newVersion.setChargeType(chargeDto.getChargeType());
        newVersion.setChargeCode(chargeDto.getChargeCode());
        newVersion.setChargeValue(chargeDto.getChargeValue());
        newVersion.setCalculationType(chargeDto.getCalculationType());
        newVersion.setFrequency(chargeDto.getFrequency());
        newVersion.setDebitCredit(chargeDto.getDebitCredit());
        
        // INSERT-ONLY Pattern: Fill audit fields for UPDATE operation
        mapper.fillAuditFieldsForUpdate(newVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with same productCode
        PRODUCT_CHARGES updated = chargeRepository.save(newVersion);
        return mapper.toChargeDto(updated);
    }

    @Override
    @Transactional
    public void deleteCharge(String productCode, String chargeCode) {
        // INSERT-ONLY Pattern: Find existing charge by productCode and chargeCode
        PRODUCT_CHARGES existing = chargeRepository.findByProductCodeAndChargeCode(productCode, chargeCode)
            .orElseThrow(() -> new ResourceNotFoundException("Charge not found: " + chargeCode));
        
        // INSERT-ONLY Pattern: Create NEW object for delete marker (soft delete)
        PRODUCT_CHARGES deleteVersion = new PRODUCT_CHARGES();
        // Copy all fields from existing (excluding chargeId and versionTimestamp)
        BeanUtils.copyProperties(existing, deleteVersion, "chargeId");
        
        // INSERT-ONLY Pattern: Fill audit fields for DELETE operation
        mapper.fillAuditFieldsForDelete(deleteVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with crud_value='D' (soft delete marker)
        chargeRepository.save(deleteVersion);
    }

    @Override
    public List<ProductChargeDTO> getChargesAuditTrail(String productCode) {
        List<PRODUCT_CHARGES> allVersions = chargeRepository.findAllVersionsByProductCode(productCode);
        if (allVersions.isEmpty()) {
            throw new ResourceNotFoundException("No charges found for product: " + productCode);
        }
        return allVersions.stream()
                .map(mapper::toChargeDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductChargeDTO> getChargeAuditTrail(String productCode, String chargeCode) {
        List<PRODUCT_CHARGES> allVersions = chargeRepository.findAllVersionsByProductCodeAndChargeCode(productCode, chargeCode);
        if (allVersions.isEmpty()) {
            throw new ResourceNotFoundException("Charge not found: " + chargeCode);
        }
        return allVersions.stream()
                .map(mapper::toChargeDto)
                .collect(Collectors.toList());
    }
}