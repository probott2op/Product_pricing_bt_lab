package com.lab.product.service;

import com.lab.product.DTO.ProductBalanceDTO;
import com.lab.product.DTO.ProductBalanceRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductBalanceService {
    ProductBalanceDTO addBalanceToProduct(String productCode, ProductBalanceRequestDTO balanceDto);
    Page<ProductBalanceDTO> getBalancesForProduct(String productCode, Pageable pageable);
    ProductBalanceDTO getBalanceByCode(String productCode, String balanceCode);
    ProductBalanceDTO updateBalance(String productCode, String balanceCode, ProductBalanceRequestDTO balanceDto);
    void deleteBalance(String productCode, String balanceCode);
}