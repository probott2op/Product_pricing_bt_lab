package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_CHARGES;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductChargesRepository extends JpaRepository<PRODUCT_CHARGES, UUID> {
    
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
}
