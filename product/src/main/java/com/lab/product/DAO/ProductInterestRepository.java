package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_INTEREST;
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
public interface ProductInterestRepository extends JpaRepository<PRODUCT_INTEREST, UUID> {
    
    // INSERT-ONLY Pattern: Find latest non-deleted versions by productCode
    @Query("SELECT i FROM PRODUCT_INTEREST i WHERE i.productCode = :productCode " +
           "AND i.crud_value != 'D' " +
           "ORDER BY i.createdAt DESC")
    List<PRODUCT_INTEREST> findByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find all versions for audit trail
    @Query("SELECT i FROM PRODUCT_INTEREST i WHERE i.productCode = :productCode " +
           "ORDER BY i.createdAt DESC")
    List<PRODUCT_INTEREST> findAllVersionsByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find specific rate by productCode and rateCode
    @Query("SELECT i FROM PRODUCT_INTEREST i WHERE i.productCode = :productCode " +
           "AND i.rateCode = :rateCode " +
           "AND i.crud_value != 'D' " +
           "ORDER BY i.createdAt DESC")
    Optional<PRODUCT_INTEREST> findByProductCodeAndRateCode(@Param("productCode") String productCode, 
                                                             @Param("rateCode") String rateCode);
    
    // INSERT-ONLY Pattern: Find latest non-deleted versions for each rateCode by product
    @Query("SELECT i FROM PRODUCT_INTEREST i WHERE i.product = :product " +
           "AND i.crud_value != 'D' " +
           "AND i.createdAt = (SELECT MAX(i2.createdAt) FROM PRODUCT_INTEREST i2 " +
           "WHERE i2.rateCode = i.rateCode AND i2.crud_value != 'D')")
    Page<PRODUCT_INTEREST> findByProduct(@Param("product") PRODUCT_DETAILS product, Pageable pageable);
    
    @Query("SELECT i FROM PRODUCT_INTEREST i WHERE i.product = :product " +
           "AND i.rateId = :interestId " +
           "AND i.crud_value != 'D' " +
           "ORDER BY i.createdAt DESC")
    Optional<PRODUCT_INTEREST> findByProductAndRateId(@Param("product") PRODUCT_DETAILS product, 
                                                       @Param("interestId") UUID interestId);
    
    @Query("SELECT i FROM PRODUCT_INTEREST i WHERE i.product = :product " +
           "AND i.rateCode = :rateCode " +
           "AND i.crud_value != 'D' " +
           "ORDER BY i.createdAt DESC")
    Optional<PRODUCT_INTEREST> findByProductAndRateCode(@Param("product") PRODUCT_DETAILS product, 
                                                         @Param("rateCode") String rateCode);
    
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM PRODUCT_INTEREST i " +
           "WHERE i.product = :product AND i.rateId = :interestId AND i.crud_value != 'D'")
    boolean existsByProductAndRateId(@Param("product") PRODUCT_DETAILS product, 
                                     @Param("interestId") UUID interestId);
    
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM PRODUCT_INTEREST i " +
           "WHERE i.product = :product AND i.rateCode = :rateCode AND i.crud_value != 'D'")
    boolean existsByProductAndRateCode(@Param("product") PRODUCT_DETAILS product, 
                                       @Param("rateCode") String rateCode);
}