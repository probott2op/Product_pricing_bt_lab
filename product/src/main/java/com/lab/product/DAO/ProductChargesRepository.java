package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_CHARGES;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductChargesRepository extends JpaRepository<PRODUCT_CHARGES, UUID> {
}
