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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductInterestServiceImpl implements ProductInterestService {
    
    private final ProductInterestRepository interestRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductInterestDTO addInterestToProduct(String productCode, ProductInterestRequestDTO interestDto) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_INTEREST interest = new PRODUCT_INTEREST();
        interest.setProduct(product);
        interest.setTermInMonths(interestDto.getTermInMonths());
        interest.setRateCumulative(interestDto.getRateCumulative());
        interest.setRateNonCumulativeMonthly(interestDto.getRateNonCumulativeMonthly());
        interest.setRateNonCumulativeQuarterly(interestDto.getRateNonCumulativeQuarterly());
        interest.setRateNonCumulativeYearly(interestDto.getRateNonCumulativeYearly());
        
        // Fill audit fields
        mapper.fillAuditFields(interest);
        
        PRODUCT_INTEREST saved = interestRepository.save(interest);
        return mapper.toInterestDto(saved);
    }

    @Override
    public Page<ProductInterestDTO> getInterestRatesForProduct(String productCode, Pageable pageable) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        return interestRepository.findByProduct(product, pageable)
                .map(mapper::toInterestDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductInterestDTO getInterestRateById(String productCode, UUID rateId) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        PRODUCT_INTEREST interest = interestRepository.findByProductAndRateId(product, rateId)
            .orElseThrow(() -> new ResourceNotFoundException("Interest rate not found: " + rateId));
            
        return mapper.toInterestDto(interest);
    }

    @Override
    @Transactional
    public ProductInterestDTO updateInterestRate(String productCode, UUID rateId, ProductInterestRequestDTO interestDto) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        PRODUCT_INTEREST interest = interestRepository.findByProductAndRateId(product, rateId)
            .orElseThrow(() -> new ResourceNotFoundException("Interest rate not found: " + rateId));
            
        interest.setTermInMonths(interestDto.getTermInMonths());
        interest.setRateCumulative(interestDto.getRateCumulative());
        interest.setRateNonCumulativeMonthly(interestDto.getRateNonCumulativeMonthly());
        interest.setRateNonCumulativeQuarterly(interestDto.getRateNonCumulativeQuarterly());
        interest.setRateNonCumulativeYearly(interestDto.getRateNonCumulativeYearly());
        
        // Update audit fields
        mapper.fillAuditFields(interest);
        
        PRODUCT_INTEREST updated = interestRepository.save(interest);
        return mapper.toInterestDto(updated);
    }

    @Override
    @Transactional
    public void deleteInterestRate(String productCode, UUID rateId) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
        
        PRODUCT_INTEREST interest = interestRepository.findByProductAndRateId(product, rateId)
            .orElseThrow(() -> new ResourceNotFoundException("Interest rate not found: " + rateId));
            
        interestRepository.delete(interest);
    }
}
