package com.lab.product.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductCommunicationRequestDTO {
    @NotBlank(message = "Communication code is required")
    private String communicationCode;
    
    @NotBlank(message = "Communication name is required")
    private String communicationName;
    
    @NotNull(message = "Communication type is required")
    private String communicationType;
    
    @NotBlank(message = "Template content is required")
    private String templateContent;
    
    private String description;
    private boolean isActive = true;
}