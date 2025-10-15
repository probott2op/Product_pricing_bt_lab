package com.lab.product.entity.ENUMS;

/**
 * Defines how frequently interest is compounded
 */
public enum COMPOUNDING_FREQUENCY {
    /**
     * Interest compounded daily
     */
    DAILY,
    
    /**
     * Interest compounded monthly
     */
    MONTHLY,
    
    /**
     * Interest compounded quarterly (every 3 months)
     */
    QUARTERLY,
    
    /**
     * Interest compounded semi-annually (every 6 months)
     */
    SEMI_ANNUALLY,
    
    /**
     * Interest compounded annually (once per year)
     */
    ANNUALLY
}
