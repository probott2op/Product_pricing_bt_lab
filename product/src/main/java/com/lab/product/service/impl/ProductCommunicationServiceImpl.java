package com.lab.product.service.impl;

import com.lab.product.DAO.ProductCommunicationRepository;
import com.lab.product.DAO.ProductDetailsRepository;
import com.lab.product.DTO.ProductCommunicationDTO;
import com.lab.product.DTO.ProductCommunicationRequestDTO;
import com.lab.product.entity.PRODUCT_COMMUNICATION;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.entity.ENUMS.PRODUCT_COMM_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_COMM_CHANNEL;
import com.lab.product.Exception.ResourceNotFoundException;
import com.lab.product.service.ProductCommunicationService;
import com.lab.product.service.helper.ProductMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCommunicationServiceImpl implements ProductCommunicationService {

    private final ProductCommunicationRepository communicationRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductCommunicationDTO addCommunicationToProduct(UUID productId, ProductCommunicationRequestDTO communicationDto) {
        PRODUCT_DETAILS product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        PRODUCT_COMMUNICATION communication = new PRODUCT_COMMUNICATION();
        communication.setProduct(product);
        communication.setCommunicationType(PRODUCT_COMM_TYPE.valueOf(communicationDto.getCommunicationType()));
        // Set default values for required fields
        communication.setChannel(PRODUCT_COMM_CHANNEL.EMAIL); // Default to EMAIL
        communication.setEvent(communicationDto.getCommunicationCode());
        communication.setTemplate(communicationDto.getTemplateContent());

        PRODUCT_COMMUNICATION saved = communicationRepository.save(productMapper.fillAuditFields(communication));
        return productMapper.toCommunicationDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductCommunicationDTO> getCommunicationsForProduct(UUID productId, Pageable pageable) {
        PRODUCT_DETAILS product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        return communicationRepository.findByProduct(product, pageable)
                .map(productMapper::toCommunicationDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCommunicationDTO getCommunicationById(UUID productId, UUID communicationId) {
        PRODUCT_DETAILS product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        PRODUCT_COMMUNICATION communication = communicationRepository.findById(communicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Communication not found with id: " + communicationId));

        if (!communication.getProduct().equals(product)) {
            throw new ResourceNotFoundException("Communication not found for this product");
        }

        return productMapper.toCommunicationDto(communication);
    }

    @Override
    @Transactional
    public ProductCommunicationDTO updateCommunication(UUID productId, UUID communicationId, ProductCommunicationRequestDTO communicationDto) {
        PRODUCT_DETAILS product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        PRODUCT_COMMUNICATION communication = communicationRepository.findById(communicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Communication not found with id: " + communicationId));

        if (!communication.getProduct().equals(product)) {
            throw new ResourceNotFoundException("Communication not found for this product");
        }

        communication.setCommunicationType(PRODUCT_COMM_TYPE.valueOf(communicationDto.getCommunicationType()));
        communication.setEvent(communicationDto.getCommunicationCode());
        communication.setTemplate(communicationDto.getTemplateContent());

        PRODUCT_COMMUNICATION updated = communicationRepository.save(productMapper.fillAuditFields(communication));
        return productMapper.toCommunicationDto(updated);
    }

    @Override
    @Transactional
    public void deleteCommunication(UUID productId, UUID communicationId) {
        PRODUCT_DETAILS product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        PRODUCT_COMMUNICATION communication = communicationRepository.findById(communicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Communication not found with id: " + communicationId));

        if (!communication.getProduct().equals(product)) {
            throw new ResourceNotFoundException("Communication not found for this product");
        }

        communicationRepository.delete(communication);
    }
}