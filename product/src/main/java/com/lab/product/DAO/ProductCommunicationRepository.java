package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_COMMUNICATION;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.entity.ENUMS.PRODUCT_COMM_TYPE;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductCommunicationRepository extends JpaRepository<PRODUCT_COMMUNICATION, UUID> {
    Page<PRODUCT_COMMUNICATION> findByProduct(PRODUCT_DETAILS product, Pageable pageable);
    List<PRODUCT_COMMUNICATION> findByProduct(PRODUCT_DETAILS product);
    List<PRODUCT_COMMUNICATION> findByProductAndCommunicationType(PRODUCT_DETAILS product, PRODUCT_COMM_TYPE communicationType);
    java.util.Optional<PRODUCT_COMMUNICATION> findByProductAndCommCode(PRODUCT_DETAILS product, String commCode);
    boolean existsByProduct(PRODUCT_DETAILS product);
    boolean existsByProductAndCommCode(PRODUCT_DETAILS product, String commCode);
}
