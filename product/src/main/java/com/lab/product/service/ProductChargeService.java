package com.lab.product.service;

import com.lab.product.DTO.ProductChargeDTO;
import com.lab.product.DTO.ProductChargeRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductChargeService {
    ProductChargeDTO addChargeToProduct(UUID productId, ProductChargeRequestDTO chargeDto);
    Page<ProductChargeDTO> getChargesForProduct(UUID productId, Pageable pageable);
    ProductChargeDTO getChargeById(UUID productId, UUID chargeId);
    ProductChargeDTO updateCharge(UUID productId, UUID chargeId, ProductChargeRequestDTO chargeDto);
    void deleteCharge(UUID productId, UUID chargeId);
}