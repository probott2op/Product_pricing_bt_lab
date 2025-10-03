package com.lab.product.repository;

import com.lab.product.entity.PRODUCT_COMMUNICATION;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductCommunicationRepository extends JpaRepository<PRODUCT_COMMUNICATION, UUID> {
}
