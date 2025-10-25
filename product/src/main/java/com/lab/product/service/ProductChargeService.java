package com.lab.product.service;

import com.lab.product.DTO.ProductChargeDTO;
import com.lab.product.DTO.ProductChargeRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductChargeService {
    ProductChargeDTO addChargeToProduct(String productCode, ProductChargeRequestDTO chargeDto);
    Page<ProductChargeDTO> getChargesForProduct(String productCode, Pageable pageable);
    ProductChargeDTO getChargeByCode(String productCode, String chargeCode);
    ProductChargeDTO updateCharge(String productCode, String chargeCode, ProductChargeRequestDTO chargeDto);
    void deleteCharge(String productCode, String chargeCode);
    List<ProductChargeDTO> getChargesAuditTrail(String productCode);
    List<ProductChargeDTO> getChargeAuditTrail(String productCode, String chargeCode);
}