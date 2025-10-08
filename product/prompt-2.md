You are an expert Spring Boot developer. Your task is to refactor the entire "Product and Pricing" module from a single-endpoint model to a more robust, nested RESTful API structure. The current approach of managing a product and all its sub-components (rules, charges, etc.) in one large request is being replaced.

Project Goal

Refactor the application to support a granular, resource-oriented API where each sub-component of a product (like rules, charges, roles) is managed through its own dedicated set of endpoints nested under the parent product.

New API Structure Specification

Product (Top-Level Resource)

POST /api/products - Create a new product (basic details only).

GET /api/products - Fetch a list of all products.

GET /api/products/{productCode} - Fetch a single product's complete details.

PUT /api/products/{productCode} - Update a product's basic details.

DELETE /api/products/{productCode} - Delete a product.

Product Rules (Sub-Resource)

POST /api/products/{productCode}/rules - Add a new rule to a product.

GET /api/products/{productCode}/rules - Get all rules for a specific product.

GET /api/products/{productCode}/rules/{ruleId} - Get a single rule.

PUT /api/products/{productCode}/rules/{ruleId} - Update a single rule.

DELETE /api/products/{productCode}/rules/{ruleId} - Remove a rule from a product.

Product Charges (Sub-Resource)

POST /api/products/{productCode}/charges

GET /api/products/{productCode}/charges

GET /api/products/{productCode}/charges/{chargeId}

PUT /api/products/{productCode}/charges/{chargeId}

DELETE /api/products/{productCode}/charges/{chargeId}

Product Interest Rates (Sub-Resource)

POST /api/products/{productCode}/interest-rates - Add a new interest rate to a product.

GET /api/products/{productCode}/interest-rates - Get all interest rates for a specific product.

GET /api/products/{productCode}/interest-rates/{rateId} - Get a single interest rate.

PUT /api/products/{productCode}/interest-rates/{rateId} - Update a single interest rate.

DELETE /api/products/{productCode}/interest-rates/{rateId} - Remove an interest rate from a product.

(Apply this nested pattern for ALL other sub-resources: Roles, Transactions, Balances, and Communications).

Refactoring Steps

Step 1: Refactor DTOs (/dto)

The existing CreateOrUpdateProductRequestDTO is too complex. We need to simplify it and create new DTOs for the sub-resources.

Modify CreateOrUpdateProductRequestDTO.java:

This DTO should ONLY contain the fields directly belonging to PRODUCT_DETAILS (e.g., productCode, productName, productType, currency, status).

REMOVE all List<> properties (like productRules, productCharges, etc.). These will now be managed via their own endpoints.

Create New Request DTOs for Sub-Resources:

Create ProductRuleRequestDTO.java: Should contain all fields of a PRODUCT_RULES entity except ruleId and the product object itself.

Create ProductChargeRequestDTO.java: Follow the same pattern.

Create similar request DTOs for Role, Transaction, Balance, and Communication.

Step 2: Split the Service Layer (/service and /service/impl)

The single ProductService needs to be broken down into multiple, focused services.

Modify ProductService.java (Interface):

Keep the core product methods: createProduct, getAllProducts, getProductById, updateProduct, deleteProduct.

Ensure createProduct and updateProduct now accept the simplified CreateOrUpdateProductRequestDTO.

Create New Service Interfaces:

**Note: All services use String productCode (not UUID) for product identification**

ProductRuleService.java:

public interface ProductRuleService {
    ProductRuleDTO addRuleToProduct(String productCode, ProductRuleRequestDTO ruleDto);
    List<ProductRuleDTO> getRulesForProduct(String productCode);
    ProductRuleDTO getRuleById(String productCode, Long ruleId);
    ProductRuleDTO updateRule(String productCode, Long ruleId, ProductRuleRequestDTO ruleDto);
    void deleteRule(String productCode, Long ruleId);
}

ProductInterestService.java:

public interface ProductInterestService {
    ProductInterestDTO addInterestToProduct(String productCode, ProductInterestRequestDTO interestDto);
    List<ProductInterestDTO> getInterestRatesForProduct(String productCode);
    ProductInterestDTO getInterestRateById(String productCode, Long rateId);
    ProductInterestDTO updateInterestRate(String productCode, Long rateId, ProductInterestRequestDTO interestDto);
    void deleteInterestRate(String productCode, Long rateId);
}

Create similar service interfaces for ProductChargeService, ProductRoleService, ProductBalanceService, ProductTransactionService, ProductCommunicationService, following the exact same method pattern with String productCode.

Implement All New Services in the /service/impl package:
- Each implementation injects its own repository (e.g., ProductRuleServiceImpl injects ProductRuleRepository from DAO package)
- Each implementation injects ProductDetailsRepository to fetch the product by productCode
- All implementations use entity-based repository methods like findByProduct(), findByProductAndXxxId()
- ProductInterestServiceImpl includes audit logging through AuditLoggable base class

