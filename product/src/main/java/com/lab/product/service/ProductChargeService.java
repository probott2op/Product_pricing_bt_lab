package com.lab.product.service;

import com.lab.product.DTO.ProductChargeDTO;
import com.lab.product.DTO.ProductChargeRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductChargeService {
    ProductChargeDTO addChargeToProduct(String productCode, ProductChargeRequestDTO chargeDto);
    Page<ProductChargeDTO> getChargesForProduct(String productCode, Pageable pageable);
    ProductChargeDTO getChargeById(String productCode, UUID chargeId);
    ProductChargeDTO updateCharge(String productCode, UUID chargeId, ProductChargeRequestDTO chargeDto);
    void deleteCharge(String productCode, UUID chargeId);
}