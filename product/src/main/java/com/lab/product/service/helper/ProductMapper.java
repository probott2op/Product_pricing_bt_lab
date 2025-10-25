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
    
    public ProductTransactionDTO toTransactionDto(PRODUCT_TRANSACTION transaction) {
        if (transaction == null) return null;
        ProductTransactionDTO dto = new ProductTransactionDTO();
        dto.setTransactionId(transaction.getId());
        dto.setTransactionCode(transaction.getTransactionCode());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setAllowed(transaction.isAllowed());
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
        
        // INSERT-ONLY Pattern: Fetch only latest versions using repositories
        String productCode = product.getProductCode();
        
        // rules - fetch latest versions by productCode
        List<PRODUCT_RULES> latestRules = rulesRepository.findByProductCode(productCode);
        if (latestRules != null && !latestRules.isEmpty()) {
            dto.setProductRules(latestRules.stream().map(r -> {
                ProductRuleDTO rd = new ProductRuleDTO();
                rd.setRuleId(r.getRuleId());
                rd.setRuleCode(r.getRuleCode());
                rd.setRuleName(r.getRuleName());
                rd.setRuleType(r.getRuleType());
                rd.setDataType(r.getDataType());
                rd.setRuleValue(r.getRuleValue());
                rd.setValidationType(r.getValidationType());
                return rd;
            }).collect(Collectors.toList()));
        }

        // charges - fetch latest versions by productCode
        List<PRODUCT_CHARGES> latestCharges = chargeRepository.findByProductCode(productCode);
        if (latestCharges != null && !latestCharges.isEmpty()) {
            dto.setProductCharges(latestCharges.stream().map(c -> {
                ProductChargeDTO cd = new ProductChargeDTO();
                cd.setChargeId(c.getChargeId());
                cd.setChargeCode(c.getChargeCode());
                cd.setChargeName(c.getChargeName());
                cd.setChargeType(c.getChargeType());
                cd.setCalculationType(c.getCalculationType());
                cd.setFrequency(c.getFrequency());
                cd.setAmount(c.getChargeValue());
                cd.setDebitCredit(c.getDebitCredit());
                return cd;
            }).collect(Collectors.toList()));
        }

        // roles - fetch latest versions by productCode
        List<PRODUCT_ROLE> latestRoles = roleRepository.findByProductCode(productCode);
        if (latestRoles != null && !latestRoles.isEmpty()) {
            dto.setProductRoles(latestRoles.stream().map(r -> {
                ProductRoleDTO pr = new ProductRoleDTO();
                pr.setRoleId(r.getRoleId());
                pr.setRoleCode(r.getRoleCode());
                pr.setRoleType(r.getRoleType());
                pr.setRoleName(r.getRoleType() != null ? r.getRoleType().name() : null);
                pr.setMandatory(r.isMandatory());
                pr.setMaxCount(r.getMaxCount());
                return pr;
            }).collect(Collectors.toList()));
        }

        // transactions - fetch latest versions by productCode
        List<PRODUCT_TRANSACTION> latestTransactions = transactionRepository.findByProductCode(productCode);
        if (latestTransactions != null && !latestTransactions.isEmpty()) {
            dto.setProductTransactions(latestTransactions.stream().map(t -> {
                ProductTransactionDTO pt = new ProductTransactionDTO();
                pt.setTransactionId(t.getId());
                pt.setTransactionCode(t.getTransactionCode());
                pt.setTransactionType(t.getTransactionType());
                pt.setAllowed(t.isAllowed());
                return pt;
            }).collect(Collectors.toList()));
        }
        
        // Interests - fetch latest versions by productCode
        List<PRODUCT_INTEREST> latestInterests = interestRepository.findByProductCode(productCode);
        if (latestInterests != null && !latestInterests.isEmpty()) {
            dto.setProductInterests(latestInterests.stream().map(t -> {
                ProductInterestDTO pi = new ProductInterestDTO();
                pi.setRateId(t.getRateId());
                pi.setRateCode(t.getRateCode());
                pi.setRateCumulative(t.getRateCumulative());
                pi.setTermInMonths(t.getTermInMonths());
                pi.setRateNonCumulativeMonthly(t.getRateNonCumulativeMonthly());
                pi.setRateNonCumulativeQuarterly(t.getRateNonCumulativeQuarterly());
                pi.setRateNonCumulativeYearly(t.getRateNonCumulativeYearly());
                return pi;
            }).collect(Collectors.toList()));
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
            dto.setProductCommunications(latestComms.stream().map(c -> {
                ProductCommunicationDTO pc = new ProductCommunicationDTO();
                pc.setCommId(c.getCommId());
                pc.setCommCode(c.getCommCode());
                pc.setCommunicationType(c.getCommunicationType());
                pc.setChannel(c.getChannel());
                pc.setEvent(c.getEvent());
                pc.setTemplate(c.getTemplate());
                pc.setFrequencyLimit(c.getFrequencyLimit());
                return pc;
            }).collect(Collectors.toList()));
        }

        dto.setCreatedAt(product.getCreatedAt());
        dto.setEfctv_date(product.getEfctv_date());
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
