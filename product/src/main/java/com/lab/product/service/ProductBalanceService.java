package com.lab.product.service;

import com.lab.product.DTO.ProductBalanceDTO;
import com.lab.product.DTO.ProductBalanceRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductBalanceService {
    ProductBalanceDTO addBalanceToProduct(String productCode, ProductBalanceRequestDTO balanceDto);
    Page<ProductBalanceDTO> getBalancesForProduct(String productCode, Pageable pageable);
    ProductBalanceDTO getBalanceById(String productCode, UUID balanceId);
    ProductBalanceDTO updateBalance(String productCode, UUID balanceId, ProductBalanceRequestDTO balanceDto);
    void deleteBalance(String productCode, UUID balanceId);
}