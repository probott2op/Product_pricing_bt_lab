package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.COMPOUNDING_FREQUENCY;
import com.lab.product.entity.ENUMS.CRUD_VALUE;
import com.lab.product.entity.ENUMS.INTEREST_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_CURRENCY;
import com.lab.product.entity.ENUMS.PRODUCT_STATUS;
import com.lab.product.entity.ENUMS.PRODUCT_TYPE;
import lombok.Data;

import java.sql.Timestamp;
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
    private INTEREST_TYPE interestType;
    private COMPOUNDING_FREQUENCY compoundingFrequency;
    private List<ProductRuleDTO> productRules;
    private List<ProductChargeDTO> productCharges;
    private List<ProductRoleDTO> productRoles;
    private List<ProductTransactionDTO> productTransactions;
    private List<ProductBalanceDTO> productBalances;
    private List<ProductCommunicationDTO> productCommunications;
    private List<ProductInterestDTO> productInterests;
    
    // Audit fields - INSERT-ONLY Pattern tracking
    private LocalDateTime createdAt;
    private Date efctv_date;
    private CRUD_VALUE crud_value;
    private String user_id;
    private String ws_id;
    private String prgm_id;
    private Timestamp host_ts;
    private Timestamp local_ts;
    private Timestamp acpt_ts;
    private Timestamp acpt_ts_utc_ofst;
    private UUID UUID_reference;
}
