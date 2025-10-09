package com.lab.product.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductInterestRequestDTO {
    @NotNull(message = "Rate code is required")
    private String rateCode;
    
    @NotNull(message = "Term in months is required")
    @Positive(message = "Term in months must be positive")
    private Integer termInMonths;
    
    @NotNull(message = "Cumulative rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cumulative rate must be greater than 0")
    private BigDecimal rateCumulative;
    
    @NotNull(message = "Non-cumulative monthly rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Non-cumulative monthly rate must be greater than 0")
    private BigDecimal rateNonCumulativeMonthly;
    
    @NotNull(message = "Non-cumulative quarterly rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Non-cumulative quarterly rate must be greater than 0")
    private BigDecimal rateNonCumulativeQuarterly;
    
    @NotNull(message = "Non-cumulative yearly rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Non-cumulative yearly rate must be greater than 0")
    private BigDecimal rateNonCumulativeYearly;
}
