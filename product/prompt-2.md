You are an expert Spring Boot developer. Your task is to refactor the entire "Product and Pricing" module from a single-endpoint model to a more robust, nested RESTful API structure. The current approach of managing a product and all its sub-components (rules, charges, etc.) in one large request is being replaced.

Project Goal

Refactor the application to support a granular, resource-oriented API where each sub-component of a product (like rules, charges, roles) is managed through its own dedicated set of endpoints nested under the parent product.

New API Structure Specification

Product (Top-Level Resource)

POST /api/products - Create a new product (basic details only).

GET /api/products - Fetch a list of all products.

GET /api/products/{productId} - Fetch a single product's complete details.

PUT /api/products/{productId} - Update a product's basic details.

DELETE /api/products/{productId} - Delete a product.

Product Rules (Sub-Resource)

POST /api/products/{productId}/rules - Add a new rule to a product.

GET /api/products/{productId}/rules - Get all rules for a specific product.

GET /api/products/{productId}/rules/{ruleId} - Get a single rule.

PUT /api/products/{productId}/rules/{ruleId} - Update a single rule.

DELETE /api/products/{productId}/rules/{ruleId} - Remove a rule from a product.

Product Charges (Sub-Resource)

Follow the exact same pattern as Product Rules:

POST /api/products/{productId}/charges

GET /api/products/{productId}/charges

GET /api/products/{productId}/charges/{chargeId}

...and so on.

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

ProductRuleService.java:

public interface ProductRuleService {
    ProductRuleDTO addRuleToProduct(UUID productId, ProductRuleRequestDTO ruleDto);
    List<ProductRuleDTO> getRulesForProduct(UUID productId);
    ProductRuleDTO getRuleById(UUID productId, UUID ruleId);
    ProductRuleDTO updateRule(UUID productId, UUID ruleId, ProductRuleRequestDTO ruleDto);
    void deleteRule(UUID productId, UUID ruleId);
}

Create similar service interfaces for ProductChargeService, ProductRoleService, etc., following the exact same method pattern.

Implement All New Services in the /service/impl package. Each implementation will inject its own repository (e.g., ProductRuleServiceImpl will inject ProductRulesRepository and ProductDetailsRepository).

Step 3: Split the Controller Layer (/controller)

This is the most critical step. Break the single ProductController into multiple controllers, one for each resource.

Refactor ProductController.java:

This controller will only handle the top-level /api/products endpoints.

Remove all methods that were managing nested lists.

// File: com/lab/product/controller/ProductController.java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    // Autowire ProductService
    @PostMapping
    public ResponseEntity<ProductDetailsDTO> createProduct(@RequestBody CreateOrUpdateProductRequestDTO requestDTO) { ... }
    @GetMapping
    public ResponseEntity<List<ProductDetailsDTO>> getAllProducts() { ... }
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailsDTO> getProductById(@PathVariable UUID productId) { ... }
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDetailsDTO> updateProduct(@PathVariable UUID productId, @RequestBody CreateOrUpdateProductRequestDTO requestDTO) { ... }
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) { ... }
}

Create ProductRuleController.java:

This controller will manage the nested rule endpoints. Note the @RequestMapping.

// File: com/lab/product/controller/ProductRuleController.java
@RestController
@RequestMapping("/api/products/{productId}/rules")
public class ProductRuleController {
    @Autowired
    private ProductRuleService productRuleService;

    @PostMapping
    public ResponseEntity<ProductRuleDTO> addRule(@PathVariable UUID productId, @RequestBody ProductRuleRequestDTO ruleDto) {
        return new ResponseEntity<>(productRuleService.addRuleToProduct(productId, ruleDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductRuleDTO>> getRules(@PathVariable UUID productId) {
        return ResponseEntity.ok(productRuleService.getRulesForProduct(productId));
    }

    @GetMapping("/{ruleId}")
    public ResponseEntity<ProductRuleDTO> getRuleById(@PathVariable UUID productId, @PathVariable UUID ruleId) {
        return ResponseEntity.ok(productRuleService.getRuleById(productId, ruleId));
    }

    @PutMapping("/{ruleId}")
    public ResponseEntity<ProductRuleDTO> updateRule(@PathVariable UUID productId, @PathVariable UUID ruleId, @RequestBody ProductRuleRequestDTO ruleDto) {
        return ResponseEntity.ok(productRuleService.updateRule(productId, ruleId, ruleDto));
    }

    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID productId, @PathVariable UUID ruleId) {
        productRuleService.deleteRule(productId, ruleId);
        return ResponseEntity.noContent().build();
    }
}

Create Additional Controllers:

Create ProductChargeController.java, ProductRoleController.java, etc., following the exact same pattern as ProductRuleController.java, each with its own @RequestMapping and injecting its corresponding service.

