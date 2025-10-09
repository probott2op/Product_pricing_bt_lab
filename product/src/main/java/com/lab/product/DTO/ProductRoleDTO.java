package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_ROLE_TYPE;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductRoleDTO {
    private UUID roleId;
    private String roleCode;
    private PRODUCT_ROLE_TYPE roleType;
    private String roleName;
}
