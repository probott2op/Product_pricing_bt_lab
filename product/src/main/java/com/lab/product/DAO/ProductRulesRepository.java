package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_RULES;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRulesRepository extends JpaRepository<PRODUCT_RULES, UUID> {
    
    // INSERT-ONLY Pattern: Find latest non-deleted versions by productCode
    @Query("SELECT r FROM PRODUCT_RULES r WHERE r.productCode = :productCode " +
           "AND r.crud_value != 'D' " +
           "ORDER BY r.createdAt DESC")
    List<PRODUCT_RULES> findByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find all versions for audit trail
    @Query("SELECT r FROM PRODUCT_RULES r WHERE r.productCode = :productCode " +
           "ORDER BY r.createdAt DESC")
    List<PRODUCT_RULES> findAllVersionsByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find specific rule by productCode and ruleCode
    @Query("SELECT r FROM PRODUCT_RULES r WHERE r.productCode = :productCode " +
           "AND r.ruleCode = :ruleCode " +
           "AND r.crud_value != 'D' " +
           "ORDER BY r.createdAt DESC")
    Optional<PRODUCT_RULES> findByProductCodeAndRuleCode(@Param("productCode") String productCode, 
                                                          @Param("ruleCode") String ruleCode);
}
