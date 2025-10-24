package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_COMMUNICATION;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.entity.ENUMS.PRODUCT_COMM_TYPE;

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
public interface ProductCommunicationRepository extends JpaRepository<PRODUCT_COMMUNICATION, UUID> {
    
    // INSERT-ONLY Pattern: Find latest non-deleted versions by productCode
    @Query("SELECT c FROM PRODUCT_COMMUNICATION c WHERE c.productCode = :productCode " +
           "AND c.crud_value != 'D' " +
           "ORDER BY c.createdAt DESC")
    List<PRODUCT_COMMUNICATION> findByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find all versions for audit trail
    @Query("SELECT c FROM PRODUCT_COMMUNICATION c WHERE c.productCode = :productCode " +
           "ORDER BY c.createdAt DESC")
    List<PRODUCT_COMMUNICATION> findAllVersionsByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find specific communication by productCode and commCode
    @Query("SELECT c FROM PRODUCT_COMMUNICATION c WHERE c.productCode = :productCode " +
           "AND c.commCode = :commCode " +
           "AND c.crud_value != 'D' " +
           "ORDER BY c.createdAt DESC")
    Optional<PRODUCT_COMMUNICATION> findByProductCodeAndCommCode(@Param("productCode") String productCode, 
                                                                  @Param("commCode") String commCode);
    
    // INSERT-ONLY Pattern: Find by productCode and communicationType
    @Query("SELECT c FROM PRODUCT_COMMUNICATION c WHERE c.productCode = :productCode " +
           "AND c.communicationType = :communicationType " +
           "AND c.crud_value != 'D' " +
           "ORDER BY c.createdAt DESC")
    List<PRODUCT_COMMUNICATION> findByProductCodeAndCommunicationType(@Param("productCode") String productCode, 
                                                                       @Param("communicationType") PRODUCT_COMM_TYPE communicationType);
    
    // INSERT-ONLY Pattern: Find latest non-deleted versions for each commCode by product
    @Query("SELECT c FROM PRODUCT_COMMUNICATION c WHERE c.product = :product " +
           "AND c.crud_value != 'D' " +
           "AND c.createdAt = (SELECT MAX(c2.createdAt) FROM PRODUCT_COMMUNICATION c2 " +
           "WHERE c2.commCode = c.commCode AND c2.crud_value != 'D')")
    Page<PRODUCT_COMMUNICATION> findByProduct(@Param("product") PRODUCT_DETAILS product, Pageable pageable);
    
    @Query("SELECT c FROM PRODUCT_COMMUNICATION c WHERE c.product = :product " +
           "AND c.crud_value != 'D'")
    List<PRODUCT_COMMUNICATION> findByProduct(@Param("product") PRODUCT_DETAILS product);
    
    @Query("SELECT c FROM PRODUCT_COMMUNICATION c WHERE c.product = :product " +
           "AND c.communicationType = :communicationType " +
           "AND c.crud_value != 'D'")
    List<PRODUCT_COMMUNICATION> findByProductAndCommunicationType(@Param("product") PRODUCT_DETAILS product, 
                                                                   @Param("communicationType") PRODUCT_COMM_TYPE communicationType);
    
    @Query("SELECT c FROM PRODUCT_COMMUNICATION c WHERE c.product = :product " +
           "AND c.commCode = :commCode " +
           "AND c.crud_value != 'D' " +
           "ORDER BY c.createdAt DESC")
    Optional<PRODUCT_COMMUNICATION> findByProductAndCommCode(@Param("product") PRODUCT_DETAILS product, 
                                                              @Param("commCode") String commCode);
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM PRODUCT_COMMUNICATION c " +
           "WHERE c.product = :product AND c.crud_value != 'D'")
    boolean existsByProduct(@Param("product") PRODUCT_DETAILS product);
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM PRODUCT_COMMUNICATION c " +
           "WHERE c.product = :product AND c.commCode = :commCode AND c.crud_value != 'D'")
    boolean existsByProductAndCommCode(@Param("product") PRODUCT_DETAILS product, 
                                       @Param("commCode") String commCode);
}
