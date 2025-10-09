package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_RULES;
import com.lab.product.entity.PRODUCT_DETAILS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRuleRepository extends JpaRepository<PRODUCT_RULES, UUID> {
    Page<PRODUCT_RULES> findByProduct(PRODUCT_DETAILS product, Pageable pageable);
    Optional<PRODUCT_RULES> findByProductAndRuleId(PRODUCT_DETAILS product, UUID ruleId);
    Optional<PRODUCT_RULES> findByProductAndRuleCode(PRODUCT_DETAILS product, String ruleCode);
    boolean existsByProductAndRuleId(PRODUCT_DETAILS product, UUID ruleId);
    boolean existsByProductAndRuleCode(PRODUCT_DETAILS product, String ruleCode);
}