package com.lab.product.entity;

import com.lab.product.entity.ENUMS.PRODUCT_ROLE_TYPE;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_role_types")
@Data
public class PRODUCT_ROLE extends AuditLoggable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PRODUCT_DETAILS product;
    
    // INSERT-ONLY Pattern: Business identifier for linking across versions
    @Column(name = "PRODUCT_CODE", nullable = false, length = 50)
    private String productCode;

    @Column(nullable = false, unique = true, length = 50)
    private String roleCode; // e.g., "ROLE_OWNER", "ROLE_NOMINEE"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PRODUCT_ROLE_TYPE roleType;

    @Column(name = "is_mandatory")
    private boolean isMandatory = false;

    private int maxCount = 1;

}
