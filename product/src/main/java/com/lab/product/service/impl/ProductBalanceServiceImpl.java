package com.lab.product.service.impl;

import com.lab.product.DTO.ProductBalanceDTO;
import com.lab.product.DTO.ProductBalanceRequestDTO;
import com.lab.product.entity.PRODUCT_BALANCE;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.entity.ENUMS.PRODUCT_BALANCE_TYPE;
import com.lab.product.Exception.ResourceNotFoundException;
import com.lab.product.DAO.ProductBalanceRepository;
import com.lab.product.DAO.ProductDetailsRepository;
import com.lab.product.service.ProductBalanceService;
import com.lab.product.service.helper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductBalanceServiceImpl implements ProductBalanceService {
    
    private final ProductBalanceRepository balanceRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductBalanceDTO addBalanceToProduct(String productCode, ProductBalanceRequestDTO balanceDto) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        // Check if balance type already exists for this product
        if (balanceRepository.existsByProductAndBalanceType(product, balanceDto.getBalanceType())) {
            throw new IllegalArgumentException("Balance type '" + balanceDto.getBalanceType() + 
                "' already exists for product: " + productCode);
        }

        PRODUCT_BALANCE balance = new PRODUCT_BALANCE();
        balance.setProduct(product);
        // INSERT-ONLY Pattern: Set productCode for cross-version linking
        balance.setProductCode(productCode);
        balance.setBalanceType(balanceDto.getBalanceType());
        balance.setIsActive(balanceDto.getIsActive() != null ? balanceDto.getIsActive() : true);
        
        // INSERT-ONLY Pattern: Fill audit fields for CREATE operation
        mapper.fillAuditFieldsForCreate(balance);
        
        PRODUCT_BALANCE saved = balanceRepository.save(balance);
        return mapper.toBalanceDto(saved);
    }

    @Override
    public Page<ProductBalanceDTO> getBalancesForProduct(String productCode, Pageable pageable) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        return balanceRepository.findByProduct(product, pageable)
                .map(mapper::toBalanceDto);
    }

    @Override
    public ProductBalanceDTO getBalanceByType(String productCode, String balanceType) {
        // INSERT-ONLY Pattern: Use productCode-based query to get latest version
        PRODUCT_BALANCE balance = balanceRepository.findByProductCodeAndBalanceType(productCode, 
            PRODUCT_BALANCE_TYPE.valueOf(balanceType))
            .orElseThrow(() -> new ResourceNotFoundException("Balance type not found: " + balanceType));
            
        return mapper.toBalanceDto(balance);
    }

    @Override
    @Transactional
    public ProductBalanceDTO updateBalance(String productCode, String balanceType, ProductBalanceRequestDTO balanceDto) {
        // INSERT-ONLY Pattern: Find existing balance by productCode and balanceType
        PRODUCT_BALANCE existing = balanceRepository.findByProductCodeAndBalanceType(productCode, 
            PRODUCT_BALANCE_TYPE.valueOf(balanceType))
            .orElseThrow(() -> new ResourceNotFoundException("Balance type not found: " + balanceType));

        // INSERT-ONLY Pattern: Create NEW object instead of modifying existing
        PRODUCT_BALANCE newVersion = new PRODUCT_BALANCE();
        // Copy all fields from existing (excluding balanceId and versionTimestamp)
        BeanUtils.copyProperties(existing, newVersion, "balanceId");

        // Apply updates from DTO
        newVersion.setBalanceType(balanceDto.getBalanceType());
        newVersion.setIsActive(balanceDto.getIsActive() != null ? balanceDto.getIsActive() : existing.getIsActive());
        
        // INSERT-ONLY Pattern: Fill audit fields for UPDATE operation
        mapper.fillAuditFieldsForUpdate(newVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with same productCode
        PRODUCT_BALANCE updated = balanceRepository.save(newVersion);
        return mapper.toBalanceDto(updated);
    }

    @Override
    @Transactional
    public void deleteBalance(String productCode, String balanceType) {
        // INSERT-ONLY Pattern: Find existing balance by productCode and balanceType
        PRODUCT_BALANCE existing = balanceRepository.findByProductCodeAndBalanceType(productCode, 
            PRODUCT_BALANCE_TYPE.valueOf(balanceType))
            .orElseThrow(() -> new ResourceNotFoundException("Balance type not found: " + balanceType));

        // INSERT-ONLY Pattern: Create NEW object for delete marker (soft delete)
        PRODUCT_BALANCE deleteVersion = new PRODUCT_BALANCE();
        // Copy all fields from existing (excluding balanceId and versionTimestamp)
        BeanUtils.copyProperties(existing, deleteVersion, "balanceId");
        
        // INSERT-ONLY Pattern: Fill audit fields for DELETE operation
        mapper.fillAuditFieldsForDelete(deleteVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with crud_value='D' (soft delete marker)
        balanceRepository.save(deleteVersion);
    }
}