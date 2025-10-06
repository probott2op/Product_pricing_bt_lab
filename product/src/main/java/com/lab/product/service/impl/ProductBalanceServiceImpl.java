package com.lab.product.service.impl;

import com.lab.product.DTO.ProductBalanceDTO;
import com.lab.product.DTO.ProductBalanceRequestDTO;
import com.lab.product.entity.PRODUCT_BALANCE;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.Exception.ResourceNotFoundException;
import com.lab.product.DAO.ProductBalanceRepository;
import com.lab.product.DAO.ProductDetailsRepository;
import com.lab.product.service.ProductBalanceService;
import com.lab.product.service.helper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductBalanceServiceImpl implements ProductBalanceService {
    
    private final ProductBalanceRepository balanceRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductBalanceDTO addBalanceToProduct(String productCode, ProductBalanceRequestDTO balanceDto) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_BALANCE balance = new PRODUCT_BALANCE();
        balance.setProduct(product);
        balance.setBalanceType(balanceDto.getBalanceType());
        balance.setBalanceCode(balanceDto.getBalanceCode());
        
        // Fill audit fields
        mapper.fillAuditFields(balance);
        
        PRODUCT_BALANCE saved = balanceRepository.save(balance);
        return mapper.toBalanceDto(saved);
    }

    @Override
    public Page<ProductBalanceDTO> getBalancesForProduct(String productCode, Pageable pageable) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        return balanceRepository.findByProduct(product, pageable)
                .map(mapper::toBalanceDto);
    }

    @Override
    public ProductBalanceDTO getBalanceById(String productCode, UUID balanceId) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
        
        PRODUCT_BALANCE balance = balanceRepository.findByProductAndBalanceId(product, balanceId)
            .orElseThrow(() -> new ResourceNotFoundException("Balance not found: " + balanceId));
            
        return mapper.toBalanceDto(balance);
    }

    @Override
    @Transactional
    public ProductBalanceDTO updateBalance(String productCode, UUID balanceId, ProductBalanceRequestDTO balanceDto) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_BALANCE balance = balanceRepository.findByProductAndBalanceId(product, balanceId)
            .orElseThrow(() -> new ResourceNotFoundException("Balance not found: " + balanceId));

        balance.setBalanceType(balanceDto.getBalanceType());
        balance.setBalanceCode(balanceDto.getBalanceCode());
        
        // Update audit fields
        mapper.fillAuditFields(balance);
        
        PRODUCT_BALANCE updated = balanceRepository.save(balance);
        return mapper.toBalanceDto(updated);
    }

    @Override
    @Transactional
    public void deleteBalance(String productCode, UUID balanceId) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_BALANCE balance = balanceRepository.findByProductAndBalanceId(product, balanceId)
            .orElseThrow(() -> new ResourceNotFoundException("Balance not found: " + balanceId));

        balanceRepository.delete(balance);
    }
}