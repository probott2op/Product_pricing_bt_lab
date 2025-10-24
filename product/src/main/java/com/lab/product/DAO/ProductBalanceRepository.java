package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_BALANCE;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.entity.ENUMS.PRODUCT_BALANCE_TYPE;
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
public interface ProductBalanceRepository extends JpaRepository<PRODUCT_BALANCE, UUID> {
    
    // INSERT-ONLY Pattern: Find latest non-deleted versions by productCode
    @Query("SELECT b FROM PRODUCT_BALANCE b WHERE b.productCode = :productCode " +
           "AND b.crud_value != 'D' " +
           "ORDER BY b.createdAt DESC")
    List<PRODUCT_BALANCE> findByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find all versions for audit trail
    @Query("SELECT b FROM PRODUCT_BALANCE b WHERE b.productCode = :productCode " +
           "ORDER BY b.createdAt DESC")
    List<PRODUCT_BALANCE> findAllVersionsByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find specific balance by productCode and balanceType
    @Query("SELECT b FROM PRODUCT_BALANCE b WHERE b.productCode = :productCode " +
           "AND b.balanceType = :balanceType " +
           "AND b.crud_value != 'D' " +
           "ORDER BY b.createdAt DESC")
    Optional<PRODUCT_BALANCE> findByProductCodeAndBalanceType(@Param("productCode") String productCode, 
                                                               @Param("balanceType") PRODUCT_BALANCE_TYPE balanceType);
    
    // Legacy methods - maintained for backward compatibility
    @Query("SELECT b FROM PRODUCT_BALANCE b WHERE b.product = :product " +
           "AND b.crud_value != 'D'")
    Page<PRODUCT_BALANCE> findByProduct(@Param("product") PRODUCT_DETAILS product, Pageable pageable);
    
    @Query("SELECT b FROM PRODUCT_BALANCE b WHERE b.product = :product " +
           "AND b.balanceId = :balanceId " +
           "AND b.crud_value != 'D' " +
           "ORDER BY b.createdAt DESC")
    Optional<PRODUCT_BALANCE> findByProductAndBalanceId(@Param("product") PRODUCT_DETAILS product, 
                                                         @Param("balanceId") UUID balanceId);
    
    @Query("SELECT b FROM PRODUCT_BALANCE b WHERE b.product = :product " +
           "AND b.balanceType = :balanceType " +
           "AND b.crud_value != 'D' " +
           "ORDER BY b.createdAt DESC")
    Optional<PRODUCT_BALANCE> findByProductAndBalanceType(@Param("product") PRODUCT_DETAILS product, 
                                                           @Param("balanceType") PRODUCT_BALANCE_TYPE balanceType);
    
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM PRODUCT_BALANCE b " +
           "WHERE b.product = :product AND b.balanceId = :balanceId AND b.crud_value != 'D'")
    boolean existsByProductAndBalanceId(@Param("product") PRODUCT_DETAILS product, 
                                        @Param("balanceId") UUID balanceId);
    
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM PRODUCT_BALANCE b " +
           "WHERE b.product = :product AND b.balanceType = :balanceType AND b.crud_value != 'D'")
    boolean existsByProductAndBalanceType(@Param("product") PRODUCT_DETAILS product, 
                                          @Param("balanceType") PRODUCT_BALANCE_TYPE balanceType);
}