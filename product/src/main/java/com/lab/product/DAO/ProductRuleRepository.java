package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_RULES;
import com.lab.product.entity.PRODUCT_DETAILS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRuleRepository extends JpaRepository<PRODUCT_RULES, UUID> {
    
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
    
    // Legacy methods - maintained for backward compatibility
    @Query("SELECT r FROM PRODUCT_RULES r WHERE r.product = :product " +
           "AND r.crud_value != 'D'")
    Page<PRODUCT_RULES> findByProduct(@Param("product") PRODUCT_DETAILS product, Pageable pageable);
    
    @Query("SELECT r FROM PRODUCT_RULES r WHERE r.product = :product " +
           "AND r.ruleId = :ruleId " +
           "AND r.crud_value != 'D' " +
           "ORDER BY r.createdAt DESC")
    Optional<PRODUCT_RULES> findByProductAndRuleId(@Param("product") PRODUCT_DETAILS product, 
                                                    @Param("ruleId") UUID ruleId);
    
    @Query("SELECT r FROM PRODUCT_RULES r WHERE r.product = :product " +
           "AND r.ruleCode = :ruleCode " +
           "AND r.crud_value != 'D' " +
           "ORDER BY r.createdAt DESC")
    Optional<PRODUCT_RULES> findByProductAndRuleCode(@Param("product") PRODUCT_DETAILS product, 
                                                      @Param("ruleCode") String ruleCode);
    
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM PRODUCT_RULES r " +
           "WHERE r.product = :product AND r.ruleId = :ruleId AND r.crud_value != 'D'")
    boolean existsByProductAndRuleId(@Param("product") PRODUCT_DETAILS product, 
                                     @Param("ruleId") UUID ruleId);
    
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM PRODUCT_RULES r " +
           "WHERE r.product = :product AND r.ruleCode = :ruleCode AND r.crud_value != 'D'")
    boolean existsByProductAndRuleCode(@Param("product") PRODUCT_DETAILS product, 
                                       @Param("ruleCode") String ruleCode);
}