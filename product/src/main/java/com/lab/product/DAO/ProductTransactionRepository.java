package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_TRANSACTION;
import com.lab.product.entity.PRODUCT_DETAILS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductTransactionRepository extends JpaRepository<PRODUCT_TRANSACTION, UUID> {
    Page<PRODUCT_TRANSACTION> findByProduct(PRODUCT_DETAILS product, Pageable pageable);
    Optional<PRODUCT_TRANSACTION> findByProductAndId(PRODUCT_DETAILS product, UUID transactionId);
    Optional<PRODUCT_TRANSACTION> findByProductAndTransactionCode(PRODUCT_DETAILS product, String transactionCode);
    boolean existsByProductAndId(PRODUCT_DETAILS product, UUID transactionId);
    boolean existsByProductAndTransactionCode(PRODUCT_DETAILS product, String transactionCode);
}