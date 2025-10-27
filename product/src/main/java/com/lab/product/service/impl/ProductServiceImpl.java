package com.lab.product.service.impl;

import com.lab.product.DTO.CreateOrUpdateProductRequestDTO;
import com.lab.product.DTO.ProductDetailsDTO;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.entity.ENUMS.COMPOUNDING_FREQUENCY;
import com.lab.product.entity.ENUMS.INTEREST_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_STATUS;
import com.lab.product.entity.ENUMS.PRODUCT_CURRENCY;
import com.lab.product.DAO.ProductDetailsRepository;
import com.lab.product.service.ProductService;
import com.lab.product.service.helper.ProductMapper;
import com.lab.product.Exception.ResourceNotFoundException;
import com.lab.product.Exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductDetailsRepository productDetailsRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductDetailsDTO createProduct(CreateOrUpdateProductRequestDTO requestDTO) {
        validateProductRequest(requestDTO);
        
        PRODUCT_DETAILS entity = mapper.toEntity(requestDTO);
        // INSERT-ONLY Pattern: Use fillAuditFieldsForCreate for new products
        entity = mapper.fillAuditFieldsForCreate(entity);
        
        // Set enums with validation
        try {
            if (requestDTO.getProductType() != null) {
                entity.setProductType(PRODUCT_TYPE.valueOf(requestDTO.getProductType()));
            }
            if (requestDTO.getStatus() != null) {
                entity.setStatus(PRODUCT_STATUS.valueOf(requestDTO.getStatus()));
            }
            if (requestDTO.getCurrency() != null) {
                entity.setCurrency(PRODUCT_CURRENCY.valueOf(requestDTO.getCurrency()));
            }
            if (requestDTO.getInterestType() != null) {
                entity.setInterestType(INTEREST_TYPE.valueOf(requestDTO.getInterestType()));
            }
            if (requestDTO.getCompoundingFrequency() != null) {
                entity.setCompoundingFrequency(COMPOUNDING_FREQUENCY.valueOf(requestDTO.getCompoundingFrequency()));
            }
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid enum value: " + e.getMessage());
        }

        // Basic product details only
        PRODUCT_DETAILS saved = productDetailsRepository.save(entity);
        return mapper.toDto(saved);
    }

    private void validateProductRequest(CreateOrUpdateProductRequestDTO request) {
        if (request == null) {
            throw new ValidationException("Product request cannot be null");
        }
        if (request.getProductCode() == null || request.getProductCode().trim().isEmpty()) {
            throw new ValidationException("Product code is required");
        }
        if (request.getProductName() == null || request.getProductName().trim().isEmpty()) {
            throw new ValidationException("Product name is required");
        }
        if (request.getProductType() == null) {
            throw new ValidationException("Product type is required");
        }
    }

    @Override
    @Transactional
    public ProductDetailsDTO updateProduct(String productCode, CreateOrUpdateProductRequestDTO requestDTO) {
        if (productCode == null || productCode.trim().isEmpty()) {
            throw new ValidationException("Product code cannot be null or empty");
        }
        
        validateProductRequest(requestDTO);
        
        // INSERT-ONLY Pattern: Find latest non-deleted version
        PRODUCT_DETAILS existing = productDetailsRepository.findLatestByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
        
        // INSERT-ONLY Pattern: Create NEW object instead of modifying existing
        PRODUCT_DETAILS newVersion = new PRODUCT_DETAILS();
        // Copy all fields from existing (excluding productId - createdAt will be auto-set)
        BeanUtils.copyProperties(existing, newVersion, "productId", "productRules", 
                "productCharges", "productRoles", "productTransactions", "productBalances", 
                "productCommunications", "productInterest");
        
        // Apply updates from DTO
        mapper.updateEntityFromDto(newVersion, requestDTO);
        
        // Update enums with validation
        try {
            if (requestDTO.getProductType() != null) {
                newVersion.setProductType(PRODUCT_TYPE.valueOf(requestDTO.getProductType()));
            }
            if (requestDTO.getStatus() != null) {
                newVersion.setStatus(PRODUCT_STATUS.valueOf(requestDTO.getStatus()));
            }
            if (requestDTO.getInterestType() != null) {
                newVersion.setInterestType(INTEREST_TYPE.valueOf(requestDTO.getInterestType()));
            }
            if (requestDTO.getCompoundingFrequency() != null) {
                newVersion.setCompoundingFrequency(COMPOUNDING_FREQUENCY.valueOf(requestDTO.getCompoundingFrequency()));
            }
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid enum value: " + e.getMessage());
        }
        
        // INSERT-ONLY Pattern: Set audit fields for UPDATE operation
        newVersion = mapper.fillAuditFieldsForUpdate(newVersion);

        // INSERT-ONLY Pattern: Save creates NEW row with same productCode but different productId
        PRODUCT_DETAILS saved = productDetailsRepository.save(newVersion);
        return mapper.toDto(saved);
    }

    @Override
    public ProductDetailsDTO getProductById(UUID productId) {
        PRODUCT_DETAILS p = productDetailsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
        return mapper.toDto(p);
    }

    @Override
    public ProductDetailsDTO getProductByCode(String productCode) {
        // INSERT-ONLY Pattern: Use findLatestByProductCode to get current version
        PRODUCT_DETAILS p = productDetailsRepository.findLatestByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
        return mapper.toDto(p);
    }

    @Override
    public List<ProductDetailsDTO> searchProducts(String productType, String status, String startDate, String endDate) {
        try {
            List<PRODUCT_DETAILS> results;

            if (productType != null) {
                PRODUCT_TYPE typeEnum = PRODUCT_TYPE.valueOf(productType.toUpperCase());
                if (typeEnum == null) {
                    throw new ResourceNotFoundException("Product type not found: " + productType);
                }
                results = productDetailsRepository.findByProductType(typeEnum);
            } else if (status != null) {
                PRODUCT_STATUS statusEnum = PRODUCT_STATUS.valueOf(status.toUpperCase());
                if (statusEnum == null) {
                    throw new ResourceNotFoundException("Product status not found: " + status);
                }
                results = productDetailsRepository.findByStatus(statusEnum);
            } else if (startDate != null && endDate != null) {
                try {
                    Date start = Date.valueOf(LocalDate.parse(startDate));
                    Date end = Date.valueOf(LocalDate.parse(endDate));
                    
                    if (end.before(start)) {
                        throw new ValidationException("End date must be after start date");
                    }
                    
                    results = productDetailsRepository.findByEfctv_dateBetween(start, end);
                } catch (DateTimeParseException e) {
                    throw new ValidationException("Invalid date format. Use ISO format (YYYY-MM-DD)");
                }
            } else {
                results = productDetailsRepository.findAll();
            }

            return results.stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid search parameter: " + e.getMessage());
        }
    }

    @Override
    public Page<ProductDetailsDTO> getAllProducts(Pageable pageable) {
        return productDetailsRepository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional
    public void deleteProduct(String productCode) {
        // INSERT-ONLY Pattern: Find latest non-deleted version
        PRODUCT_DETAILS existing = productDetailsRepository.findLatestByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
        
        // INSERT-ONLY Pattern: Create NEW object for delete marker (soft delete)
        PRODUCT_DETAILS deleteVersion = new PRODUCT_DETAILS();
        // Copy all fields from existing (excluding productId - createdAt will be auto-set)
        BeanUtils.copyProperties(existing, deleteVersion, "productId", "productRules", 
                "productCharges", "productRoles", "productTransactions", "productBalances", 
                "productCommunications", "productInterest");
        
        // INSERT-ONLY Pattern: Set audit fields for DELETE operation
        deleteVersion = mapper.fillAuditFieldsForDelete(deleteVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with crud_value='D' (soft delete marker)
        productDetailsRepository.save(deleteVersion);
    }

    @Override
    public List<ProductDetailsDTO> getProductAuditTrail(String productCode) {
        List<PRODUCT_DETAILS> allVersions = productDetailsRepository.findAllVersionsByProductCode(productCode);
        if (allVersions.isEmpty()) {
            throw new ResourceNotFoundException("Product not found: " + productCode);
        }
        return allVersions.stream()
                .map(mapper::toDtoWithAudit)
                .collect(Collectors.toList());
    }

    // Removed validateRateMatrixEntry method as it's no longer needed with the simplified DTO structure
}
