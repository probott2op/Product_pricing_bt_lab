You are an expert Spring Boot developer. Your task is to create a complete "Product and Pricing" module for a banking application. Follow the provided structure, entity definitions, and API specifications precisely.

Project Goal

To build a robust and maintainable module that allows a bank to define, create, update, and query financial products like Term Deposits, using a flexible business rule engine.

Design Decision: Product Interest Rates

Product interest rates are managed through a dedicated PRODUCT_INTEREST entity with its own table (interest_rates). This provides a structured approach to storing interest rate matrices for different terms and payout frequencies.

The PRODUCT_INTEREST entity stores:
- termInMonths: The term duration (e.g., 12, 24, 36, 60 months)
- rateCumulative: Base rate for cumulative option (paid at maturity)
- rateNonCumulativeMonthly: Rate for monthly payout
- rateNonCumulativeQuarterly: Rate for quarterly payout
- rateNonCumulativeYearly: Rate for yearly payout

This allows flexible querying and management of interest rates per product.

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

This is the aggregate root. It should have relationships to all sub-entities.

// Inside PRODUCT_DETAILS class

// ... existing fields and relationships for rules and charges ...

@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<PRODUCT_ROLE> productRoles;

@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<PRODUCT_TRANSACTION> productTransactions;

@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<PRODUCT_BALANCE> productBalances;

@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<PRODUCT_COMMUNICATION> productCommunications;

@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<PRODUCT_INTEREST> productInterests;

B. PRODUCT_INTEREST.java

This entity manages interest rates for different terms and payout frequencies. It is stored in the interest_rates table.

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

ProductInterestDTO.java - Response DTO with rateId, termInMonths, and all rate fields

ProductInterestRequestDTO.java - Request DTO with validation annotations

ProductDetailsDTO.java (The Main DTO)
This DTO will be a nested structure representing the entire product definition.

// File: com/lab/product/dto/ProductDetailsDTO.java
@Data
public class ProductDetailsDTO {
    private UUID productId;
    private String productCode;
    private String productName;
    // ... other product detail fields ...
    private List<ProductRuleDTO> productRules;
    private List<ProductChargeDTO> productCharges;
    private List<ProductInterestDTO> productInterests;
    private List<ProductRoleDTO> productRoles;
    private List<ProductTransactionDTO> productTransactions;
    private List<ProductBalanceDTO> productBalances;
    private List<ProductCommunicationDTO> productCommunications;
    // ... audit fields ...
}

CreateOrUpdateProductRequestDTO.java
This will be used as the @RequestBody. It's similar to ProductDetailsDTO but without read-only fields.

4. Repositories (package: com.lab.product.DAO)

Create a JPA repository interface for each entity.

ProductDetailsRepository.java: This is the most important one. It must extend JpaRepository<PRODUCT_DETAILS, UUID> and include methods for searching.

// File: com/lab/product/DAO/ProductDetailsRepository.java
public interface ProductDetailsRepository extends JpaRepository<PRODUCT_DETAILS, UUID> {
    Optional<PRODUCT_DETAILS> findByProductCode(String productCode);
    List<PRODUCT_DETAILS> findByProductType(PRODUCT_TYPE productType);
    List<PRODUCT_DETAILS> findByStatus(PRODUCT_STATUS status);
    
    @Query("SELECT p FROM PRODUCT_DETAILS p WHERE p.efctv_date > :startDate AND p.efctv_date < :endDate")
    List<PRODUCT_DETAILS> findByEfctv_dateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    Page<PRODUCT_DETAILS> findAllProducts(Pageable pageable);
}

ProductInterestRepository.java: Repository for PRODUCT_INTEREST entity

// File: com/lab/product/DAO/ProductInterestRepository.java
public interface ProductInterestRepository extends JpaRepository<PRODUCT_INTEREST, Long> {
    List<PRODUCT_INTEREST> findByProduct(PRODUCT_DETAILS product);
    Optional<PRODUCT_INTEREST> findByProductAndRateId(PRODUCT_DETAILS product, Long rateId);
    boolean existsByProductAndRateId(PRODUCT_DETAILS product, Long rateId);
}

