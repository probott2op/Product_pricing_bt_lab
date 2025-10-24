package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_TRANSACTION;
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
public interface ProductTransactionRepository extends JpaRepository<PRODUCT_TRANSACTION, UUID> {
    
    // INSERT-ONLY Pattern: Find latest non-deleted versions by productCode
    @Query("SELECT t FROM PRODUCT_TRANSACTION t WHERE t.productCode = :productCode " +
           "AND t.crud_value != 'D' " +
           "ORDER BY t.createdAt DESC")
    List<PRODUCT_TRANSACTION> findByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find all versions for audit trail
    @Query("SELECT t FROM PRODUCT_TRANSACTION t WHERE t.productCode = :productCode " +
           "ORDER BY t.createdAt DESC")
    List<PRODUCT_TRANSACTION> findAllVersionsByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find specific transaction by productCode and transactionCode
    @Query("SELECT t FROM PRODUCT_TRANSACTION t WHERE t.productCode = :productCode " +
           "AND t.transactionCode = :transactionCode " +
           "AND t.crud_value != 'D' " +
           "ORDER BY t.createdAt DESC")
    Optional<PRODUCT_TRANSACTION> findByProductCodeAndTransactionCode(@Param("productCode") String productCode, 
                                                                       @Param("transactionCode") String transactionCode);
    
    // Legacy methods - maintained for backward compatibility
    @Query("SELECT t FROM PRODUCT_TRANSACTION t WHERE t.product = :product " +
           "AND t.crud_value != 'D'")
    Page<PRODUCT_TRANSACTION> findByProduct(@Param("product") PRODUCT_DETAILS product, Pageable pageable);
    
    @Query("SELECT t FROM PRODUCT_TRANSACTION t WHERE t.product = :product " +
           "AND t.id = :transactionId " +
           "AND t.crud_value != 'D' " +
           "ORDER BY t.createdAt DESC")
    Optional<PRODUCT_TRANSACTION> findByProductAndId(@Param("product") PRODUCT_DETAILS product, 
                                                      @Param("transactionId") UUID transactionId);
    
    @Query("SELECT t FROM PRODUCT_TRANSACTION t WHERE t.product = :product " +
           "AND t.transactionCode = :transactionCode " +
           "AND t.crud_value != 'D' " +
           "ORDER BY t.createdAt DESC")
    Optional<PRODUCT_TRANSACTION> findByProductAndTransactionCode(@Param("product") PRODUCT_DETAILS product, 
                                                                   @Param("transactionCode") String transactionCode);
    
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM PRODUCT_TRANSACTION t " +
           "WHERE t.product = :product AND t.id = :transactionId AND t.crud_value != 'D'")
    boolean existsByProductAndId(@Param("product") PRODUCT_DETAILS product, 
                                 @Param("transactionId") UUID transactionId);
    
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM PRODUCT_TRANSACTION t " +
           "WHERE t.product = :product AND t.transactionCode = :transactionCode AND t.crud_value != 'D'")
    boolean existsByProductAndTransactionCode(@Param("product") PRODUCT_DETAILS product, 
                                              @Param("transactionCode") String transactionCode);
}