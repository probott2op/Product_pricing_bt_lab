package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.entity.ENUMS.PRODUCT_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_STATUS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductDetailsRepository extends JpaRepository<PRODUCT_DETAILS, UUID> {
    
    // INSERT-ONLY Pattern: Find latest version by productCode, but return only if the latest is not deleted
    @Query("SELECT p FROM PRODUCT_DETAILS p WHERE p.productCode = :productCode " +
           "AND p.createdAt = (SELECT MAX(p2.createdAt) FROM PRODUCT_DETAILS p2 WHERE p2.productCode = :productCode) " +
           "AND p.crud_value != 'D'")
    Optional<PRODUCT_DETAILS> findLatestByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find all versions (including deleted) for audit trail
    @Query("SELECT p FROM PRODUCT_DETAILS p WHERE p.productCode = :productCode " +
           "ORDER BY p.createdAt DESC")
    List<PRODUCT_DETAILS> findAllVersionsByProductCode(@Param("productCode") String productCode);
    
    // Legacy method - now returns latest version only
    @Query("SELECT p FROM PRODUCT_DETAILS p WHERE p.productCode = :productCode " +
           "AND p.crud_value != 'D' " +
           "ORDER BY p.createdAt DESC")
    Optional<PRODUCT_DETAILS> findByProductCode(@Param("productCode") String productCode);
    
    @Query("SELECT p FROM PRODUCT_DETAILS p WHERE p.productType = :productType " +
           "AND p.createdAt = (SELECT MAX(p2.createdAt) FROM PRODUCT_DETAILS p2 WHERE p2.productCode = p.productCode) " +
           "AND p.crud_value != 'D'")
    List<PRODUCT_DETAILS> findByProductType(@Param("productType") PRODUCT_TYPE productType);
    
    @Query("SELECT p FROM PRODUCT_DETAILS p WHERE p.status = :status " +
           "AND p.createdAt = (SELECT MAX(p2.createdAt) FROM PRODUCT_DETAILS p2 WHERE p2.productCode = p.productCode) " +
           "AND p.crud_value != 'D'")
    List<PRODUCT_DETAILS> findByStatus(@Param("status") PRODUCT_STATUS status);
    
    @Query("SELECT p FROM PRODUCT_DETAILS p WHERE p.efctv_date > :startDate AND p.efctv_date < :endDate " +
           "AND p.createdAt = (SELECT MAX(p2.createdAt) FROM PRODUCT_DETAILS p2 WHERE p2.productCode = p.productCode) " +
           "AND p.crud_value != 'D'")
    List<PRODUCT_DETAILS> findByEfctv_dateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
}
