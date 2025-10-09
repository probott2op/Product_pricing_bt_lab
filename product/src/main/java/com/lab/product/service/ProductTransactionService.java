package com.lab.product.service;

import com.lab.product.DTO.ProductTransactionDTO;
import com.lab.product.DTO.ProductTransactionRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductTransactionService {
    ProductTransactionDTO addTransactionToProduct(String productCode, ProductTransactionRequestDTO transactionDto);
    Page<ProductTransactionDTO> getTransactionsForProduct(String productCode, Pageable pageable);
    ProductTransactionDTO getTransactionByCode(String productCode, String transactionCode);
    ProductTransactionDTO updateTransaction(String productCode, String transactionCode, ProductTransactionRequestDTO transactionDto);
    void deleteTransaction(String productCode, String transactionCode);
}