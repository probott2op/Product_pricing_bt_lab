package com.lab.product.service.impl;

import com.lab.product.DTO.ProductRoleDTO;
import com.lab.product.DTO.ProductRoleRequestDTO;
import com.lab.product.entity.PRODUCT_ROLE;
import com.lab.product.entity.PRODUCT_DETAILS;
import com.lab.product.entity.ENUMS.PRODUCT_ROLE_TYPE;
import com.lab.product.Exception.ResourceNotFoundException;
import com.lab.product.DAO.ProductRoleRepository;
import com.lab.product.DAO.ProductDetailsRepository;
import com.lab.product.service.ProductRoleService;
import com.lab.product.service.helper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductRoleServiceImpl implements ProductRoleService {
    
    private final ProductRoleRepository roleRepository;
    private final ProductDetailsRepository productRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductRoleDTO addRoleToProduct(String productCode, ProductRoleRequestDTO roleDto) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_ROLE role = new PRODUCT_ROLE();
        role.setProduct(product);
        // INSERT-ONLY Pattern: Set productCode for cross-version linking
        role.setProductCode(productCode);
        role.setRoleCode(roleDto.getRoleCode());
        role.setRoleType(PRODUCT_ROLE_TYPE.valueOf(roleDto.getRoleType()));
        role.setMandatory(roleDto.isActive());
        role.setMaxCount(1);
        
        // INSERT-ONLY Pattern: Fill audit fields for CREATE operation
        mapper.fillAuditFieldsForCreate(role);
        
        PRODUCT_ROLE saved = roleRepository.save(role);
        return mapper.toRoleDto(saved);
    }

    @Override
    public Page<ProductRoleDTO> getRolesForProduct(String productCode, Pageable pageable) {
        // INSERT-ONLY Pattern: Find latest version of product
        PRODUCT_DETAILS product = productRepository.findLatestByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        return roleRepository.findByProduct(product, pageable)
                .map(mapper::toRoleDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductRoleDTO getRoleByCode(String productCode, String roleCode) {
        // INSERT-ONLY Pattern: Use productCode-based query to get latest version
        PRODUCT_ROLE role = roleRepository.findByProductCodeAndRoleCode(productCode, roleCode)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleCode));
            
        return mapper.toRoleDto(role);
    }

    @Override
    @Transactional
    public ProductRoleDTO updateRole(String productCode, String roleCode, ProductRoleRequestDTO roleDto) {
        // INSERT-ONLY Pattern: Find existing role by productCode and roleCode
        PRODUCT_ROLE existing = roleRepository.findByProductCodeAndRoleCode(productCode, roleCode)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleCode));
            
        // INSERT-ONLY Pattern: Create NEW object instead of modifying existing
        PRODUCT_ROLE newVersion = new PRODUCT_ROLE();
        // Copy all fields from existing (excluding roleId and versionTimestamp)
        BeanUtils.copyProperties(existing, newVersion, "roleId");
            
        // Apply updates from DTO
        newVersion.setRoleCode(roleDto.getRoleCode());
        newVersion.setRoleType(PRODUCT_ROLE_TYPE.valueOf(roleDto.getRoleType()));
        newVersion.setMandatory(roleDto.isActive());
        newVersion.setMaxCount(1);
        
        // INSERT-ONLY Pattern: Fill audit fields for UPDATE operation
        mapper.fillAuditFieldsForUpdate(newVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with same productCode
        PRODUCT_ROLE updated = roleRepository.save(newVersion);
        return mapper.toRoleDto(updated);
    }

    @Override
    @Transactional
    public void deleteRole(String productCode, String roleCode) {
        // INSERT-ONLY Pattern: Find existing role by productCode and roleCode
        PRODUCT_ROLE existing = roleRepository.findByProductCodeAndRoleCode(productCode, roleCode)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleCode));
        
        // INSERT-ONLY Pattern: Create NEW object for delete marker (soft delete)
        PRODUCT_ROLE deleteVersion = new PRODUCT_ROLE();
        // Copy all fields from existing (excluding roleId and versionTimestamp)
        BeanUtils.copyProperties(existing, deleteVersion, "roleId");
        
        // INSERT-ONLY Pattern: Fill audit fields for DELETE operation
        mapper.fillAuditFieldsForDelete(deleteVersion);
        
        // INSERT-ONLY Pattern: Save creates NEW row with crud_value='D' (soft delete marker)
        roleRepository.save(deleteVersion);
    }
}