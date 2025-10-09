package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_COMM_CHANNEL;
import com.lab.product.entity.ENUMS.PRODUCT_COMM_TYPE;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductCommunicationDTO {
    private UUID commId;
    private String commCode;
    private PRODUCT_COMM_TYPE communicationType;
    private PRODUCT_COMM_CHANNEL channel;
    private String event;
    private String template;
}
