package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_INTEREST;
import com.lab.product.entity.PRODUCT_DETAILS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductInterestRepository extends JpaRepository<PRODUCT_INTEREST, UUID> {
    Page<PRODUCT_INTEREST> findByProduct(PRODUCT_DETAILS product, Pageable pageable);
    Optional<PRODUCT_INTEREST> findByProductAndRateId(PRODUCT_DETAILS product, UUID interestId);
    Optional<PRODUCT_INTEREST> findByProductAndRateCode(PRODUCT_DETAILS product, String rateCode);
    boolean existsByProductAndRateId(PRODUCT_DETAILS product, UUID interestId);
    boolean existsByProductAndRateCode(PRODUCT_DETAILS product, String rateCode);
}