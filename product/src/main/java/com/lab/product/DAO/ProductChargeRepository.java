package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_CHARGES;
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
public interface ProductChargeRepository extends JpaRepository<PRODUCT_CHARGES, UUID> {
    
    // INSERT-ONLY Pattern: Find latest non-deleted versions by productCode
    @Query("SELECT c FROM PRODUCT_CHARGES c WHERE c.productCode = :productCode " +
           "AND c.crud_value != 'D' " +
           "ORDER BY c.createdAt DESC")
    List<PRODUCT_CHARGES> findByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find all versions for audit trail
    @Query("SELECT c FROM PRODUCT_CHARGES c WHERE c.productCode = :productCode " +
           "ORDER BY c.createdAt DESC")
    List<PRODUCT_CHARGES> findAllVersionsByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find specific charge by productCode and chargeCode
    @Query("SELECT c FROM PRODUCT_CHARGES c WHERE c.productCode = :productCode " +
           "AND c.chargeCode = :chargeCode " +
           "AND c.crud_value != 'D' " +
           "ORDER BY c.createdAt DESC")
    Optional<PRODUCT_CHARGES> findByProductCodeAndChargeCode(@Param("productCode") String productCode, 
                                                              @Param("chargeCode") String chargeCode);
    
    // INSERT-ONLY Pattern: Find latest non-deleted versions for each chargeCode by product
    @Query("SELECT c FROM PRODUCT_CHARGES c WHERE c.product = :product " +
           "AND c.crud_value != 'D' " +
           "AND c.createdAt = (SELECT MAX(c2.createdAt) FROM PRODUCT_CHARGES c2 " +
           "WHERE c2.chargeCode = c.chargeCode AND c2.crud_value != 'D')")
    Page<PRODUCT_CHARGES> findByProduct(@Param("product") PRODUCT_DETAILS product, Pageable pageable);
    
    @Query("SELECT c FROM PRODUCT_CHARGES c WHERE c.product = :product " +
           "AND c.chargeId = :chargeId " +
           "AND c.crud_value != 'D' " +
           "ORDER BY c.createdAt DESC")
    Optional<PRODUCT_CHARGES> findByProductAndChargeId(@Param("product") PRODUCT_DETAILS product, 
                                                        @Param("chargeId") UUID chargeId);
    
    @Query("SELECT c FROM PRODUCT_CHARGES c WHERE c.product = :product " +
           "AND c.chargeCode = :chargeCode " +
           "AND c.crud_value != 'D' " +
           "ORDER BY c.createdAt DESC")
    Optional<PRODUCT_CHARGES> findByProductAndChargeCode(@Param("product") PRODUCT_DETAILS product, 
                                                          @Param("chargeCode") String chargeCode);
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM PRODUCT_CHARGES c " +
           "WHERE c.product = :product AND c.chargeId = :chargeId AND c.crud_value != 'D'")
    boolean existsByProductAndChargeId(@Param("product") PRODUCT_DETAILS product, 
                                       @Param("chargeId") UUID chargeId);
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM PRODUCT_CHARGES c " +
           "WHERE c.product = :product AND c.chargeCode = :chargeCode AND c.crud_value != 'D'")
    boolean existsByProductAndChargeCode(@Param("product") PRODUCT_DETAILS product, 
                                         @Param("chargeCode") String chargeCode);
}