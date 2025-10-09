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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductChargeServiceImpl implements ProductChargeService {
    
    private final ProductChargeRepository chargeRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductChargeDTO addChargeToProduct(String productCode, ProductChargeRequestDTO chargeDto) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_CHARGES charge = new PRODUCT_CHARGES();
        charge.setProduct(product);
        charge.setChargeType(chargeDto.getChargeType());
        charge.setChargeCode(chargeDto.getChargeCode());
        charge.setChargeValue(chargeDto.getChargeValue());
        charge.setCalculationType(chargeDto.getCalculationType());
        charge.setFrequency(chargeDto.getFrequency());
        charge.setDebitCredit(chargeDto.getDebitCredit());
        
        // Fill audit fields
        mapper.fillAuditFields(charge);
        
        PRODUCT_CHARGES saved = chargeRepository.save(charge);
        return mapper.toChargeDto(saved);
    }

    @Override
    public Page<ProductChargeDTO> getChargesForProduct(String productCode, Pageable pageable) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        return chargeRepository.findByProduct(product, pageable)
                .map(mapper::toChargeDto);
    }

    @Override
    public ProductChargeDTO getChargeByCode(String productCode, String chargeCode) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
        
        PRODUCT_CHARGES charge = chargeRepository.findByProductAndChargeCode(product, chargeCode)
            .orElseThrow(() -> new ResourceNotFoundException("Charge not found: " + chargeCode));
            
        return mapper.toChargeDto(charge);
    }

    @Override
    @Transactional
    public ProductChargeDTO updateCharge(String productCode, String chargeCode, ProductChargeRequestDTO chargeDto) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_CHARGES charge = chargeRepository.findByProductAndChargeCode(product, chargeCode)
            .orElseThrow(() -> new ResourceNotFoundException("Charge not found: " + chargeCode));

        charge.setChargeType(chargeDto.getChargeType());
        charge.setChargeCode(chargeDto.getChargeCode());
        charge.setChargeValue(chargeDto.getChargeValue());
        charge.setCalculationType(chargeDto.getCalculationType());
        charge.setFrequency(chargeDto.getFrequency());
        charge.setDebitCredit(chargeDto.getDebitCredit());
        
        // Update audit fields
        mapper.fillAuditFields(charge);
        
        PRODUCT_CHARGES updated = chargeRepository.save(charge);
        return mapper.toChargeDto(updated);
    }

    @Override
    @Transactional
    public void deleteCharge(String productCode, String chargeCode) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        PRODUCT_CHARGES charge = chargeRepository.findByProductAndChargeCode(product, chargeCode)
            .orElseThrow(() -> new ResourceNotFoundException("Charge not found: " + chargeCode));
            
        chargeRepository.delete(charge);
    }
}