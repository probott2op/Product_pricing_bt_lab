package com.lab.product.service.impl;

import com.lab.product.DTO.ProductInterestDTO;
import com.lab.product.DTO.ProductInterestRequestDTO;
import com.lab.product.entity.PRODUCT_INTEREST;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.Exception.ResourceNotFoundException;
import com.lab.product.DAO.ProductInterestRepository;
import com.lab.product.DAO.ProductDetailsRepository;
import com.lab.product.service.ProductInterestService;
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
public class ProductInterestServiceImpl implements ProductInterestService {
    
    private final ProductInterestRepository interestRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductInterestDTO addInterestToProduct(String productCode, ProductInterestRequestDTO interestDto) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_INTEREST interest = new PRODUCT_INTEREST();
        interest.setProduct(product);
        // INSERT-ONLY Pattern: Set productCode for cross-version linking
        interest.setProductCode(productCode);
        interest.setRateCode(interestDto.getRateCode());
        interest.setTermInMonths(interestDto.getTermInMonths());
        interest.setRateCumulative(interestDto.getRateCumulative());
        interest.setRateNonCumulativeMonthly(interestDto.getRateNonCumulativeMonthly());
        interest.setRateNonCumulativeQuarterly(interestDto.getRateNonCumulativeQuarterly());
        interest.setRateNonCumulativeYearly(interestDto.getRateNonCumulativeYearly());
        
        // INSERT-ONLY Pattern: Fill audit fields for CREATE operation
        mapper.fillAuditFieldsForCreate(interest);
        
        PRODUCT_INTEREST saved = interestRepository.save(interest);
        return mapper.toInterestDto(saved);
    }

    @Override
    public Page<ProductInterestDTO> getInterestRatesForProduct(String productCode, Pageable pageable) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        return interestRepository.findByProduct(product, pageable)
                .map(mapper::toInterestDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductInterestDTO getInterestRateByCode(String productCode, String rateCode) {
        // INSERT-ONLY Pattern: Use productCode-based query to get latest version
        PRODUCT_INTEREST interest = interestRepository.findByProductCodeAndRateCode(productCode, rateCode)
            .orElseThrow(() -> new ResourceNotFoundException("Interest rate not found: " + rateCode));
            
        return mapper.toInterestDto(interest);
    }

    @Override
    @Transactional
    public ProductInterestDTO updateInterestRate(String productCode, String rateCode, ProductInterestRequestDTO interestDto) {
        // INSERT-ONLY Pattern: Find existing interest rate by productCode and rateCode
        PRODUCT_INTEREST existing = interestRepository.findByProductCodeAndRateCode(productCode, rateCode)
            .orElseThrow(() -> new ResourceNotFoundException("Interest rate not found: " + rateCode));
        
        // INSERT-ONLY Pattern: Create NEW object instead of modifying existing
        PRODUCT_INTEREST newVersion = new PRODUCT_INTEREST();
        // Copy all fields from existing (excluding rateId and versionTimestamp)
        BeanUtils.copyProperties(existing, newVersion, "rateId");
        
        // Apply updates from DTO
        newVersion.setRateCode(interestDto.getRateCode());
        newVersion.setTermInMonths(interestDto.getTermInMonths());
        newVersion.setRateCumulative(interestDto.getRateCumulative());
        newVersion.setRateNonCumulativeMonthly(interestDto.getRateNonCumulativeMonthly());
        newVersion.setRateNonCumulativeQuarterly(interestDto.getRateNonCumulativeQuarterly());
        newVersion.setRateNonCumulativeYearly(interestDto.getRateNonCumulativeYearly());
        
        // INSERT-ONLY Pattern: Fill audit fields for UPDATE operation
        mapper.fillAuditFieldsForUpdate(newVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with same productCode
        PRODUCT_INTEREST updated = interestRepository.save(newVersion);
        return mapper.toInterestDto(updated);
    }

    @Override
    @Transactional
    public void deleteInterestRate(String productCode, String rateCode) {
        // INSERT-ONLY Pattern: Find existing interest rate by productCode and rateCode
        PRODUCT_INTEREST existing = interestRepository.findByProductCodeAndRateCode(productCode, rateCode)
            .orElseThrow(() -> new ResourceNotFoundException("Interest rate not found: " + rateCode));
        
        // INSERT-ONLY Pattern: Create NEW object for delete marker (soft delete)
        PRODUCT_INTEREST deleteVersion = new PRODUCT_INTEREST();
        // Copy all fields from existing (excluding rateId and versionTimestamp)
        BeanUtils.copyProperties(existing, deleteVersion, "rateId");
        
        // INSERT-ONLY Pattern: Fill audit fields for DELETE operation
        mapper.fillAuditFieldsForDelete(deleteVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with crud_value='D' (soft delete marker)
        interestRepository.save(deleteVersion);
    }

    @Override
    public List<ProductInterestDTO> getInterestRatesAuditTrail(String productCode) {
        List<PRODUCT_INTEREST> allVersions = interestRepository.findAllVersionsByProductCode(productCode);
        if (allVersions.isEmpty()) {
            throw new ResourceNotFoundException("No interest rates found for product: " + productCode);
        }
        return allVersions.stream()
                .map(mapper::toInterestDtoWithAudit)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductInterestDTO> getInterestRateAuditTrail(String productCode, String rateCode) {
        List<PRODUCT_INTEREST> allVersions = interestRepository.findAllVersionsByProductCodeAndRateCode(productCode, rateCode);
        if (allVersions.isEmpty()) {
            throw new ResourceNotFoundException("Interest rate not found: " + rateCode);
        }
        return allVersions.stream()
                .map(mapper::toInterestDtoWithAudit)
                .collect(Collectors.toList());
    }
}
