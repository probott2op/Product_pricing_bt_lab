package com.lab.product.service;

import com.lab.product.DTO.ProductInterestDTO;
import com.lab.product.DTO.ProductInterestRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get a specific interest rate by code
     */
    ProductInterestDTO getInterestRateByCode(String productCode, String rateCode);
    
    /**
     * Update an interest rate
     */
    ProductInterestDTO updateInterestRate(String productCode, String rateCode, ProductInterestRequestDTO interestDto);
    
    /**
     * Delete an interest rate
     */
    void deleteInterestRate(String productCode, String rateCode);
}
