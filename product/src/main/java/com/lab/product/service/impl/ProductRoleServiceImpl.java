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
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        PRODUCT_ROLE role = new PRODUCT_ROLE();
        role.setProduct(product);
        role.setRoleCode(roleDto.getRoleCode());
        role.setRoleType(PRODUCT_ROLE_TYPE.valueOf(roleDto.getRoleType()));
        role.setMandatory(roleDto.isActive());
        role.setMaxCount(1);
        
        // Fill audit fields
        mapper.fillAuditFields(role);
        
        PRODUCT_ROLE saved = roleRepository.save(role);
        return mapper.toRoleDto(saved);
    }

    @Override
    public Page<ProductRoleDTO> getRolesForProduct(String productCode, Pageable pageable) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        return roleRepository.findByProduct(product, pageable)
                .map(mapper::toRoleDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductRoleDTO getRoleByCode(String productCode, String roleCode) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        PRODUCT_ROLE role = roleRepository.findByProductAndRoleCode(product, roleCode)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleCode));
            
        return mapper.toRoleDto(role);
    }

    @Override
    @Transactional
    public ProductRoleDTO updateRole(String productCode, String roleCode, ProductRoleRequestDTO roleDto) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
            
        PRODUCT_ROLE role = roleRepository.findByProductAndRoleCode(product, roleCode)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleCode));
            
        role.setRoleCode(roleDto.getRoleCode());
        role.setRoleType(PRODUCT_ROLE_TYPE.valueOf(roleDto.getRoleType()));
        role.setMandatory(roleDto.isActive());
        role.setMaxCount(1);
        
        // Update audit fields
        mapper.fillAuditFields(role);
        
        PRODUCT_ROLE updated = roleRepository.save(role);
        return mapper.toRoleDto(updated);
    }

    @Override
    @Transactional
    public void deleteRole(String productCode, String roleCode) {
        PRODUCT_DETAILS product = productRepository.findByProductCode(productCode)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));
        
        PRODUCT_ROLE role = roleRepository.findByProductAndRoleCode(product, roleCode)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleCode));
            
        roleRepository.delete(role);
    }
}