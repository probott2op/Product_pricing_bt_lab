package com.lab.product.DAO;

import com.lab.product.entity.PRODUCT_RULES;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRulesRepository extends JpaRepository<PRODUCT_RULES, UUID> {
}
