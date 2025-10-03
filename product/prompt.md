You are an expert Spring Boot developer. Your task is to create a complete "Product and Pricing" module for a banking application. Follow the provided structure, entity definitions, and API specifications precisely.

Project Goal

To build a robust and maintainable module that allows a bank to define, create, update, and query financial products like Term Deposits, using a flexible business rule engine.

Design Change: Interest as a Business Rule

Instead of a separate PRODUCT_INTEREST table, we will model the interest rate matrix as a "Complex Business Rule" within the PRODUCT_RULES entity. This provides greater flexibility. The ruleValue field of a PRODUCT_RULES record will store a JSON string when the rule represents an interest matrix.

Example JSON for an Interest Rate Rule:
When creating a rule with ruleCode = INTEREST_RATE_MATRIX, the ruleValue field should contain a JSON string like this:

{
  "rateMatrix": [
    {
      "minAmount": 0,
      "maxAmount": 100000,
      "minTermDays": 90,
      "maxTermDays": 180,
      "customerCategory": "RETAIL",
      "interestRate": 0.0550
    },
    {
      "minAmount": 0,
      "maxAmount": 100000,
      "minTermDays": 181,
      "maxTermDays": 365,
      "customerCategory": "RETAIL",
      "interestRate": 0.0600
    },
    {
      "minAmount": 0,
      "maxAmount": 100000,
      "minTermDays": 90,
      "maxTermDays": 365,
      "customerCategory": "SENIOR_CITIZEN",
      "interestRate": 0.0650
    }
  ]
}

1. Project Structure

Create the following package structure within the com.lab.product base package:

com.lab.product.controller

com.lab.product.dto

com.lab.product.entity (Use the provided entity classes)

com.lab.product.entity.ENUMS

com.lab.product.DAO

com.lab.product.service

com.lab.product.service.impl

com.lab.product.service.helper

com.lab.product.exception

2. Entities (/entity)

You have been provided with the core entity classes. Your first task is to refine and complete them based on the new design.

A. Update PRODUCT_DETAILS.java

This is the aggregate root. Remove the relationship to PRODUCT_INTEREST and add the relationship for the new PRODUCT_COMMUNICATION entity.

// Inside PRODUCT_DETAILS class

// ... existing fields and relationships for rules and charges ...

// REMOVE: @OneToMany for PRODUCT_INTEREST

@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<PRODUCT_ROLE> productRoles;

@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<PRODUCT_TRANSACTION> productTransactions;

@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<PRODUCT_BALANCE> productBalances;

// Add this new relationship for the new entity below
@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<PRODUCT_COMMUNICATION> productCommunications;

B. REMOVE PRODUCT_INTEREST.java

This entity is no longer needed and should be deleted.

C. Create New Entity PRODUCT_COMMUNICATION.java

The requirements document mentions "Customer Communications (Alerts, Notices, Statements)". Create a new entity for this.

// File: com/lab/product/entity/PRODUCT_COMMUNICATION.java
package com.lab.product.entity;

import com.lab.product.entity.ENUMS.PRODUCT_COMM_TYPE;
import com.lab.product.entity.ENUMS.PRODUCT_COMM_CHANNEL;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "product_communications")
@Data
public class PRODUCT_COMMUNICATION extends AuditLoggable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID commId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PRODUCT_DETAILS product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PRODUCT_COMM_TYPE communicationType; // e.g., ALERT, NOTICE, STATEMENT

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PRODUCT_COMM_CHANNEL channel; // e.g., EMAIL, SMS, POST

    @Column(nullable = false)
    private String event; // e.g., "ACCOUNT_OPENING", "TRANSACTION_COMPLETE"

    @Column(columnDefinition = "TEXT")
    private String template; // The message template
}

D. Create All ENUMs (/entity/ENUMS)

Create all necessary enum types based on the entity definitions and the requirements document. Ensure they are in the com.lab.product.entity.ENUMS package. Create files for each enum.

CRUD_VALUE.java: (CREATE, READ, UPDATE, DELETE)

PRODUCT_TYPE.java: (SAVINGS, FIXED_DEPOSIT, LOAN)

PRODUCT_STATUS.java: (ACTIVE, INACTIVE, DRAFT)

PRODUCT_CURRENCY.java: (INR, USD, EUR, GBP)

PRODUCT_RULE_TYPE.java: (SIMPLE, COMPLEX)

PRODUCT_RULE_DATA.java: (DATE, NUMBER, TEXT, PERCENTAGE, CURRENCY, JSON_MATRIX)

PRODUCT_RULE_VALIDATION.java: (MIN_VALUE, MAX_VALUE, REGEX)

PRODUCT_CHARGE_TYPE.java: (FEE, TAX, INTEREST)

PRODUCT_CHARGE_CALCULATION_TYPE.java: (FIXED_AMOUNT, PERCENTAGE)

PRODUCT_CHARGE_FREQUENCY.java: (ONCE, MONTHLY, QUARTERLY, ANNUALLY)

