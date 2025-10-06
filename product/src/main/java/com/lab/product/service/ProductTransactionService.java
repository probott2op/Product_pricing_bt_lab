package com.lab.product.service;

import com.lab.product.DTO.ProductTransactionDTO;
import com.lab.product.DTO.ProductTransactionRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface ProductTransactionService {
    ProductTransactionDTO addTransactionToProduct(String productCode, ProductTransactionRequestDTO transactionDto);
    Page<ProductTransactionDTO> getTransactionsForProduct(String productCode, Pageable pageable);
    ProductTransactionDTO getTransactionById(String productCode, UUID transactionId);
    ProductTransactionDTO updateTransaction(String productCode, UUID transactionId, ProductTransactionRequestDTO transactionDto);
    void deleteTransaction(String productCode, UUID transactionId);
}