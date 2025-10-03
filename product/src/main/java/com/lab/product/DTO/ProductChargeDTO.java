package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_CHARGE_CALCULATION_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_CHARGE_FREQUENCY;
import com.lab.product.entity.ENUMS.PRODUCT_CHARGE_TYPE;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductChargeDTO {
    private UUID chargeId;
    private String chargeCode;
    private PRODUCT_CHARGE_TYPE chargeType;
    private PRODUCT_CHARGE_CALCULATION_TYPE calculationType;
    private PRODUCT_CHARGE_FREQUENCY frequency;
    private BigDecimal amount;
}
