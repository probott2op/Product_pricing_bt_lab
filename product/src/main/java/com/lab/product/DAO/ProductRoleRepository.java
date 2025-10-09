package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_ROLE;
import com.lab.product.entity.PRODUCT_DETAILS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRoleRepository extends JpaRepository<PRODUCT_ROLE, UUID> {
    Page<PRODUCT_ROLE> findByProduct(PRODUCT_DETAILS product, Pageable pageable);
    Optional<PRODUCT_ROLE> findByProductAndRoleId(PRODUCT_DETAILS product, UUID roleId);
    Optional<PRODUCT_ROLE> findByProductAndRoleCode(PRODUCT_DETAILS product, String roleCode);
    boolean existsByProductAndRoleId(PRODUCT_DETAILS product, UUID roleId);
    boolean existsByProductAndRoleCode(PRODUCT_DETAILS product, String roleCode);
}