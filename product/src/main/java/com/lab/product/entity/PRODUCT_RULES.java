package com.lab.product.entity;

import com.lab.product.entity.ENUMS.PRODUCT_RULE_DATA;
import com.lab.product.entity.ENUMS.PRODUCT_RULE_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_RULE_VALIDATION;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

// All the business rules related to a product
@Entity
@Table(name = "PRODUCT_RULES")
@Data
public class PRODUCT_RULES extends AuditLoggable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="RULE_ID")
    private UUID ruleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PRODUCT_DETAILS product;

    @Column(name = "RULE_CODE", nullable = false, length = 100)
    private String ruleCode;

    @Column(name = "RULE_NAME", nullable = false)
    private String ruleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "RULE_TYPE", nullable = false)
    private PRODUCT_RULE_TYPE ruleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "RULE_DATA_TYPE", nullable = false)
    private PRODUCT_RULE_DATA dataType;

    @Column(name = "RULE_VALUE", nullable = false, columnDefinition = "TEXT")
    private String ruleValue; // Can store simple values or complex JSON strings

//    @Column(name = "is_account_level")
//    private boolean isAccountLevel = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "RULE_VALIDATION_TYPE")
    private PRODUCT_RULE_VALIDATION validationType;

}