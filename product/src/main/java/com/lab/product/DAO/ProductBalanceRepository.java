package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_BALANCE;
import com.lab.product.entity.PRODUCT_DETAILS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductBalanceRepository extends JpaRepository<PRODUCT_BALANCE, UUID> {
    Page<PRODUCT_BALANCE> findByProduct(PRODUCT_DETAILS product, Pageable pageable);
    Optional<PRODUCT_BALANCE> findByProductAndBalanceId(PRODUCT_DETAILS product, UUID balanceId);
    Optional<PRODUCT_BALANCE> findByProductAndBalanceCode(PRODUCT_DETAILS product, String balanceCode);
    boolean existsByProductAndBalanceId(PRODUCT_DETAILS product, UUID balanceId);
    boolean existsByProductAndBalanceCode(PRODUCT_DETAILS product, String balanceCode);
}