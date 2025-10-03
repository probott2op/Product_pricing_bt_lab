package com.lab.product.entity;


import com.lab.product.entity.ENUMS.PRODUCT_CUSTOMERCAT;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "interest_rates")
@Data
public class PRODUCT_INTEREST {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID rateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PRODUCT_DETAILS product;

    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Integer minTermDays;
    private Integer maxTermDays;

    @Enumerated(EnumType.STRING)
    private PRODUCT_CUSTOMERCAT customerCategory;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private LocalDate effectiveDate;
    private LocalDate expiryDate;

    @CreationTimestamp
    private LocalDateTime createdAt;


}