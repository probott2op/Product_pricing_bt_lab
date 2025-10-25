package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_ROLE;
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
public interface ProductRoleRepository extends JpaRepository<PRODUCT_ROLE, UUID> {
    
    // INSERT-ONLY Pattern: Find latest non-deleted versions by productCode
    @Query("SELECT r FROM PRODUCT_ROLE r WHERE r.productCode = :productCode " +
           "AND r.crud_value != 'D' " +
           "AND r.createdAt = (SELECT MAX(r2.createdAt) FROM PRODUCT_ROLE r2 " +
           "WHERE r2.roleCode = r.roleCode AND r2.productCode = :productCode AND r2.crud_value != 'D') " +
           "ORDER BY r.createdAt DESC")
    List<PRODUCT_ROLE> findByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find all versions for audit trail
    @Query("SELECT r FROM PRODUCT_ROLE r WHERE r.productCode = :productCode " +
           "ORDER BY r.createdAt DESC")
    List<PRODUCT_ROLE> findAllVersionsByProductCode(@Param("productCode") String productCode);
    
    // INSERT-ONLY Pattern: Find all versions for a specific role code (audit trail)
    @Query("SELECT r FROM PRODUCT_ROLE r WHERE r.productCode = :productCode " +
           "AND r.roleCode = :roleCode " +
           "ORDER BY r.createdAt DESC")
    List<PRODUCT_ROLE> findAllVersionsByProductCodeAndRoleCode(@Param("productCode") String productCode, 
                                                                @Param("roleCode") String roleCode);
    
    // INSERT-ONLY Pattern: Find specific role by productCode and roleCode
    @Query(value = "SELECT r FROM PRODUCT_ROLE r WHERE r.productCode = :productCode " +
           "AND r.roleCode = :roleCode " +
           "AND r.crud_value != 'D' " +
           "ORDER BY r.createdAt DESC LIMIT 1")
    Optional<PRODUCT_ROLE> findByProductCodeAndRoleCode(@Param("productCode") String productCode, 
                                                         @Param("roleCode") String roleCode);
    
    // INSERT-ONLY Pattern: Find latest non-deleted versions for each roleCode by product
    @Query("SELECT r FROM PRODUCT_ROLE r WHERE r.product = :product " +
           "AND r.crud_value != 'D' " +
           "AND r.createdAt = (SELECT MAX(r2.createdAt) FROM PRODUCT_ROLE r2 " +
           "WHERE r2.roleCode = r.roleCode AND r2.crud_value != 'D')")
    Page<PRODUCT_ROLE> findByProduct(@Param("product") PRODUCT_DETAILS product, Pageable pageable);
    
    @Query(value = "SELECT r FROM PRODUCT_ROLE r WHERE r.product = :product " +
           "AND r.roleId = :roleId " +
           "AND r.crud_value != 'D' " +
           "ORDER BY r.createdAt DESC LIMIT 1")
    Optional<PRODUCT_ROLE> findByProductAndRoleId(@Param("product") PRODUCT_DETAILS product, 
                                                   @Param("roleId") UUID roleId);
    
    @Query(value = "SELECT r FROM PRODUCT_ROLE r WHERE r.product = :product " +
           "AND r.roleCode = :roleCode " +
           "AND r.crud_value != 'D' " +
           "ORDER BY r.createdAt DESC LIMIT 1")
    Optional<PRODUCT_ROLE> findByProductAndRoleCode(@Param("product") PRODUCT_DETAILS product, 
                                                     @Param("roleCode") String roleCode);
    
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM PRODUCT_ROLE r " +
           "WHERE r.product = :product AND r.roleId = :roleId AND r.crud_value != 'D'")
    boolean existsByProductAndRoleId(@Param("product") PRODUCT_DETAILS product, 
                                     @Param("roleId") UUID roleId);
    
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM PRODUCT_ROLE r " +
           "WHERE r.product = :product AND r.roleCode = :roleCode AND r.crud_value != 'D'")
    boolean existsByProductAndRoleCode(@Param("product") PRODUCT_DETAILS product, 
                                       @Param("roleCode") String roleCode);
}