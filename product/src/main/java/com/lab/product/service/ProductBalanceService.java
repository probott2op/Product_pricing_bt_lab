package com.lab.product.service;

import com.lab.product.DTO.ProductBalanceDTO;
import com.lab.product.DTO.ProductBalanceRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductBalanceService {
    ProductBalanceDTO addBalanceToProduct(UUID productId, ProductBalanceRequestDTO balanceDto);
    Page<ProductBalanceDTO> getBalancesForProduct(UUID productId, Pageable pageable);
    ProductBalanceDTO getBalanceById(UUID productId, UUID balanceId);
    ProductBalanceDTO updateBalance(UUID productId, UUID balanceId, ProductBalanceRequestDTO balanceDto);
    void deleteBalance(UUID productId, UUID balanceId);
}