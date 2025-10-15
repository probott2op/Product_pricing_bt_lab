package com.lab.product.entity;

import com.lab.product.entity.ENUMS.PRODUCT_BALANCE_TYPE;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Defines which balance types are supported/applicable for a financial product.
 * For example, a Loan product would have: LOAN_PRINCIPAL, LOAN_INTEREST, OVERDRAFT, PENALTY
 * A Fixed Deposit product would have: FD_PRINCIPAL, FD_INTEREST
 * This simply indicates which balances are relevant for the product.
 */
@Entity
@Table(name = "product_balances", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "balance_type"}))
@Data
@lombok.EqualsAndHashCode(callSuper = false)
public class PRODUCT_BALANCE extends AuditLoggable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID balanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PRODUCT_DETAILS product;

    /**
     * Type of balance supported for this product
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PRODUCT_BALANCE_TYPE balanceType;

    /**
     * Whether this balance type is currently active/applicable
     */
    @Column(nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
