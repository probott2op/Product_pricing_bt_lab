package com.lab.product.service;

import com.lab.product.DTO.ProductRoleDTO;
import com.lab.product.DTO.ProductRoleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRoleService {
    ProductRoleDTO addRoleToProduct(String productCode, ProductRoleRequestDTO roleDto);
    Page<ProductRoleDTO> getRolesForProduct(String productCode, Pageable pageable);
    ProductRoleDTO getRoleByCode(String productCode, String roleCode);
    ProductRoleDTO updateRole(String productCode, String roleCode, ProductRoleRequestDTO roleDto);
    void deleteRole(String productCode, String roleCode);
}