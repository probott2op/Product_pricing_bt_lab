package com.lab.product.service.helper;

import com.lab.product.DTO.*;
import com.lab.product.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDetailsDTO toDto(PRODUCT_DETAILS product) {
        if (product == null) return null;
        ProductDetailsDTO dto = new ProductDetailsDTO();
        dto.setProductId(product.getProductId());
        dto.setProductCode(product.getProductCode());
        dto.setProductName(product.getProductName());
        dto.setProductType(product.getProductType());
        dto.setCurrency(product.getCurrency());
        dto.setStatus(product.getStatus());
        // rules
        if (product.getProductRules() != null) {
            dto.setProductRules(product.getProductRules().stream().map(r -> {
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

        // charges
        if (product.getProductCharges() != null) {
            dto.setProductCharges(product.getProductCharges().stream().map(c -> {
                ProductChargeDTO cd = new ProductChargeDTO();
                cd.setChargeId(c.getChargeId());
                cd.setChargeCode(c.getChargeCode());
                cd.setChargeType(c.getChargeType());
                cd.setCalculationType(c.getCalculationType());
                cd.setFrequency(c.getFrequency());
                cd.setAmount(c.getChargeValue());
                return cd;
            }).collect(Collectors.toList()));
        }

        // roles
        if (product.getProductRoles() != null) {
            dto.setProductRoles(product.getProductRoles().stream().map(r -> {
                ProductRoleDTO pr = new ProductRoleDTO();
                pr.setRoleId(r.getRoleId());
                pr.setRoleType(r.getRoleType());
                pr.setRoleName(r.getRoleType() != null ? r.getRoleType().name() : null);
                return pr;
            }).collect(Collectors.toList()));
        }

        // transactions
        if (product.getProductTransactions() != null) {
            dto.setProductTransactions(product.getProductTransactions().stream().map(t -> {
                ProductTransactionDTO pt = new ProductTransactionDTO();
                pt.setTransactionId(t.getId());
                pt.setTransactionType(t.getTransactionType());
                pt.setAmountLimit(null);
                return pt;
            }).collect(Collectors.toList()));
        }

        // balances
        if (product.getProductBalances() != null) {
            dto.setProductBalances(product.getProductBalances().stream().map(b -> {
                ProductBalanceDTO pb = new ProductBalanceDTO();
                pb.setBalanceId(b.getBalanceId());
                pb.setBalanceType(b.getBalanceType());
                pb.setAmount(null);
                return pb;
            }).collect(Collectors.toList()));
        }

        // communications
        if (product.getProductCommunications() != null) {
            dto.setProductCommunications(product.getProductCommunications().stream().map(c -> {
                ProductCommunicationDTO pc = new ProductCommunicationDTO();
                pc.setCommId(c.getCommId());
                pc.setCommunicationType(c.getCommunicationType());
                pc.setChannel(c.getChannel());
                pc.setEvent(c.getEvent());
                pc.setTemplate(c.getTemplate());
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