Create repositories for other entities as needed:
- ProductRoleRepository: Uses findByProduct(), findByProductAndRoleId()
- ProductTransactionRepository: Uses findByProduct(), findByProductAndTransactionId()
- ProductBalanceRepository: Uses findByProduct(), findByProductAndBalanceId()
- ProductCommunicationRepository, ProductChargeRepository, ProductRuleRepository

5. Service Helper (/service/helper)

Create a ProductMapper.java class to handle conversions between DTOs and Entities. Include mapping methods for all entities:
- toRoleDto() - Maps PRODUCT_ROLE to ProductRoleDTO
- toTransactionDto() - Maps PRODUCT_TRANSACTION to ProductTransactionDTO
- toBalanceDto() - Maps PRODUCT_BALANCE to ProductBalanceDTO
- toChargeDto() - Maps PRODUCT_CHARGES to ProductChargeDTO
- toInterestDto() - Maps PRODUCT_INTEREST to ProductInterestDTO
- And corresponding reverse mappings

6. Service Layer (/service and /service/impl)

Implement the service logic. All services use String productCode (not UUID) to identify products.

ProductService.java (Interface) - Main product operations

ProductServiceImpl.java (Implementation) - Uses ProductDetailsRepository.findByProductCode()

**Sub-Resource Services:**

ProductRoleService/ProductRoleServiceImpl
- Methods: addRoleToProduct, getRolesForProduct, getRoleById, updateRole, deleteRole
- All methods use String productCode parameter

ProductRuleService/ProductRuleServiceImpl
- Standard CRUD operations using productCode

ProductBalanceService/ProductBalanceServiceImpl
- Uses entity-based queries: findByProduct(), findByProductAndBalanceId()

ProductChargeService/ProductChargeServiceImpl
- Full CRUD operations with productCode

ProductTransactionService/ProductTransactionServiceImpl
- Uses PRODUCT_TRANSACTION_TYPE enum
- Entity-based repository methods

ProductInterestService/ProductInterestServiceImpl (NEW)
- Methods: addInterestToProduct, getInterestRatesForProduct, getInterestRateById, updateInterestRate, deleteInterestRate
- Uses ProductInterestRepository and ProductMapper
- Includes audit logging through AuditLoggable

7. Controller (/controller)

Create ProductController.java for main product operations. All controllers follow nested REST API pattern with String productCode.

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
    @PostMapping("/{productCode}")
    public ResponseEntity<ProductDetailsDTO> updateProduct(@PathVariable String productCode, @RequestBody CreateOrUpdateProductRequestDTO requestDTO) { ... }

    // --- Inquire: Get a single product by its code ---
    @GetMapping("/{productCode}")
    public ResponseEntity<ProductDetailsDTO> getProductByCode(@PathVariable String productCode) { ... }

**Sub-Resource Controllers (Nested REST API):**

ProductRoleController - /api/products/{productCode}/roles
ProductBalanceController - /api/products/{productCode}/balances
ProductChargeController - /api/products/{productCode}/charges
ProductTransactionController - /api/products/{productCode}/transactions
ProductCommunicationController - /api/products/{productCode}/communications
ProductRuleController - /api/products/{productCode}/rules
ProductInterestController - /api/products/{productCode}/interest-rates (NEW)

Each sub-resource controller provides:
- POST /{productCode}/[resource] - Create new sub-resource
- GET /{productCode}/[resource] - List all sub-resources
- GET /{productCode}/[resource]/{id} - Get specific sub-resource
- PUT /{productCode}/[resource]/{id} - Update sub-resource
- DELETE /{productCode}/[resource]/{id} - Delete sub-resource

All controllers use @PathVariable String productCode and include Swagger/OpenAPI documentation.

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

