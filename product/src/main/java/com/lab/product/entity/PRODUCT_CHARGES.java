package com.lab.product.entity;

import com.lab.product.entity.ENUMS.PRODUCT_CHARGE_FREQUENCY;
import com.lab.product.entity.ENUMS.PRODUCT_CHARGE_CALCULATION_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_CHARGE_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_DebitCredit;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product_charges")
@Data
public class PRODUCT_CHARGES extends AuditLoggable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID chargeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PRODUCT_DETAILS product;

    @Column(nullable = false, length = 100)
    private String chargeCode;

    @Column(nullable = false)
    private String chargeName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PRODUCT_CHARGE_TYPE chargeType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PRODUCT_CHARGE_CALCULATION_TYPE calculationType;

    @Column(nullable = false, precision = 26, scale = 4)
    private BigDecimal chargeValue;

    @Enumerated(EnumType.STRING)
    private PRODUCT_CHARGE_FREQUENCY frequency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PRODUCT_DebitCredit debitCredit;

}
