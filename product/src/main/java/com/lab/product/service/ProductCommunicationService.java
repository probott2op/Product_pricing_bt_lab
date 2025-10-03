package com.lab.product.service;

import com.lab.product.DTO.ProductCommunicationDTO;
import com.lab.product.DTO.ProductCommunicationRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductCommunicationService {
    ProductCommunicationDTO addCommunicationToProduct(UUID productId, ProductCommunicationRequestDTO communicationDto);
    Page<ProductCommunicationDTO> getCommunicationsForProduct(UUID productId, Pageable pageable);
    ProductCommunicationDTO getCommunicationById(UUID productId, UUID communicationId);
    ProductCommunicationDTO updateCommunication(UUID productId, UUID communicationId, ProductCommunicationRequestDTO communicationDto);
    void deleteCommunication(UUID productId, UUID communicationId);
}