package com.lab.product.entity.ENUMS;

/**
 * Defines the types of balances that can be supported for different product types.
 * Each product can specify which balance types are applicable to it.
 */
public enum PRODUCT_BALANCE_TYPE {
    /**
     * Principal balance for loans - outstanding loan amount
     */
    LOAN_PRINCIPAL,

    /**
     * Interest balance for loans - accrued or charged interest
     */
    LOAN_INTEREST,

    /**
     * Principal balance for Fixed Deposits - deposited amount
     */
    FD_PRINCIPAL,

    /**
     * Interest balance for Fixed Deposits - earned interest
     */
    FD_INTEREST,

    /**
     * Overdraft balance - negative balance facility
     */
    OVERDRAFT,

    /**
     * Penalty charges - late payment or violation penalties
     */
    PENALTY
}
