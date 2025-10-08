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
    Optional<PRODUCT_DETAILS> findByProductCode(String productCode);
    List<PRODUCT_DETAILS> findByProductType(PRODUCT_TYPE productType);
    List<PRODUCT_DETAILS> findByStatus(PRODUCT_STATUS status);
    
    @Query("SELECT p FROM PRODUCT_DETAILS p WHERE p.efctv_date > :startDate AND p.efctv_date < :endDate")
    List<PRODUCT_DETAILS> findByEfctv_dateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
}
