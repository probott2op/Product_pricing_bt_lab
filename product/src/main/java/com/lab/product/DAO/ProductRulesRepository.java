package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_RULES;
import com.lab.product.entity.PRODUCT_DETAILS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
           "AND r.createdAt = (SELECT MAX(r2.createdAt) FROM PRODUCT_RULES r2 " +
           "WHERE r2.ruleCode = r.ruleCode AND r2.productCode = :productCode AND r2.crud_value != 'D') " +
           "ORDER BY r.createdAt DESC")
    List<PRODUCT_RULES> findByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find all versions for audit trail
    @Query("SELECT r FROM PRODUCT_RULES r WHERE r.productCode = :productCode " +
           "ORDER BY r.createdAt DESC")
    List<PRODUCT_RULES> findAllVersionsByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find all versions for a specific rule code (audit trail)
    @Query("SELECT r FROM PRODUCT_RULES r WHERE r.productCode = :productCode " +
           "AND r.ruleCode = :ruleCode " +
           "ORDER BY r.createdAt DESC")
    List<PRODUCT_RULES> findAllVersionsByProductCodeAndRuleCode(@Param("productCode") String productCode, 
                                                                 @Param("ruleCode") String ruleCode);
    
    // INSERT-ONLY Pattern: Find specific rule by productCode and ruleCode
    @Query("SELECT r FROM PRODUCT_RULES r WHERE r.productCode = :productCode " +
           "AND r.ruleCode = :ruleCode " +
           "AND r.crud_value != 'D' " +
           "ORDER BY r.createdAt DESC")
    Optional<PRODUCT_RULES> findByProductCodeAndRuleCode(@Param("productCode") String productCode, 
                                                          @Param("ruleCode") String ruleCode);
    
    // INSERT-ONLY Pattern: Find latest non-deleted versions for each ruleCode by product
    @Query("SELECT r FROM PRODUCT_RULES r WHERE r.product = :product " +
           "AND r.crud_value != 'D' " +
           "AND r.createdAt = (SELECT MAX(r2.createdAt) FROM PRODUCT_RULES r2 " +
           "WHERE r2.ruleCode = r.ruleCode AND r2.crud_value != 'D')")
    Page<PRODUCT_RULES> findByProduct(@Param("product") PRODUCT_DETAILS product, Pageable pageable);
}
