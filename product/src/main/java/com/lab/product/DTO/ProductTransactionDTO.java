package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_TRANSACTION_TYPE;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductTransactionDTO {
    private UUID transactionId;
    private String transactionCode;
    private PRODUCT_TRANSACTION_TYPE transactionType;
    private BigDecimal amountLimit;
}
