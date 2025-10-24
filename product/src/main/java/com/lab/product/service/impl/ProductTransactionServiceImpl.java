package com.lab.product.service.impl;

import com.lab.product.DTO.ProductTransactionDTO;
import com.lab.product.DTO.ProductTransactionRequestDTO;
import com.lab.product.entity.PRODUCT_TRANSACTION;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.entity.ENUMS.PRODUCT_TRANSACTION_TYPE;
import com.lab.product.Exception.ResourceNotFoundException;
import com.lab.product.DAO.ProductTransactionRepository;
import com.lab.product.DAO.ProductDetailsRepository;
import com.lab.product.service.ProductTransactionService;
import com.lab.product.service.helper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductTransactionServiceImpl implements ProductTransactionService {
    
    private final ProductTransactionRepository transactionRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductTransactionDTO addTransactionToProduct(String productCode, ProductTransactionRequestDTO transactionDto) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_TRANSACTION transaction = new PRODUCT_TRANSACTION();
        transaction.setProduct(product);
        // INSERT-ONLY Pattern: Set productCode for cross-version linking
        transaction.setProductCode(productCode);
        transaction.setTransactionCode(transactionDto.getTransactionCode());
        transaction.setTransactionType(PRODUCT_TRANSACTION_TYPE.valueOf(transactionDto.getTransactionType()));
        transaction.setAllowed(transactionDto.isActive());
        
        // INSERT-ONLY Pattern: Fill audit fields for CREATE operation
        mapper.fillAuditFieldsForCreate(transaction);
        
        PRODUCT_TRANSACTION saved = transactionRepository.save(transaction);
        return mapper.toTransactionDto(saved);
    }

    @Override
    public Page<ProductTransactionDTO> getTransactionsForProduct(String productCode, Pageable pageable) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        return transactionRepository.findByProduct(product, pageable)
                .map(mapper::toTransactionDto);
    }

    @Override
    public ProductTransactionDTO getTransactionByCode(String productCode, String transactionCode) {
        // INSERT-ONLY Pattern: Use productCode-based query to get latest version
        PRODUCT_TRANSACTION transaction = transactionRepository.findByProductCodeAndTransactionCode(productCode, transactionCode)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionCode));
            
        return mapper.toTransactionDto(transaction);
    }

    @Override
    @Transactional
    public ProductTransactionDTO updateTransaction(String productCode, String transactionCode, ProductTransactionRequestDTO transactionDto) {
        // INSERT-ONLY Pattern: Find existing transaction by productCode and transactionCode
        PRODUCT_TRANSACTION existing = transactionRepository.findByProductCodeAndTransactionCode(productCode, transactionCode)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionCode));

        // INSERT-ONLY Pattern: Create NEW object instead of modifying existing
        PRODUCT_TRANSACTION newVersion = new PRODUCT_TRANSACTION();
        // Copy all fields from existing (excluding transactionId and versionTimestamp)
        BeanUtils.copyProperties(existing, newVersion, "transactionId");

        // Apply updates from DTO
        newVersion.setTransactionCode(transactionDto.getTransactionCode());
        newVersion.setTransactionType(PRODUCT_TRANSACTION_TYPE.valueOf(transactionDto.getTransactionType()));
        newVersion.setAllowed(transactionDto.isActive());
        
        // INSERT-ONLY Pattern: Fill audit fields for UPDATE operation
        mapper.fillAuditFieldsForUpdate(newVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with same productCode
        PRODUCT_TRANSACTION updated = transactionRepository.save(newVersion);
        return mapper.toTransactionDto(updated);
    }

    @Override
    @Transactional
    public void deleteTransaction(String productCode, String transactionCode) {
        // INSERT-ONLY Pattern: Find existing transaction by productCode and transactionCode
        PRODUCT_TRANSACTION existing = transactionRepository.findByProductCodeAndTransactionCode(productCode, transactionCode)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionCode));

        // INSERT-ONLY Pattern: Create NEW object for delete marker (soft delete)
        PRODUCT_TRANSACTION deleteVersion = new PRODUCT_TRANSACTION();
        // Copy all fields from existing (excluding transactionId and versionTimestamp)
        BeanUtils.copyProperties(existing, deleteVersion, "transactionId");
        
        // INSERT-ONLY Pattern: Fill audit fields for DELETE operation
        mapper.fillAuditFieldsForDelete(deleteVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with crud_value='D' (soft delete marker)
        transactionRepository.save(deleteVersion);
    }
}