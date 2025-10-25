package com.lab.product.service.impl;

import com.lab.product.DAO.ProductCommunicationRepository;
import com.lab.product.DAO.ProductDetailsRepository;
import com.lab.product.DTO.ProductCommunicationDTO;
import com.lab.product.DTO.ProductCommunicationRequestDTO;
import com.lab.product.entity.PRODUCT_COMMUNICATION;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.Exception.ResourceNotFoundException;
import com.lab.product.service.ProductCommunicationService;
import com.lab.product.service.helper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCommunicationServiceImpl implements ProductCommunicationService {

    private final ProductCommunicationRepository communicationRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductCommunicationDTO addCommunicationToProduct(String productCode, ProductCommunicationRequestDTO communicationDto) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_COMMUNICATION communication = new PRODUCT_COMMUNICATION();
        communication.setProduct(product);
        // INSERT-ONLY Pattern: Set productCode for cross-version linking
        communication.setProductCode(productCode);
        communication.setCommCode(communicationDto.getCommunicationCode());
        communication.setCommunicationType(communicationDto.getCommunicationType());
        communication.setChannel(communicationDto.getCommunicationChannel());
        communication.setEvent(communicationDto.getCommunicationCode());
        communication.setTemplate(communicationDto.getTemplateContent());
        communication.setFrequencyLimit(communicationDto.getFrequencyLimit());
        
        // INSERT-ONLY Pattern: Fill audit fields for CREATE operation
        productMapper.fillAuditFieldsForCreate(communication);
        
        PRODUCT_COMMUNICATION saved = communicationRepository.save(communication);
        return productMapper.toCommunicationDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductCommunicationDTO> getCommunicationsForProduct(String productCode, Pageable pageable) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        return communicationRepository.findByProduct(product, pageable)
                .map(productMapper::toCommunicationDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCommunicationDTO getCommunicationByCode(String productCode, String commCode) {
        // INSERT-ONLY Pattern: Use productCode-based query to get latest version
        PRODUCT_COMMUNICATION communication = communicationRepository.findByProductCodeAndCommCode(productCode, commCode)
                .orElseThrow(() -> new ResourceNotFoundException("Communication not found: " + commCode));

        return productMapper.toCommunicationDto(communication);
    }

    @Override
    @Transactional
    public ProductCommunicationDTO updateCommunication(String productCode, String commCode, ProductCommunicationRequestDTO communicationDto) {
        // INSERT-ONLY Pattern: Find existing communication by productCode and commCode
        PRODUCT_COMMUNICATION existing = communicationRepository.findByProductCodeAndCommCode(productCode, commCode)
                .orElseThrow(() -> new ResourceNotFoundException("Communication not found: " + commCode));

        // INSERT-ONLY Pattern: Create NEW object instead of modifying existing
        PRODUCT_COMMUNICATION newVersion = new PRODUCT_COMMUNICATION();
        // Copy all fields from existing (excluding commId and versionTimestamp)
        BeanUtils.copyProperties(existing, newVersion, "commId");

        // Apply updates from DTO
        newVersion.setCommCode(communicationDto.getCommunicationCode());
        newVersion.setCommunicationType(communicationDto.getCommunicationType());
        newVersion.setChannel(communicationDto.getCommunicationChannel());
        newVersion.setEvent(communicationDto.getCommunicationCode());
        newVersion.setTemplate(communicationDto.getTemplateContent());
        newVersion.setFrequencyLimit(communicationDto.getFrequencyLimit());

        // INSERT-ONLY Pattern: Fill audit fields for UPDATE operation
        productMapper.fillAuditFieldsForUpdate(newVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with same productCode
        PRODUCT_COMMUNICATION updated = communicationRepository.save(newVersion);
        return productMapper.toCommunicationDto(updated);
    }

    @Override
    @Transactional
    public void deleteCommunication(String productCode, String commCode) {
        // INSERT-ONLY Pattern: Find existing communication by productCode and commCode
        PRODUCT_COMMUNICATION existing = communicationRepository.findByProductCodeAndCommCode(productCode, commCode)
                .orElseThrow(() -> new ResourceNotFoundException("Communication not found: " + commCode));

        // INSERT-ONLY Pattern: Create NEW object for delete marker (soft delete)
        PRODUCT_COMMUNICATION deleteVersion = new PRODUCT_COMMUNICATION();
        // Copy all fields from existing (excluding commId and versionTimestamp)
        BeanUtils.copyProperties(existing, deleteVersion, "commId");
        
        // INSERT-ONLY Pattern: Fill audit fields for DELETE operation
        productMapper.fillAuditFieldsForDelete(deleteVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with crud_value='D' (soft delete marker)
        communicationRepository.save(deleteVersion);
    }

    @Override
    public List<ProductCommunicationDTO> getCommunicationsAuditTrail(String productCode) {
        List<PRODUCT_COMMUNICATION> allVersions = communicationRepository.findAllVersionsByProductCode(productCode);
        if (allVersions.isEmpty()) {
            throw new ResourceNotFoundException("No communications found for product: " + productCode);
        }
        return allVersions.stream()
                .map(productMapper::toCommunicationDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductCommunicationDTO> getCommunicationAuditTrail(String productCode, String commCode) {
        List<PRODUCT_COMMUNICATION> allVersions = communicationRepository.findAllVersionsByProductCodeAndCommCode(productCode, commCode);
        if (allVersions.isEmpty()) {
            throw new ResourceNotFoundException("Communication not found: " + commCode);
        }
        return allVersions.stream()
                .map(productMapper::toCommunicationDto)
                .collect(Collectors.toList());
    }
}