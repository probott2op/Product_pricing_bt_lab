package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_BALANCE_TYPE;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Product Balance
 * Indicates which balance types are supported/applicable for a product
 */
@Data
public class ProductBalanceDTO {
    private UUID balanceId;
    private PRODUCT_BALANCE_TYPE balanceType;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
