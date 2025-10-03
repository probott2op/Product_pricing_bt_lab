package com.lab.product.service;

import com.lab.product.DTO.ProductTransactionDTO;
import com.lab.product.DTO.ProductTransactionRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface ProductTransactionService {
    ProductTransactionDTO addTransactionToProduct(UUID productId, ProductTransactionRequestDTO transactionDto);
    Page<ProductTransactionDTO> getTransactionsForProduct(UUID productId, Pageable pageable);
    ProductTransactionDTO getTransactionById(UUID productId, UUID transactionId);
    ProductTransactionDTO updateTransaction(UUID productId, UUID transactionId, ProductTransactionRequestDTO transactionDto);
    void deleteTransaction(UUID productId, UUID transactionId);
}