Step 3: Split the Controller Layer (/controller)

This is the most critical step. Break the single ProductController into multiple controllers, one for each resource.

Refactor ProductController.java:

This controller will only handle the top-level /api/products endpoints.

Remove all methods that were managing nested lists.

**Note: All controllers use String productCode in path variables (not UUID productId)**

// File: com/lab/product/controller/ProductController.java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    // Autowire ProductService
    @PostMapping
    public ResponseEntity<ProductDetailsDTO> createProduct(@RequestBody CreateOrUpdateProductRequestDTO requestDTO) { ... }
    @GetMapping
    public ResponseEntity<List<ProductDetailsDTO>> getAllProducts() { ... }
    @GetMapping("/{productCode}")
    public ResponseEntity<ProductDetailsDTO> getProductByCode(@PathVariable String productCode) { ... }
    @PutMapping("/{productCode}")
    public ResponseEntity<ProductDetailsDTO> updateProduct(@PathVariable String productCode, @RequestBody CreateOrUpdateProductRequestDTO requestDTO) { ... }
    @DeleteMapping("/{productCode}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productCode) { ... }
}

Create ProductRuleController.java:

This controller will manage the nested rule endpoints. Note the @RequestMapping uses {productCode}.

// File: com/lab/product/controller/ProductRuleController.java
@RestController
@RequestMapping("/api/products/{productCode}/rules")
public class ProductRuleController {
    @Autowired
    private ProductRuleService productRuleService;

    @PostMapping
    public ResponseEntity<ProductRuleDTO> addRule(@PathVariable String productCode, @RequestBody ProductRuleRequestDTO ruleDto) {
        return new ResponseEntity<>(productRuleService.addRuleToProduct(productCode, ruleDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductRuleDTO>> getRules(@PathVariable String productCode) {
        return ResponseEntity.ok(productRuleService.getRulesForProduct(productCode));
    }

    @GetMapping("/{ruleId}")
    public ResponseEntity<ProductRuleDTO> getRuleById(@PathVariable String productCode, @PathVariable Long ruleId) {
        return ResponseEntity.ok(productRuleService.getRuleById(productCode, ruleId));
    }

    @PutMapping("/{ruleId}")
    public ResponseEntity<ProductRuleDTO> updateRule(@PathVariable String productCode, @PathVariable Long ruleId, @RequestBody ProductRuleRequestDTO ruleDto) {
        return ResponseEntity.ok(productRuleService.updateRule(productCode, ruleId, ruleDto));
    }

    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> deleteRule(@PathVariable String productCode, @PathVariable Long ruleId) {
        productRuleService.deleteRule(productCode, ruleId);
        return ResponseEntity.noContent().build();
    }
}

Create ProductInterestController.java:

// File: com/lab/product/controller/ProductInterestController.java
@RestController
@RequestMapping("/api/products/{productCode}/interest-rates")
public class ProductInterestController {
    @Autowired
    private ProductInterestService productInterestService;

    @PostMapping
    @Operation(summary = "Add interest rate to product")
    public ResponseEntity<ProductInterestDTO> addInterestRate(@PathVariable String productCode, @Valid @RequestBody ProductInterestRequestDTO requestDTO) {
        return new ResponseEntity<>(productInterestService.addInterestToProduct(productCode, requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all interest rates for product")
    public ResponseEntity<List<ProductInterestDTO>> getInterestRates(@PathVariable String productCode) {
        return ResponseEntity.ok(productInterestService.getInterestRatesForProduct(productCode));
    }

    @GetMapping("/{rateId}")
    @Operation(summary = "Get specific interest rate")
    public ResponseEntity<ProductInterestDTO> getInterestRateById(@PathVariable String productCode, @PathVariable Long rateId) {
        return ResponseEntity.ok(productInterestService.getInterestRateById(productCode, rateId));
    }

    @PutMapping("/{rateId}")
    @Operation(summary = "Update interest rate")
    public ResponseEntity<ProductInterestDTO> updateInterestRate(@PathVariable String productCode, @PathVariable Long rateId, @Valid @RequestBody ProductInterestRequestDTO requestDTO) {
        return ResponseEntity.ok(productInterestService.updateInterestRate(productCode, rateId, requestDTO));
    }

    @DeleteMapping("/{rateId}")
    @Operation(summary = "Delete interest rate")
    public ResponseEntity<Void> deleteInterestRate(@PathVariable String productCode, @PathVariable Long rateId) {
        productInterestService.deleteInterestRate(productCode, rateId);
        return ResponseEntity.noContent().build();
    }
}

Create Additional Controllers:

Create ProductChargeController.java, ProductRoleController.java, ProductBalanceController.java, ProductTransactionController.java, ProductCommunicationController.java, following the exact same pattern with String productCode in @RequestMapping and @PathVariable.

