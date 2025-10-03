package com.lab.product.entity;

import com.lab.product.entity.ENUMS.PRODUCT_BALANCE_TYPE;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_balances")
@Data
public class PRODUCT_BALANCE extends AuditLoggable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID balanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PRODUCT_DETAILS product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PRODUCT_BALANCE_TYPE balanceType;

    @Column(nullable = false, length = 100)
    private String balanceCode; // e.g., "FD_PRINCIPAL", "LOAN_INTEREST_ACCRUED"

    @CreationTimestamp
    private LocalDateTime createdAt;

}
