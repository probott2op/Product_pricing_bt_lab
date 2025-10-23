package com.lab.product.DTO;

import com.lab.product.entity.ENUMS.PRODUCT_COMM_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_COMM_CHANNEL;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProductCommunicationRequestDTO {
    @NotBlank(message = "Communication code is required")
    private String communicationCode;
    
    @NotBlank(message = "Communication name is required")
    private String communicationName;
    
    @NotNull(message = "Communication type is required")
    private PRODUCT_COMM_TYPE communicationType;
    
    @NotBlank(message = "Template content is required")
    private String templateContent;

    @NotNull(message = "Communication channel is required")
    private PRODUCT_COMM_CHANNEL communicationChannel;
    
    @Min(value = 0, message = "Frequency limit must be non-negative")
    private Integer frequencyLimit;
    
    private String description;
    private boolean isActive = true;
}