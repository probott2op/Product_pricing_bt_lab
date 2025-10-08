package com.lab.product.entity;


import com.lab.product.entity.ENUMS.PRODUCT_CURRENCY;
import com.lab.product.entity.ENUMS.PRODUCT_STATUS;
import com.lab.product.entity.ENUMS.PRODUCT_TYPE;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
public class PRODUCT_DETAILS extends AuditLoggable {
    @Id
    @Column(name = "PRODUCT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productId;

    @Column(name = "PRODUCT_CODE", unique = true, nullable = false, length = 50)
    private String productCode;

    @Column(name = "PRODUCT_NAME", nullable = false)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRODUCT_TYPE", nullable = false)
    private PRODUCT_TYPE productType;

    @Column(name = "PRODUCT_CURRENCY", length = 3)
    private PRODUCT_CURRENCY currency;

    @Enumerated(EnumType.STRING)
    @Column(name="PRODUCT_STATUS")
    private PRODUCT_STATUS status;

    // Relationships
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PRODUCT_RULES> productRules;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PRODUCT_CHARGES> productCharges;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PRODUCT_ROLE> productRoles;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PRODUCT_TRANSACTION> productTransactions;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PRODUCT_BALANCE> productBalances;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PRODUCT_COMMUNICATION> productCommunications;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PRODUCT_INTEREST> productInterest;
}
