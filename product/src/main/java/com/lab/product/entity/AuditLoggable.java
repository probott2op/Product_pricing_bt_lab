package com.lab.product.entity;

import com.lab.product.entity.ENUMS.CRUD_VALUE;
import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

// Use @MappedSuperclass to avoid creating a table for this abstract class
@MappedSuperclass
@Data
public abstract class AuditLoggable {

    @CreationTimestamp
    @Column(name = "PRODUCT_CRTN_DATE")
    private LocalDateTime createdAt;
    @Column(name = "PRODUCT_EFCTV_DATE")
    private Date efctv_date;
    @Column(name = "PRODUCT_CRUD_VALUE")
    @Enumerated(EnumType.STRING)
    private CRUD_VALUE crud_value;
    @Column(name = "PRODUCT_USER_ID")
    private String user_id;
    @Column(name = "PRODUCT_WS_ID")
    private String ws_id;
    @Column(name = "PRODUCT_PRGM_ID")
    private String prgm_id;
    @Column(name = "PRODUCT_HOST_TS")
    @UpdateTimestamp
    private Timestamp host_ts;
    @UpdateTimestamp
    @Column(name = "PRODUCT_LOCAL_TS")
    private Timestamp local_ts;
    @UpdateTimestamp
    @Column(name = "PRODUCT_ACPT_TS")
    private Timestamp acpt_ts;
    @UpdateTimestamp
    @Column(name = "PRODUCT_ACPT_TS_UTC_OFST")
    private Timestamp acpt_ts_utc_ofst;
    @Column(name = "PRODUCT_UUID")
    private UUID UUID_reference;

}
