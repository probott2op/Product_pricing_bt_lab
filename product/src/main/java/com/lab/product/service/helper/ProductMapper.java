package com.lab.product.service.helper;

import com.lab.product.DAO.*;
import com.lab.product.DTO.*;
import com.lab.product.entity.*;
import com.lab.product.entity.ENUMS.CRUD_VALUE;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    
    private final ProductInterestRepository interestRepository;
    private final ProductChargeRepository chargeRepository;
    private final ProductCommunicationRepository communicationRepository;
    private final ProductTransactionRepository transactionRepository;
    private final ProductRoleRepository roleRepository;
    private final ProductBalanceRepository balanceRepository;
    private final ProductRulesRepository rulesRepository;

    public ProductBalanceDTO toBalanceDto(PRODUCT_BALANCE balance) {
        if (balance == null) return null;
        ProductBalanceDTO dto = new ProductBalanceDTO();
        dto.setBalanceId(balance.getBalanceId());
        dto.setBalanceType(balance.getBalanceType());
        dto.setIsActive(balance.getIsActive());
        dto.setCreatedAt(balance.getCreatedAt());
        return dto;
    }
    
    // Audit trail version - includes all audit fields
    public ProductBalanceDTO toBalanceDtoWithAudit(PRODUCT_BALANCE balance) {
        if (balance == null) return null;
        ProductBalanceDTO dto = toBalanceDto(balance);
        
        // Copy audit fields
        dto.setEfctv_date(balance.getEfctv_date());
        dto.setCrud_value(balance.getCrud_value());
        dto.setUser_id(balance.getUser_id());
        dto.setWs_id(balance.getWs_id());
        dto.setPrgm_id(balance.getPrgm_id());
        dto.setHost_ts(balance.getHost_ts());
        dto.setLocal_ts(balance.getLocal_ts());
        dto.setAcpt_ts(balance.getAcpt_ts());
        dto.setAcpt_ts_utc_ofst(balance.getAcpt_ts_utc_ofst());
        dto.setUUID_reference(balance.getUUID_reference());
        
        return dto;
    }
    
    public ProductChargeDTO toChargeDto(PRODUCT_CHARGES charge) {
        if (charge == null) return null;
        ProductChargeDTO dto = new ProductChargeDTO();
        dto.setChargeId(charge.getChargeId());
        dto.setChargeCode(charge.getChargeCode());
        dto.setChargeName(charge.getChargeName());
        dto.setChargeType(charge.getChargeType());
        dto.setCalculationType(charge.getCalculationType());
        dto.setFrequency(charge.getFrequency());
        dto.setAmount(charge.getChargeValue());
        dto.setDebitCredit(charge.getDebitCredit());
        return dto;
    }
    
    // Audit trail version - includes all audit fields
    public ProductChargeDTO toChargeDtoWithAudit(PRODUCT_CHARGES charge) {
        if (charge == null) return null;
        ProductChargeDTO dto = toChargeDto(charge);
        
        // Copy audit fields
        dto.setCreatedAt(charge.getCreatedAt());
        dto.setEfctv_date(charge.getEfctv_date());
        dto.setCrud_value(charge.getCrud_value());
        dto.setUser_id(charge.getUser_id());
        dto.setWs_id(charge.getWs_id());
        dto.setPrgm_id(charge.getPrgm_id());
        dto.setHost_ts(charge.getHost_ts());
        dto.setLocal_ts(charge.getLocal_ts());
        dto.setAcpt_ts(charge.getAcpt_ts());
        dto.setAcpt_ts_utc_ofst(charge.getAcpt_ts_utc_ofst());
        dto.setUUID_reference(charge.getUUID_reference());
        
        return dto;
    }
    
    public ProductRoleDTO toRoleDto(PRODUCT_ROLE role) {
        if (role == null) return null;
        ProductRoleDTO dto = new ProductRoleDTO();
        dto.setRoleId(role.getRoleId());
        dto.setRoleCode(role.getRoleCode());
        dto.setRoleType(role.getRoleType());
        dto.setRoleName(role.getRoleType() != null ? role.getRoleType().name() : null);
        dto.setMandatory(role.isMandatory());
        dto.setMaxCount(role.getMaxCount());
        return dto;
    }
    
    // Audit trail version - includes all audit fields
    public ProductRoleDTO toRoleDtoWithAudit(PRODUCT_ROLE role) {
        if (role == null) return null;
        ProductRoleDTO dto = toRoleDto(role);
        
        // Copy audit fields
        dto.setCreatedAt(role.getCreatedAt());
        dto.setEfctv_date(role.getEfctv_date());
        dto.setCrud_value(role.getCrud_value());
        dto.setUser_id(role.getUser_id());
        dto.setWs_id(role.getWs_id());
        dto.setPrgm_id(role.getPrgm_id());
        dto.setHost_ts(role.getHost_ts());
        dto.setLocal_ts(role.getLocal_ts());
        dto.setAcpt_ts(role.getAcpt_ts());
        dto.setAcpt_ts_utc_ofst(role.getAcpt_ts_utc_ofst());
        dto.setUUID_reference(role.getUUID_reference());
        
        return dto;
    }
    
    public ProductRuleDTO toRuleDto(PRODUCT_RULES rule) {
        if (rule == null) return null;
        ProductRuleDTO dto = new ProductRuleDTO();
        dto.setRuleCode(rule.getRuleCode());
        dto.setRuleName(rule.getRuleName());
        dto.setRuleId(rule.getRuleId());
        dto.setRuleType(rule.getRuleType());
        dto.setDataType(rule.getDataType());
        dto.setRuleValue(rule.getRuleValue());
        dto.setValidationType(rule.getValidationType());
        return dto;
    }
    
    // Audit trail version - includes all audit fields
    public ProductRuleDTO toRuleDtoWithAudit(PRODUCT_RULES rule) {
        if (rule == null) return null;
        ProductRuleDTO dto = toRuleDto(rule);
        
        // Copy audit fields
        dto.setCreatedAt(rule.getCreatedAt());
        dto.setEfctv_date(rule.getEfctv_date());
        dto.setCrud_value(rule.getCrud_value());
        dto.setUser_id(rule.getUser_id());
        dto.setWs_id(rule.getWs_id());
        dto.setPrgm_id(rule.getPrgm_id());
        dto.setHost_ts(rule.getHost_ts());
        dto.setLocal_ts(rule.getLocal_ts());
        dto.setAcpt_ts(rule.getAcpt_ts());
        dto.setAcpt_ts_utc_ofst(rule.getAcpt_ts_utc_ofst());
        dto.setUUID_reference(rule.getUUID_reference());
        
        return dto;
    }
    
    public ProductTransactionDTO toTransactionDto(PRODUCT_TRANSACTION transaction) {
        if (transaction == null) return null;
        ProductTransactionDTO dto = new ProductTransactionDTO();
        dto.setTransactionId(transaction.getId());
        dto.setTransactionCode(transaction.getTransactionCode());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setAllowed(transaction.isAllowed());
        return dto;
    }
    
    // Audit trail version - includes all audit fields
    public ProductTransactionDTO toTransactionDtoWithAudit(PRODUCT_TRANSACTION transaction) {
        if (transaction == null) return null;
        ProductTransactionDTO dto = toTransactionDto(transaction);
        
        // Copy audit fields
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setEfctv_date(transaction.getEfctv_date());
        dto.setCrud_value(transaction.getCrud_value());
        dto.setUser_id(transaction.getUser_id());
        dto.setWs_id(transaction.getWs_id());
        dto.setPrgm_id(transaction.getPrgm_id());
        dto.setHost_ts(transaction.getHost_ts());
        dto.setLocal_ts(transaction.getLocal_ts());
        dto.setAcpt_ts(transaction.getAcpt_ts());
        dto.setAcpt_ts_utc_ofst(transaction.getAcpt_ts_utc_ofst());
        dto.setUUID_reference(transaction.getUUID_reference());
        
        return dto;
    }
    
    public ProductCommunicationDTO toCommunicationDto(PRODUCT_COMMUNICATION communication) {
        if (communication == null) return null;
        ProductCommunicationDTO dto = new ProductCommunicationDTO();
        dto.setCommId(communication.getCommId());
        dto.setCommCode(communication.getCommCode());
        dto.setCommunicationType(communication.getCommunicationType());
        dto.setChannel(communication.getChannel());
        dto.setEvent(communication.getEvent());
        dto.setTemplate(communication.getTemplate());
        dto.setFrequencyLimit(communication.getFrequencyLimit());
        return dto;
    }
    
    // Audit trail version - includes all audit fields
    public ProductCommunicationDTO toCommunicationDtoWithAudit(PRODUCT_COMMUNICATION communication) {
        if (communication == null) return null;
        ProductCommunicationDTO dto = toCommunicationDto(communication);
        
        // Copy audit fields
        dto.setCreatedAt(communication.getCreatedAt());
        dto.setEfctv_date(communication.getEfctv_date());
        dto.setCrud_value(communication.getCrud_value());
        dto.setUser_id(communication.getUser_id());
        dto.setWs_id(communication.getWs_id());
        dto.setPrgm_id(communication.getPrgm_id());
        dto.setHost_ts(communication.getHost_ts());
        dto.setLocal_ts(communication.getLocal_ts());
        dto.setAcpt_ts(communication.getAcpt_ts());
        dto.setAcpt_ts_utc_ofst(communication.getAcpt_ts_utc_ofst());
        dto.setUUID_reference(communication.getUUID_reference());
        
        return dto;
    }
    
    public ProductInterestDTO toInterestDto(PRODUCT_INTEREST interest) {
        if (interest == null) return null;
        ProductInterestDTO dto = new ProductInterestDTO();
        dto.setRateId(interest.getRateId());
        dto.setRateCode(interest.getRateCode());
        dto.setTermInMonths(interest.getTermInMonths());
        dto.setRateCumulative(interest.getRateCumulative());
        dto.setRateNonCumulativeMonthly(interest.getRateNonCumulativeMonthly());
        dto.setRateNonCumulativeQuarterly(interest.getRateNonCumulativeQuarterly());
        dto.setRateNonCumulativeYearly(interest.getRateNonCumulativeYearly());
        return dto;
    }
    
    // Audit trail version - includes all audit fields
    public ProductInterestDTO toInterestDtoWithAudit(PRODUCT_INTEREST interest) {
        if (interest == null) return null;
        ProductInterestDTO dto = toInterestDto(interest);
        
        // Copy audit fields
        dto.setCreatedAt(interest.getCreatedAt());
        dto.setEfctv_date(interest.getEfctv_date());
        dto.setCrud_value(interest.getCrud_value());
        dto.setUser_id(interest.getUser_id());
        dto.setWs_id(interest.getWs_id());
        dto.setPrgm_id(interest.getPrgm_id());
        dto.setHost_ts(interest.getHost_ts());
        dto.setLocal_ts(interest.getLocal_ts());
        dto.setAcpt_ts(interest.getAcpt_ts());
        dto.setAcpt_ts_utc_ofst(interest.getAcpt_ts_utc_ofst());
        dto.setUUID_reference(interest.getUUID_reference());
        
        return dto;
    }
    
    // INSERT-ONLY Pattern: Fill audit fields for CREATE operation
    public <T extends AuditLoggable> T fillAuditFieldsForCreate(T entity) {
        if (entity == null) return null;
        
        // Set timestamps - createdAt serves as version timestamp via @CreationTimestamp
        entity.setHost_ts(new Timestamp(System.currentTimeMillis()));
        entity.setLocal_ts(new Timestamp(System.currentTimeMillis()));
        entity.setAcpt_ts(new Timestamp(System.currentTimeMillis()));
        entity.setAcpt_ts_utc_ofst(new Timestamp(System.currentTimeMillis()));
        
        // Set dummy user info
        entity.setUser_id("SYSTEM");
        entity.setWs_id("WS001");
        entity.setPrgm_id("PGM001");
        
        // INSERT-ONLY Pattern: Set CRUD value to CREATE
        entity.setCrud_value(CRUD_VALUE.C);
        
        // Set UUID reference
        entity.setUUID_reference(UUID.randomUUID());
        
        return entity;
    }
    
    // INSERT-ONLY Pattern: Fill audit fields for UPDATE operation (creates new row)
    public <T extends AuditLoggable> T fillAuditFieldsForUpdate(T entity) {
        if (entity == null) return null;
        
        // Set timestamps - createdAt serves as version timestamp via @CreationTimestamp
        entity.setHost_ts(new Timestamp(System.currentTimeMillis()));
        entity.setLocal_ts(new Timestamp(System.currentTimeMillis()));
        entity.setAcpt_ts(new Timestamp(System.currentTimeMillis()));
        entity.setAcpt_ts_utc_ofst(new Timestamp(System.currentTimeMillis()));
        
        // Set dummy user info
        entity.setUser_id("SYSTEM");
        entity.setWs_id("WS001");
        entity.setPrgm_id("PGM001");
        
        // INSERT-ONLY Pattern: Set CRUD value to UPDATE
        entity.setCrud_value(CRUD_VALUE.U);
        
        // Set UUID reference
        entity.setUUID_reference(UUID.randomUUID());
        
        return entity;
    }
    
    // INSERT-ONLY Pattern: Fill audit fields for DELETE operation (creates new row)
    public <T extends AuditLoggable> T fillAuditFieldsForDelete(T entity) {
        if (entity == null) return null;
        
        // Set timestamps - createdAt serves as version timestamp via @CreationTimestamp
        entity.setHost_ts(new Timestamp(System.currentTimeMillis()));
        entity.setLocal_ts(new Timestamp(System.currentTimeMillis()));
        entity.setAcpt_ts(new Timestamp(System.currentTimeMillis()));
        entity.setAcpt_ts_utc_ofst(new Timestamp(System.currentTimeMillis()));
        
        // Set dummy user info
        entity.setUser_id("SYSTEM");
        entity.setWs_id("WS001");
        entity.setPrgm_id("PGM001");
        
        // INSERT-ONLY Pattern: Set CRUD value to DELETE
        entity.setCrud_value(CRUD_VALUE.D);
        
        // Set UUID reference
        entity.setUUID_reference(UUID.randomUUID());
        
        return entity;
    }
    
    // Legacy method - maintained for backward compatibility (defaults to CREATE)
    public <T extends AuditLoggable> T fillAuditFields(T entity) {
        return fillAuditFieldsForCreate(entity);
    }

    public ProductDetailsDTO toDto(PRODUCT_DETAILS product) {
        if (product == null) return null;
        ProductDetailsDTO dto = new ProductDetailsDTO();
        dto.setProductId(product.getProductId());
        dto.setProductCode(product.getProductCode());
        dto.setProductName(product.getProductName());
        dto.setProductType(product.getProductType());
        dto.setCurrency(product.getCurrency());
        dto.setStatus(product.getStatus());
        dto.setInterestType(product.getInterestType());
        dto.setCompoundingFrequency(product.getCompoundingFrequency());
        
        dto.setCreatedAt(product.getCreatedAt());
        dto.setEfctv_date(product.getEfctv_date());
        
        // INSERT-ONLY Pattern: Fetch only latest versions using repositories
        String productCode = product.getProductCode();
        
        // rules - fetch latest versions by productCode
        List<PRODUCT_RULES> latestRules = rulesRepository.findByProductCode(productCode);
        if (latestRules != null && !latestRules.isEmpty()) {
            dto.setProductRules(latestRules.stream()
                .map(this::toRuleDto)
                .collect(Collectors.toList()));
        }

        // charges - fetch latest versions by productCode
        List<PRODUCT_CHARGES> latestCharges = chargeRepository.findByProductCode(productCode);
        if (latestCharges != null && !latestCharges.isEmpty()) {
            dto.setProductCharges(latestCharges.stream()
                .map(this::toChargeDto)
                .collect(Collectors.toList()));
        }

        // roles - fetch latest versions by productCode
        List<PRODUCT_ROLE> latestRoles = roleRepository.findByProductCode(productCode);
        if (latestRoles != null && !latestRoles.isEmpty()) {
            dto.setProductRoles(latestRoles.stream()
                .map(this::toRoleDto)
                .collect(Collectors.toList()));
        }

        // transactions - fetch latest versions by productCode
        List<PRODUCT_TRANSACTION> latestTransactions = transactionRepository.findByProductCode(productCode);
        if (latestTransactions != null && !latestTransactions.isEmpty()) {
            dto.setProductTransactions(latestTransactions.stream()
                .map(this::toTransactionDto)
                .collect(Collectors.toList()));
        }
        
        // Interests - fetch latest versions by productCode
        List<PRODUCT_INTEREST> latestInterests = interestRepository.findByProductCode(productCode);
        if (latestInterests != null && !latestInterests.isEmpty()) {
            dto.setProductInterests(latestInterests.stream()
                .map(this::toInterestDto)
                .collect(Collectors.toList()));
        }

        // balances - fetch latest versions by productCode
        List<PRODUCT_BALANCE> latestBalances = balanceRepository.findByProductCode(productCode);
        if (latestBalances != null && !latestBalances.isEmpty()) {
            dto.setProductBalances(latestBalances.stream()
                .map(this::toBalanceDto)
                .collect(Collectors.toList()));
        }

        // communications - fetch latest versions by productCode
        List<PRODUCT_COMMUNICATION> latestComms = communicationRepository.findByProductCode(productCode);
        if (latestComms != null && !latestComms.isEmpty()) {
            dto.setProductCommunications(latestComms.stream()
                .map(this::toCommunicationDto)
                .collect(Collectors.toList()));
        }

        return dto;
    }
    
    // Audit trail version - includes all audit fields
    public ProductDetailsDTO toDtoWithAudit(PRODUCT_DETAILS product) {
        if (product == null) return null;
        ProductDetailsDTO dto = toDto(product);
        
        // Copy audit fields
        dto.setCrud_value(product.getCrud_value());
        dto.setUser_id(product.getUser_id());
        dto.setWs_id(product.getWs_id());
        dto.setPrgm_id(product.getPrgm_id());
        dto.setHost_ts(product.getHost_ts());
        dto.setLocal_ts(product.getLocal_ts());
        dto.setAcpt_ts(product.getAcpt_ts());
        dto.setAcpt_ts_utc_ofst(product.getAcpt_ts_utc_ofst());
        dto.setUUID_reference(product.getUUID_reference());
        
        return dto;
    }

    public List<ProductDetailsDTO> toDtoList(List<PRODUCT_DETAILS> products) {
        if (products == null) return new ArrayList<>();
        return products.stream().map(this::toDto).collect(Collectors.toList());
    }

    public PRODUCT_DETAILS toEntity(CreateOrUpdateProductRequestDTO dto) {
        PRODUCT_DETAILS p = new PRODUCT_DETAILS();
        p.setProductCode(dto.getProductCode());
        p.setProductName(dto.getProductName());
        // Note: enum parsing will be handled in service
        return p;
    }

    public void updateEntityFromDto(PRODUCT_DETAILS existingProduct, CreateOrUpdateProductRequestDTO dto) {
        if (dto.getProductName() != null) existingProduct.setProductName(dto.getProductName());
        if (dto.getProductCode() != null) existingProduct.setProductCode(dto.getProductCode());
        // child collections handled in service
    }
}
