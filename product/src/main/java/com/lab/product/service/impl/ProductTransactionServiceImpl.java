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
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_TRANSACTION transaction = new PRODUCT_TRANSACTION();
        transaction.setProduct(product);
        transaction.setTransactionCode(transactionDto.getTransactionCode());
        transaction.setTransactionType(PRODUCT_TRANSACTION_TYPE.valueOf(transactionDto.getTransactionType()));
        transaction.setAllowed(transactionDto.isActive());
        
        // Fill audit fields
        mapper.fillAuditFields(transaction);
        
        PRODUCT_TRANSACTION saved = transactionRepository.save(transaction);
        return mapper.toTransactionDto(saved);
    }

    @Override
    public Page<ProductTransactionDTO> getTransactionsForProduct(String productCode, Pageable pageable) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        return transactionRepository.findByProduct(product, pageable)
                .map(mapper::toTransactionDto);
    }

    @Override
    public ProductTransactionDTO getTransactionByCode(String productCode, String transactionCode) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
        
        PRODUCT_TRANSACTION transaction = transactionRepository.findByProductAndTransactionCode(product, transactionCode)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionCode));
            
        return mapper.toTransactionDto(transaction);
    }

    @Override
    @Transactional
    public ProductTransactionDTO updateTransaction(String productCode, String transactionCode, ProductTransactionRequestDTO transactionDto) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_TRANSACTION transaction = transactionRepository.findByProductAndTransactionCode(product, transactionCode)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionCode));

        transaction.setTransactionCode(transactionDto.getTransactionCode());
        transaction.setTransactionType(PRODUCT_TRANSACTION_TYPE.valueOf(transactionDto.getTransactionType()));
        transaction.setAllowed(transactionDto.isActive());
        
        // Update audit fields
        mapper.fillAuditFields(transaction);
        
        PRODUCT_TRANSACTION updated = transactionRepository.save(transaction);
        return mapper.toTransactionDto(updated);
    }

    @Override
    @Transactional
    public void deleteTransaction(String productCode, String transactionCode) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_TRANSACTION transaction = transactionRepository.findByProductAndTransactionCode(product, transactionCode)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionCode));

        transactionRepository.delete(transaction);
    }
}