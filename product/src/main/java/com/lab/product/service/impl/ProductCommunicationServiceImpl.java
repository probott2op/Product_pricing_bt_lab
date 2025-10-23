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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCommunicationServiceImpl implements ProductCommunicationService {

    private final ProductCommunicationRepository communicationRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductCommunicationDTO addCommunicationToProduct(String productCode, ProductCommunicationRequestDTO communicationDto) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_COMMUNICATION communication = new PRODUCT_COMMUNICATION();
        communication.setProduct(product);
        communication.setCommCode(communicationDto.getCommunicationCode());
        communication.setCommunicationType(communicationDto.getCommunicationType());
        communication.setChannel(communicationDto.getCommunicationChannel());
        communication.setEvent(communicationDto.getCommunicationCode());
        communication.setTemplate(communicationDto.getTemplateContent());
        communication.setFrequencyLimit(communicationDto.getFrequencyLimit());
        PRODUCT_COMMUNICATION saved = communicationRepository.save(productMapper.fillAuditFields(communication));
        return productMapper.toCommunicationDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductCommunicationDTO> getCommunicationsForProduct(String productCode, Pageable pageable) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        return communicationRepository.findByProduct(product, pageable)
                .map(productMapper::toCommunicationDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCommunicationDTO getCommunicationByCode(String productCode, String commCode) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_COMMUNICATION communication = communicationRepository.findByProductAndCommCode(product, commCode)
                .orElseThrow(() -> new ResourceNotFoundException("Communication not found: " + commCode));

        return productMapper.toCommunicationDto(communication);
    }

    @Override
    @Transactional
    public ProductCommunicationDTO updateCommunication(String productCode, String commCode, ProductCommunicationRequestDTO communicationDto) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_COMMUNICATION communication = communicationRepository.findByProductAndCommCode(product, commCode)
                .orElseThrow(() -> new ResourceNotFoundException("Communication not found: " + commCode));

        communication.setCommCode(communicationDto.getCommunicationCode());
        communication.setCommunicationType(communicationDto.getCommunicationType());
        communication.setChannel(communicationDto.getCommunicationChannel());
        communication.setEvent(communicationDto.getCommunicationCode());
        communication.setTemplate(communicationDto.getTemplateContent());
        communication.setFrequencyLimit(communicationDto.getFrequencyLimit());

        PRODUCT_COMMUNICATION updated = communicationRepository.save(productMapper.fillAuditFields(communication));
        return productMapper.toCommunicationDto(updated);
    }

    @Override
    @Transactional
    public void deleteCommunication(String productCode, String commCode) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_COMMUNICATION communication = communicationRepository.findByProductAndCommCode(product, commCode)
                .orElseThrow(() -> new ResourceNotFoundException("Communication not found: " + commCode));

        communicationRepository.delete(communication);
    }
}