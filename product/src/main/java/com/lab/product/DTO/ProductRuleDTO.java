package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.CRUD_VALUE;
import com.lab.product.entity.ENUMS.PRODUCT_RULE_DATA;
import com.lab.product.entity.ENUMS.PRODUCT_RULE_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_RULE_VALIDATION;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
public class ProductRuleDTO {
    private UUID ruleId;
    private String ruleCode;
    private String ruleName;
    private PRODUCT_RULE_TYPE ruleType;
    private PRODUCT_RULE_DATA dataType;
    private String ruleValue; // could be JSON
    private PRODUCT_RULE_VALIDATION validationType;
    
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
