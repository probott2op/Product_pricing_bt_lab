package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_BALANCE_TYPE;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductBalanceDTO {
    private UUID balanceId;
    private PRODUCT_BALANCE_TYPE balanceType;
    private BigDecimal amount;
}
