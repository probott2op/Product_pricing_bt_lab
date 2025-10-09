package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_CHARGES;
import com.lab.product.entity.PRODUCT_DETAILS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductChargeRepository extends JpaRepository<PRODUCT_CHARGES, UUID> {
    Page<PRODUCT_CHARGES> findByProduct(PRODUCT_DETAILS product, Pageable pageable);
    Optional<PRODUCT_CHARGES> findByProductAndChargeId(PRODUCT_DETAILS product, UUID chargeId);
    Optional<PRODUCT_CHARGES> findByProductAndChargeCode(PRODUCT_DETAILS product, String chargeCode);
    boolean existsByProductAndChargeId(PRODUCT_DETAILS product, UUID chargeId);
    boolean existsByProductAndChargeCode(PRODUCT_DETAILS product, String chargeCode);
}