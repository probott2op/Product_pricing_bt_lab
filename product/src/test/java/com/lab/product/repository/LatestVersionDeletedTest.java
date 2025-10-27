package com.lab.product.repository;

import com.lab.product.DAO.*;
import com.lab.product.entity.*;
import com.lab.product.entity.ENUMS.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test suite to verify that repository queries return empty/no results
 * when the latest version of an entity has crud_value = 'D' (deleted).
 * 
 * Pattern: Insert-Only versioning with audit trail
 * Requirement: If latest version is deleted, queries should return empty (not the previous version)
 */
@DataJpaTest
@ActiveProfiles("test")
class LatestVersionDeletedTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Autowired
    private ProductChargeRepository productChargeRepository;

    @Autowired
    private ProductRulesRepository productRulesRepository;

    @Autowired
    private ProductRoleRepository productRoleRepository;

    @Autowired
    private ProductTransactionRepository productTransactionRepository;

    @Autowired
    private ProductCommunicationRepository productCommunicationRepository;

    @Autowired
    private ProductInterestRepository productInterestRepository;

    @Autowired
    private ProductBalanceRepository productBalanceRepository;

    @Test
    void testProductDetails_LatestDeleted_ReturnsEmpty() {
        // Create product with CRUD_VALUE = C (created)
        PRODUCT_DETAILS product1 = createProduct("PROD001", "Test Product", CRUD_VALUE.C);
        entityManager.persistAndFlush(product1);
        
        // Update product with CRUD_VALUE = U (updated)
        PRODUCT_DETAILS product2 = createProduct("PROD001", "Test Product Updated", CRUD_VALUE.U);
        entityManager.persistAndFlush(product2);
        
        // Delete product with CRUD_VALUE = D (deleted) - this is now the latest
        PRODUCT_DETAILS product3 = createProduct("PROD001", "Test Product Deleted", CRUD_VALUE.D);
        entityManager.persistAndFlush(product3);
        
        entityManager.clear();

        // When: Query for latest product
        Optional<PRODUCT_DETAILS> result = productDetailsRepository.findLatestByProductCode("PROD001");

        // Then: Should return empty because latest is deleted
        assertThat(result).isEmpty();
    }

    @Test
    void testProductDetails_LatestNotDeleted_ReturnsLatest() {
        // Create product with CRUD_VALUE = C
        PRODUCT_DETAILS product1 = createProduct("PROD002", "Test Product", CRUD_VALUE.C);
        entityManager.persistAndFlush(product1);
        
        // Update product with CRUD_VALUE = U (this is the latest non-deleted)
        PRODUCT_DETAILS product2 = createProduct("PROD002", "Test Product Updated", CRUD_VALUE.U);
        entityManager.persistAndFlush(product2);
        
        entityManager.clear();

        // When: Query for latest product
        Optional<PRODUCT_DETAILS> result = productDetailsRepository.findLatestByProductCode("PROD002");

        // Then: Should return the latest non-deleted version
        assertThat(result).isPresent();
        assertThat(result.get().getProductName()).isEqualTo("Test Product Updated");
        assertThat(result.get().getCrud_value()).isEqualTo(CRUD_VALUE.U);
    }

    @Test
    void testProductCharge_LatestDeleted_ReturnsEmpty() {
        // Setup: Create product first
        PRODUCT_DETAILS product = createProduct("PROD003", "Test Product", CRUD_VALUE.C);
        entityManager.persistAndFlush(product);

        // Create charge with CRUD_VALUE = C
        PRODUCT_CHARGES charge1 = createCharge(product, "CHARGE001", "Test Charge", CRUD_VALUE.C);
        entityManager.persistAndFlush(charge1);
        
        // Update charge with CRUD_VALUE = U
        PRODUCT_CHARGES charge2 = createCharge(product, "CHARGE001", "Test Charge Updated", CRUD_VALUE.U);
        entityManager.persistAndFlush(charge2);
        
        // Delete charge with CRUD_VALUE = D - this is now the latest
        PRODUCT_CHARGES charge3 = createCharge(product, "CHARGE001", "Test Charge Deleted", CRUD_VALUE.D);
        entityManager.persistAndFlush(charge3);
        
        entityManager.clear();

        // When: Query for latest charges
        List<PRODUCT_CHARGES> results = productChargeRepository.findByProductCode("PROD003");

        // Then: Should return empty list because latest is deleted
        assertThat(results).isEmpty();
    }

    @Test
    void testProductRule_LatestDeleted_ReturnsEmpty() {
        // Setup: Create product first
        PRODUCT_DETAILS product = createProduct("PROD004", "Test Product", CRUD_VALUE.C);
        entityManager.persistAndFlush(product);

        // Create rule with CRUD_VALUE = C
        PRODUCT_RULES rule1 = createRule(product, "RULE001", "Test Rule", CRUD_VALUE.C);
        entityManager.persistAndFlush(rule1);
        
        // Delete rule with CRUD_VALUE = D - this is now the latest
        PRODUCT_RULES rule2 = createRule(product, "RULE001", "Test Rule Deleted", CRUD_VALUE.D);
        entityManager.persistAndFlush(rule2);
        
        entityManager.clear();

        // When: Query for latest rules
        List<PRODUCT_RULES> results = productRulesRepository.findByProductCode("PROD004");

        // Then: Should return empty list because latest is deleted
        assertThat(results).isEmpty();
    }

    @Test
    void testProductRole_LatestDeleted_ReturnsEmpty() {
        // Setup: Create product first
        PRODUCT_DETAILS product = createProduct("PROD005", "Test Product", CRUD_VALUE.C);
        entityManager.persistAndFlush(product);

        // Create role with CRUD_VALUE = C
        PRODUCT_ROLE role1 = createRole(product, "ROLE001", CRUD_VALUE.C);
        entityManager.persistAndFlush(role1);
        
        // Delete role with CRUD_VALUE = D - this is now the latest
        PRODUCT_ROLE role2 = createRole(product, "ROLE001", CRUD_VALUE.D);
        entityManager.persistAndFlush(role2);
        
        entityManager.clear();

        // When: Query for latest roles
        List<PRODUCT_ROLE> results = productRoleRepository.findByProductCode("PROD005");

        // Then: Should return empty list because latest is deleted
        assertThat(results).isEmpty();
    }

    @Test
    void testProductTransaction_LatestDeleted_ReturnsEmpty() {
        // Setup: Create product first
        PRODUCT_DETAILS product = createProduct("PROD006", "Test Product", CRUD_VALUE.C);
        entityManager.persistAndFlush(product);

        // Create transaction with CRUD_VALUE = C
        PRODUCT_TRANSACTION txn1 = createTransaction(product, "TXN001", CRUD_VALUE.C);
        entityManager.persistAndFlush(txn1);
        
        // Delete transaction with CRUD_VALUE = D - this is now the latest
        PRODUCT_TRANSACTION txn2 = createTransaction(product, "TXN001", CRUD_VALUE.D);
        entityManager.persistAndFlush(txn2);
        
        entityManager.clear();

        // When: Query for latest transactions
        List<PRODUCT_TRANSACTION> results = productTransactionRepository.findByProductCode("PROD006");

        // Then: Should return empty list because latest is deleted
        assertThat(results).isEmpty();
    }

    @Test
    void testProductCommunication_LatestDeleted_ReturnsEmpty() {
        // Setup: Create product first
        PRODUCT_DETAILS product = createProduct("PROD007", "Test Product", CRUD_VALUE.C);
        entityManager.persistAndFlush(product);

        // Create communication with CRUD_VALUE = C
        PRODUCT_COMMUNICATION comm1 = createCommunication(product, "COMM001", CRUD_VALUE.C);
        entityManager.persistAndFlush(comm1);
        
        // Delete communication with CRUD_VALUE = D - this is now the latest
        PRODUCT_COMMUNICATION comm2 = createCommunication(product, "COMM001", CRUD_VALUE.D);
        entityManager.persistAndFlush(comm2);
        
        entityManager.clear();

        // When: Query for latest communications
        List<PRODUCT_COMMUNICATION> results = productCommunicationRepository.findByProductCode("PROD007");

        // Then: Should return empty list because latest is deleted
        assertThat(results).isEmpty();
    }

    @Test
    void testProductInterest_LatestDeleted_ReturnsEmpty() {
        // Setup: Create product first
        PRODUCT_DETAILS product = createProduct("PROD008", "Test Product", CRUD_VALUE.C);
        entityManager.persistAndFlush(product);

        // Create interest with CRUD_VALUE = C
        PRODUCT_INTEREST interest1 = createInterest(product, "RATE001", CRUD_VALUE.C);
        entityManager.persistAndFlush(interest1);
        
        // Delete interest with CRUD_VALUE = D - this is now the latest
        PRODUCT_INTEREST interest2 = createInterest(product, "RATE001", CRUD_VALUE.D);
        entityManager.persistAndFlush(interest2);
        
        entityManager.clear();

        // When: Query for latest interest rates
        List<PRODUCT_INTEREST> results = productInterestRepository.findByProductCode("PROD008");

        // Then: Should return empty list because latest is deleted
        assertThat(results).isEmpty();
    }

    @Test
    void testProductBalance_LatestDeleted_ReturnsEmpty() {
        // Setup: Create product first
        PRODUCT_DETAILS product = createProduct("PROD009", "Test Product", CRUD_VALUE.C);
        entityManager.persistAndFlush(product);

        // Create balance with CRUD_VALUE = C
        PRODUCT_BALANCE balance1 = createBalance(product, PRODUCT_BALANCE_TYPE.FD_PRINCIPAL, CRUD_VALUE.C);
        entityManager.persistAndFlush(balance1);
        
        // Delete balance with CRUD_VALUE = D - this is now the latest
        PRODUCT_BALANCE balance2 = createBalance(product, PRODUCT_BALANCE_TYPE.FD_PRINCIPAL, CRUD_VALUE.D);
        entityManager.persistAndFlush(balance2);
        
        entityManager.clear();

        // When: Query for latest balances
        List<PRODUCT_BALANCE> results = productBalanceRepository.findByProductCode("PROD009");

        // Then: Should return empty list because latest is deleted
        assertThat(results).isEmpty();
    }

    // Helper methods to create test entities

    private PRODUCT_DETAILS createProduct(String code, String name, CRUD_VALUE crudValue) {
        PRODUCT_DETAILS product = new PRODUCT_DETAILS();
        product.setProductCode(code);
        product.setProductName(name);
        product.setProductType(PRODUCT_TYPE.FIXED_DEPOSIT);
        product.setStatus(PRODUCT_STATUS.ACTIVE);
        product.setCurrency(PRODUCT_CURRENCY.INR);
        product.setCrud_value(crudValue);
        product.setEfctv_date(Date.valueOf(LocalDate.now()));
        product.setUser_id("1001");
        product.setWs_id("1");
        product.setPrgm_id("1");
        return product;
    }

    private PRODUCT_CHARGES createCharge(PRODUCT_DETAILS product, String chargeCode, String chargeName, CRUD_VALUE crudValue) {
        PRODUCT_CHARGES charge = new PRODUCT_CHARGES();
        charge.setProduct(product);
        charge.setProductCode(product.getProductCode());
        charge.setChargeCode(chargeCode);
        charge.setChargeName(chargeName);
        charge.setChargeType(PRODUCT_CHARGE_TYPE.PENALTY);
        charge.setCalculationType(PRODUCT_CHARGE_CALCULATION_TYPE.FLAT);
        charge.setChargeValue(new BigDecimal("100.00"));
        charge.setDebitCredit(PRODUCT_DebitCredit.DEBIT);
        charge.setCrud_value(crudValue);
        charge.setEfctv_date(Date.valueOf(LocalDate.now()));
        charge.setUser_id("1001");
        charge.setWs_id("1");
        charge.setPrgm_id("1");
        return charge;
    }

    private PRODUCT_RULES createRule(PRODUCT_DETAILS product, String ruleCode, String ruleName, CRUD_VALUE crudValue) {
        PRODUCT_RULES rule = new PRODUCT_RULES();
        rule.setProduct(product);
        rule.setProductCode(product.getProductCode());
        rule.setRuleCode(ruleCode);
        rule.setRuleName(ruleName);
        rule.setRuleType(PRODUCT_RULE_TYPE.SIMPLE);
        rule.setDataType(PRODUCT_RULE_DATA.TEXT);
        rule.setRuleValue("18");
        rule.setValidationType(PRODUCT_RULE_VALIDATION.EXACT);
        rule.setCrud_value(crudValue);
        rule.setEfctv_date(Date.valueOf(LocalDate.now()));
        rule.setUser_id("1001");
        rule.setWs_id("1");
        rule.setPrgm_id("1");
        return rule;
    }

    private PRODUCT_ROLE createRole(PRODUCT_DETAILS product, String roleCode, CRUD_VALUE crudValue) {
        PRODUCT_ROLE role = new PRODUCT_ROLE();
        role.setProduct(product);
        role.setProductCode(product.getProductCode());
        role.setRoleCode(roleCode);
        role.setRoleType(PRODUCT_ROLE_TYPE.OWNER);
        role.setMandatory(true);
        role.setMaxCount(1);
        role.setCrud_value(crudValue);
        role.setEfctv_date(Date.valueOf(LocalDate.now()));
        role.setUser_id("1001");
        role.setWs_id("1");
        role.setPrgm_id("1");
        return role;
    }

    private PRODUCT_TRANSACTION createTransaction(PRODUCT_DETAILS product, String txnCode, CRUD_VALUE crudValue) {
        PRODUCT_TRANSACTION txn = new PRODUCT_TRANSACTION();
        txn.setProduct(product);
        txn.setProductCode(product.getProductCode());
        txn.setTransactionCode(txnCode);
        txn.setTransactionType(PRODUCT_TRANSACTION_TYPE.DEPOSIT);
        txn.setAllowed(true);
        txn.setCrud_value(crudValue);
        txn.setEfctv_date(Date.valueOf(LocalDate.now()));
        txn.setUser_id("1001");
        txn.setWs_id("1");
        txn.setPrgm_id("1");
        return txn;
    }

    private PRODUCT_COMMUNICATION createCommunication(PRODUCT_DETAILS product, String commCode, CRUD_VALUE crudValue) {
        PRODUCT_COMMUNICATION comm = new PRODUCT_COMMUNICATION();
        comm.setProduct(product);
        comm.setProductCode(product.getProductCode());
        comm.setCommCode(commCode);
        comm.setCommunicationType(PRODUCT_COMM_TYPE.STATEMENT);
        comm.setChannel(PRODUCT_COMM_CHANNEL.EMAIL);
        comm.setEvent("ACCOUNT_OPENING");
        comm.setTemplate("email-template");
        comm.setFrequencyLimit(10);
        comm.setCrud_value(crudValue);
        comm.setEfctv_date(Date.valueOf(LocalDate.now()));
        comm.setUser_id("1001");
        comm.setWs_id("1");
        comm.setPrgm_id("1");
        return comm;
    }

    private PRODUCT_INTEREST createInterest(PRODUCT_DETAILS product, String rateCode, CRUD_VALUE crudValue) {
        PRODUCT_INTEREST interest = new PRODUCT_INTEREST();
        interest.setProduct(product);
        interest.setProductCode(product.getProductCode());
        interest.setRateCode(rateCode);
        interest.setRateCumulative(new BigDecimal("5.50"));
        interest.setCrud_value(crudValue);
        interest.setEfctv_date(Date.valueOf(LocalDate.now()));
        interest.setUser_id("1001");
        interest.setWs_id("1");
        interest.setPrgm_id("1");
        return interest;
    }

    private PRODUCT_BALANCE createBalance(PRODUCT_DETAILS product, PRODUCT_BALANCE_TYPE balanceType, CRUD_VALUE crudValue) {
        PRODUCT_BALANCE balance = new PRODUCT_BALANCE();
        balance.setProduct(product);
        balance.setProductCode(product.getProductCode());
        balance.setBalanceType(balanceType);
        balance.setIsActive(true);
        balance.setCrud_value(crudValue);
        balance.setEfctv_date(Date.valueOf(LocalDate.now()));
        balance.setUser_id("1001");
        balance.setWs_id("1");
        balance.setPrgm_id("1");
        return balance;
    }
}
