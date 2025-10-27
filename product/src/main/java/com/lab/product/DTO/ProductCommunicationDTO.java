package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.CRUD_VALUE;
import com.lab.product.entity.ENUMS.PRODUCT_COMM_CHANNEL;
import com.lab.product.entity.ENUMS.PRODUCT_COMM_TYPE;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
public class ProductCommunicationDTO {
    private UUID commId;
    private String commCode;
    private PRODUCT_COMM_TYPE communicationType;
    private PRODUCT_COMM_CHANNEL channel;
    private String event;
    private String template;
    private Integer frequencyLimit;
    
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
