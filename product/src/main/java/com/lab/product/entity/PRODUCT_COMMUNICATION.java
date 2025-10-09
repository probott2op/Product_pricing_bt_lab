package com.lab.product.entity;

import com.lab.product.entity.ENUMS.PRODUCT_COMM_CHANNEL;
import com.lab.product.entity.ENUMS.PRODUCT_COMM_TYPE;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "product_communications")
@Data
public class PRODUCT_COMMUNICATION extends AuditLoggable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID commId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PRODUCT_DETAILS product;

    @Column(nullable = false, unique = true, length = 50)
    private String commCode; // e.g., "COMM_ALERT_EMAIL", "COMM_STATEMENT_SMS"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PRODUCT_COMM_TYPE communicationType; // e.g., ALERT, NOTICE, STATEMENT

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PRODUCT_COMM_CHANNEL channel; // e.g., EMAIL, SMS, POST

    @Column(nullable = false)
    private String event; // e.g., "ACCOUNT_OPENING", "TRANSACTION_COMPLETE"

    @Column(columnDefinition = "TEXT")
    private String template; // The message template
}
