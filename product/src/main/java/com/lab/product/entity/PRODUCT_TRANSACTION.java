package com.lab.product.entity;

import com.lab.product.entity.ENUMS.PRODUCT_TRANSACTION_TYPE;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_transaction_types")
@Data
public class PRODUCT_TRANSACTION extends AuditLoggable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PRODUCT_DETAILS product;

    @Column(nullable = false, unique = true, length = 50)
    private String transactionCode; // e.g., "TXN_DEPOSIT", "TXN_WITHDRAWAL"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PRODUCT_TRANSACTION_TYPE transactionType;

    @Column(name = "is_allowed")
    private boolean isAllowed = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
