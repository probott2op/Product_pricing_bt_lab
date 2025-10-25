package com.lab.product.service;

import com.lab.product.DTO.ProductCommunicationDTO;
import com.lab.product.DTO.ProductCommunicationRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductCommunicationService {
    ProductCommunicationDTO addCommunicationToProduct(String productCode, ProductCommunicationRequestDTO communicationDto);
    Page<ProductCommunicationDTO> getCommunicationsForProduct(String productCode, Pageable pageable);
    ProductCommunicationDTO getCommunicationByCode(String productCode, String commCode);
    ProductCommunicationDTO updateCommunication(String productCode, String commCode, ProductCommunicationRequestDTO communicationDto);
    void deleteCommunication(String productCode, String commCode);
    List<ProductCommunicationDTO> getCommunicationsAuditTrail(String productCode);
    List<ProductCommunicationDTO> getCommunicationAuditTrail(String productCode, String commCode);
}