PRODUCT_DebitCredit.java: (DEBIT, CREDIT)

PRODUCT_CUSTOMERCAT.java: (RETAIL, CORPORATE, SENIOR_CITIZEN)

PRODUCT_ROLE_TYPE.java: (OWNER, CO_OWNER, GUARDIAN, NOMINEE, BORROWER, GUARANTOR)

PRODUCT_TRANSACTION_TYPE.java: (DEPOSIT, WITHDRAWAL, DISBURSEMENT, PAYMENT)

PRODUCT_BALANCE_TYPE.java: (LOAN_PRINCIPAL, LOAN_INTEREST, FD_PRINCIPAL, FD_INTEREST)

PRODUCT_COMM_TYPE.java: (ALERT, NOTICE, STATEMENT)

PRODUCT_COMM_CHANNEL.java: (EMAIL, SMS, POST)

3. DTOs (/dto)

Do not expose entities in the API. Create a comprehensive set of DTOs.

ProductRuleDTO.java

ProductChargeDTO.java

ProductRoleDTO.java

ProductTransactionDTO.java

ProductBalanceDTO.java

ProductCommunicationDTO.java

ProductDetailsDTO.java (The Main DTO)
This DTO will be a nested structure representing the entire product definition. Remove productInterests.

// File: com/lab/product/dto/ProductDetailsDTO.java
@Data
public class ProductDetailsDTO {
    private UUID productId;
    private String productCode;
    private String productName;
    // ... other product detail fields ...
    private List<ProductRuleDTO> productRules;
    private List<ProductChargeDTO> productCharges;
    // REMOVE: private List<ProductInterestDTO> productInterests;
    private List<ProductRoleDTO> productRoles;
    private List<ProductTransactionDTO> productTransactions;
    private List<ProductBalanceDTO> productBalances;
    private List<ProductCommunicationDTO> productCommunications;
    // ... audit fields ...
}

CreateOrUpdateProductRequestDTO.java
This will be used as the @RequestBody. It's similar to ProductDetailsDTO but without read-only fields. Remove productInterests.

4. Repositories (/repository)

Create a JPA repository interface for each entity.

ProductDetailsRepository.java: This is the most important one. It must extend JpaRepository<PRODUCT_DETAILS, UUID> and include methods for searching.

// File: com/lab/product/repository/ProductDetailsRepository.java
public interface ProductDetailsRepository extends JpaRepository<PRODUCT_DETAILS, UUID> {
    Optional<PRODUCT_DETAILS> findByProductCode(String productCode);
    List<PRODUCT_DETAILS> findByProductType(PRODUCT_TYPE productType);
    List<PRODUCT_DETAILS> findByStatus(PRODUCT_STATUS status);
    List<PRODUCT_DETAILS> findByEfctv_dateBetween(Date startDate, Date endDate);
}

REMOVE ProductInterestRepository.java.

Create repositories for other entities as needed (e.g., ProductChargesRepository, ProductRulesRepository, etc.).

5. Service Helper (/service/helper)

Create a ProductMapper.java class to handle conversions between DTOs and Entities. Remove all logic related to PRODUCT_INTEREST.

6. Service Layer (/service and /service/impl)

Implement the service logic. The service will need to be aware of how to handle the JSON-based interest rule. You might need a JSON processing library like Jackson or Gson, which Spring Boot includes by default.

ProductService.java (Interface) - No changes needed here.

ProductServiceImpl.java (Implementation)

In createProduct and updateProduct, when mapping ProductRuleDTO to PRODUCT_RULES, check if the rule is for an interest matrix. If so, validate the JSON structure of ruleValue before saving.

7. Controller (/controller)

Create ProductController.java following the conventions from the provided BankingController.java. The API endpoints remain the same.

// File: com/lab/product/controller/ProductController.java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // --- Create a new Product ---
    @PutMapping("/")
    public ResponseEntity<ProductDetailsDTO> createProduct(@RequestBody CreateOrUpdateProductRequestDTO requestDTO) { ... }

    // --- Update/Maintain an existing Product ---
    @PostMapping("/{productId}")
    public ResponseEntity<ProductDetailsDTO> updateProduct(@PathVariable UUID productId, @RequestBody CreateOrUpdateProductRequestDTO requestDTO) { ... }

    // --- Inquire: Get a single product by its ID ---
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailsDTO> getProductById(@PathVariable UUID productId) { ... }

    // --- Inquire: Get a single product by its unique code ---
    @GetMapping("/byCode/{productCode}")
    public ResponseEntity<ProductDetailsDTO> getProductByCode(@PathVariable String productCode) { ... }

    // --- Get list of products based on search criteria ---
    @GetMapping("/search")
    public ResponseEntity<List<ProductDetailsDTO>> searchProducts(
        @RequestParam(required = false) String productType,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) { ... }
}

8. Exception Handling (/exception)

Create a custom exception ResourceNotFoundException.java and a global exception handler GlobalExceptionHandler.java using @ControllerAdvice to handle cases where a product is not found and return a proper 404 response.

