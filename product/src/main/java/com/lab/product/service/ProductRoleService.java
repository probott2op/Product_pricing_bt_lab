package com.lab.product.service;

import com.lab.product.DTO.ProductRoleDTO;
import com.lab.product.DTO.ProductRoleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductRoleService {
    ProductRoleDTO addRoleToProduct(UUID productId, ProductRoleRequestDTO roleDto);
    Page<ProductRoleDTO> getRolesForProduct(UUID productId, Pageable pageable);
    ProductRoleDTO getRoleById(UUID productId, UUID roleId);
    ProductRoleDTO updateRole(UUID productId, UUID roleId, ProductRoleRequestDTO roleDto);
    void deleteRole(UUID productId, UUID roleId);
}