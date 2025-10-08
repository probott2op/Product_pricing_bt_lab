package com.lab.product.service;

import com.lab.product.DTO.ProductInterestDTO;
import com.lab.product.DTO.ProductInterestRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductInterestService {
    /**
     * Add a new interest rate to a product
     */
    ProductInterestDTO addInterestToProduct(String productCode, ProductInterestRequestDTO interestDto);
    
    /**
     * Get all interest rates for a product with pagination
     */
    Page<ProductInterestDTO> getInterestRatesForProduct(String productCode, Pageable pageable);
    
    /**
     * Get a specific interest rate by ID
     */
    ProductInterestDTO getInterestRateById(String productCode, UUID rateId);
    
    /**
     * Update an interest rate
     */
    ProductInterestDTO updateInterestRate(String productCode, UUID rateId, ProductInterestRequestDTO interestDto);
    
    /**
     * Delete an interest rate
     */
    void deleteInterestRate(String productCode, UUID rateId);
}
