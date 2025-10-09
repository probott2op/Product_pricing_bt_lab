package com.lab.product.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductInterestDTO {
    private UUID rateId;
    private String rateCode;
    private Integer termInMonths;
    private BigDecimal rateCumulative;
    private BigDecimal rateNonCumulativeMonthly;
    private BigDecimal rateNonCumulativeQuarterly;
    private BigDecimal rateNonCumulativeYearly;
}
