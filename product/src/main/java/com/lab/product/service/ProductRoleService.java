package com.lab.product.service;

import com.lab.product.DTO.ProductRoleDTO;
import com.lab.product.DTO.ProductRoleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductRoleService {
    ProductRoleDTO addRoleToProduct(String productCode, ProductRoleRequestDTO roleDto);
    Page<ProductRoleDTO> getRolesForProduct(String productCode, Pageable pageable);
    ProductRoleDTO getRoleById(String productCode, UUID roleId);
    ProductRoleDTO updateRole(String productCode, UUID roleId, ProductRoleRequestDTO roleDto);
    void deleteRole(String productCode, UUID roleId);
}