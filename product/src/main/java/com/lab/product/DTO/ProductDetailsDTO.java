package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_CURRENCY;
import com.lab.product.entity.ENUMS.PRODUCT_STATUS;
import com.lab.product.entity.ENUMS.PRODUCT_TYPE;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class ProductDetailsDTO {
    private UUID productId;
    private String productCode;
    private String productName;
    private PRODUCT_TYPE productType;
    private PRODUCT_CURRENCY currency;
    private PRODUCT_STATUS status;
    private List<ProductRuleDTO> productRules;
    private List<ProductChargeDTO> productCharges;
    private List<ProductRoleDTO> productRoles;
    private List<ProductTransactionDTO> productTransactions;
    private List<ProductBalanceDTO> productBalances;
    private List<ProductCommunicationDTO> productCommunications;
    private LocalDateTime createdAt;
    private Date efctv_date;
}
