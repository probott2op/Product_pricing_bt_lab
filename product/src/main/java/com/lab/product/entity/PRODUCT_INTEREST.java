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
public class PRODUCT_INTEREST extends AuditLoggable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID rateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PRODUCT_DETAILS product;

    @Column(nullable = false, unique = true, length = 50)
    private String rateCode; // e.g., "RATE_12M", "RATE_36M"

    @Column(nullable = false)
    private Integer termInMonths; // e.g., 12, 24, 36, 60

    // Base rate for cumulative option (paid at maturity)
    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal rateCumulative; // e.g., for 36 months, this would be 8.00% -> 0.0800

     // Base rates for non-cumulative options
    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal rateNonCumulativeMonthly; // e.g., 7.85% -> 0.0785

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal rateNonCumulativeQuarterly; // e.g., 7.90% -> 0.0790

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal rateNonCumulativeYearly; // e.g., 8.00% -> 0.0800


}