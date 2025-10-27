package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.CRUD_VALUE;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
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
