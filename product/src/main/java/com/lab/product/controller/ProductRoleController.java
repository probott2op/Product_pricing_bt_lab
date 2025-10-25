package com.lab.product.controller;

import com.lab.product.DTO.ProductRoleDTO;
import com.lab.product.DTO.ProductRoleRequestDTO;
import com.lab.product.service.ProductRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productCode}/roles")
@RequiredArgsConstructor
@Tag(
    name = "Product Roles & Permissions",
    description = "An API for managing which user roles can access a specific financial product."+
            " It establishes a simple mapping between a product and one or more roles (e.g., 'OWNER', 'CO_OWNER',"+
            " 'GUARDIAN'), effectively creating an access list. This controls product visibility and"+
            " eligibility at a high level."
)
public class ProductRoleController {

    @Autowired
    private final ProductRoleService productRoleService;

    @PostMapping
    @Operation(
        summary = "Define user role permissions for product access",
        description = """
            Configure which user roles can access and interact with a specific financial product.
            
            **Role Types Commonly Used:**
            - **PRIMARY_OWNER** - Primary account holder with full control
            - **CO_OWNER** - Joint account holder with equal rights
            - **AUTHORIZED_USER** - Can transact but not close account
            - **VIEW_ONLY** - Read-only access to account
            - **GUARDIAN** - For minor accounts (parent/guardian)
            - **BENEFICIARY** - Receives funds under specific conditions
            - **POWER_OF_ATTORNEY** - Legal representative
            - **TRUSTEE** - Trust account manager
            
            **Permissions Typically Associated:**
            - **Full Access**: View, transact, modify, close
            - **Transaction Rights**: View, deposit, withdraw, transfer
            - **View Rights**: View balance and statements only
            - **Administrative**: Product configuration, user management
            
            **Use Cases:**
            
            **Scenario 1: Joint Savings Account**
            Two account holders with equal rights:
            - PRIMARY_OWNER (spouse 1)
            - CO_OWNER (spouse 2)
            - Both can transact independently
            - Either can close account
            
            **Scenario 2: Business Current Account**
            Multiple authorized signatories:
            - PRIMARY_OWNER (business owner)
            - AUTHORIZED_USER (finance manager)
            - AUTHORIZED_USER (operations manager)
            - VIEW_ONLY (accountant)
            
            **Scenario 3: Minor's Savings Account**
            Guardian manages until child is 18:
            - GUARDIAN (parent) - full access
            - PRIMARY_OWNER (child) - future owner
            - Auto-transfer control at age 18
            
            **Scenario 4: Trust Fixed Deposit**
            Professional management:
            - TRUSTEE (trust company)
            - BENEFICIARY (trust beneficiary)
            - Limited actions per trust deed
            
            **Role Configuration Includes:**
            - Role name and type
            - Permission level
            - Access restrictions
            - Transaction limits (if applicable)
            - Duration/expiry (optional)
            
            **Related Endpoints:**
            - GET /api/products/{code}/roles - View all role configurations
            - PUT /api/products/{code}/roles/{id} - Update role permissions
            - DELETE /api/products/{code}/roles/{id} - Remove role access
            """,
        tags = {"Product Roles & Permissions"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Role configured successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductRoleDTO.class),
                examples = {
                    @ExampleObject(
                        name = "FD001 Owner Role",
                        summary = "Fixed Deposit owner with full control",
                        value = """
                            {
                              "roleId": "f1097ab9-789c-4bf2-984a-eaa6d42b1fed",
                              "roleCode": "ROLE001",
                              "roleType": "OWNER",
                              "roleName": "OWNER",
                              "permissions": ["VIEW", "TRANSACT", "MODIFY", "CLOSE"],
                              "transactionLimit": null,
                              "requiresApproval": false,
                              "isActive": true,
                              "createdAt": "2025-10-18T11:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Authorized User Role",
                        summary = "Transaction rights with limits",
                        value = """
                            {
                              "roleId": "role-992e8400-e29b-41d4-a716-446655440002",
                              "roleName": "AUTHORIZED_USER",
                              "roleDescription": "Can perform transactions with daily limits",
                              "permissions": ["VIEW", "TRANSACT"],
                              "transactionLimit": 10000.00,
                              "requiresApproval": false,
                              "isActive": true,
                              "createdAt": "2025-10-18T11:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "View Only Role",
                        summary = "Read-only access for monitoring",
                        value = """
                            {
                              "roleId": "role-993e8400-e29b-41d4-a716-446655440003",
                              "roleName": "VIEW_ONLY",
                              "roleDescription": "Read-only access to account information",
                              "permissions": ["VIEW"],
                              "transactionLimit": 0.00,
                              "requiresApproval": false,
                              "isActive": true,
                              "createdAt": "2025-10-18T11:00:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Product Not Found",
                    value = """
                        {
                          "error": "Resource Not Found",
                          "message": "Product not found with code: SAV-INVALID-2025",
                          "timestamp": "2025-10-18T11:00:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid role configuration",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Missing Required Field",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Role name is required",
                              "timestamp": "2025-10-18T11:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Duplicate Role",
                        value = """
                            {
                              "error": "Duplicate Entry",
                              "message": "Role 'PRIMARY_OWNER' already exists for this product",
                              "timestamp": "2025-10-18T11:00:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ProductRoleDTO> addRole(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = """
                    Role configuration details.
                    
                    Required fields:
                    - roleName (PRIMARY_OWNER, CO_OWNER, etc.)
                    - roleDescription
                    - permissions (array)
                    
                    Optional:
                    - transactionLimit (null = unlimited)
                    - requiresApproval (default false)
                    - isActive (default true)
                    """,
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductRoleRequestDTO.class),
                    examples = {
                        @ExampleObject(
                            name = "Primary Owner",
                            summary = "Full account control",
                            value = """
                                {
                                  "roleName": "PRIMARY_OWNER",
                                  "roleDescription": "Primary account holder with full control",
                                  "permissions": ["VIEW", "TRANSACT", "MODIFY", "CLOSE"],
                                  "transactionLimit": null,
                                  "requiresApproval": false,
                                  "isActive": true
                                }
                                """
                        ),
                        @ExampleObject(
                            name = "Authorized User",
                            summary = "Limited transaction rights",
                            value = """
                                {
                                  "roleName": "AUTHORIZED_USER",
                                  "roleDescription": "Can perform transactions with daily limits",
                                  "permissions": ["VIEW", "TRANSACT"],
                                  "transactionLimit": 10000.00,
                                  "requiresApproval": false,
                                  "isActive": true
                                }
                                """
                        ),
                        @ExampleObject(
                            name = "View Only",
                            summary = "Read-only access",
                            value = """
                                {
                                  "roleName": "VIEW_ONLY",
                                  "roleDescription": "Read-only access to account information",
                                  "permissions": ["VIEW"],
                                  "transactionLimit": 0.00,
                                  "requiresApproval": false,
                                  "isActive": true
                                }
                                """
                        )
                    }
                )
            )
            @Valid @RequestBody ProductRoleRequestDTO roleDto) {
        return new ResponseEntity<>(productRoleService.addRoleToProduct(productCode, roleDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "Retrieve all role configurations for product",
        description = """
            Get a paginated list of all user roles configured for a specific product with their permissions.
            
            **What You Get:**
            Complete role and permission structure:
            - All defined roles for the product
            - Permission levels per role
            - Transaction limits (if applicable)
            - Approval requirements
            - Active/inactive status
            
            **Use Cases:**
            
            **Scenario 1: Product Access Control Review**
            Audit who can access this product:
            - List all authorized roles
            - Review permission levels
            - Verify transaction limits
            - Check for unauthorized access
            
            **Scenario 2: Customer Onboarding**
            Explain account ownership options:
            - Show available role types
            - Explain permissions per role
            - Help customer choose structure
            - Set up joint accounts
            
            **Scenario 3: Compliance Verification**
            Regulatory audit requires:
            - Document all access roles
            - Verify separation of duties
            - Check approval workflows
            - Validate transaction limits
            
            **Scenario 4: Account Structure Planning**
            Design multi-user account:
            - Review available roles
            - Plan permission hierarchy
            - Set appropriate limits
            - Balance security vs usability
            
            **Scenario 5: Permission Matrix Documentation**
            Create reference documentation:
            - Export all roles and permissions
            - Create training materials
            - Update customer communications
            - Support system integrations
            
            **Response Includes:**
            - Full role configurations
            - Pagination details
            - Total count of roles
            - Sorted by role hierarchy
            
            **Related Endpoints:**
            - GET /api/products/{code}/roles/{id} - View specific role details
            - POST /api/products/{code}/roles - Add new role
            - PUT /api/products/{code}/roles/{id} - Update role permissions
            """,
        tags = {"Product Roles & Permissions"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Role configurations retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "FD001 Role Configuration",
                        summary = "Fixed Deposit under 500000 - role setup",
                        value = """
                            {
                              "content": [
                                {
                                  "roleId": "f1097ab9-789c-4bf2-984a-eaa6d42b1fed",
                                  "roleCode": "ROLE001",
                                  "roleType": "OWNER",
                                  "roleName": "OWNER",
                                  "permissions": ["VIEW", "TRANSACT", "MODIFY", "CLOSE"],
                                  "transactionLimit": null,
                                  "requiresApproval": false,
                                  "isActive": true,
                                  "createdAt": "2025-01-15T10:00:00"
                                }
                              ],
                              "totalElements": 1,
                              "totalPages": 1,
                              "number": 0,
                              "size": 10,
                              "numberOfElements": 1,
                              "empty": false
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Joint Account Roles",
                        summary = "Two owners with equal rights",
                        value = """
                            {
                              "content": [
                                {
                                  "roleId": "role-991e8400-e29b-41d4-a716-446655440001",
                                  "roleName": "PRIMARY_OWNER",
                                  "roleDescription": "Primary account holder with full control",
                                  "permissions": ["VIEW", "TRANSACT", "MODIFY", "CLOSE"],
                                  "transactionLimit": null,
                                  "requiresApproval": false,
                                  "isActive": true,
                                  "createdAt": "2025-01-15T10:00:00"
                                },
                                {
                                  "roleId": "role-992e8400-e29b-41d4-a716-446655440002",
                                  "roleName": "CO_OWNER",
                                  "roleDescription": "Joint account holder with equal rights",
                                  "permissions": ["VIEW", "TRANSACT", "MODIFY", "CLOSE"],
                                  "transactionLimit": null,
                                  "requiresApproval": false,
                                  "isActive": true,
                                  "createdAt": "2025-01-15T10:00:00"
                                }
                              ],
                              "totalElements": 2,
                              "totalPages": 1,
                              "number": 0,
                              "size": 10,
                              "numberOfElements": 2,
                              "empty": false
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Business Account Roles",
                        summary = "Multiple roles with varied permissions",
                        value = """
                            {
                              "content": [
                                {
                                  "roleId": "role-991e8400-e29b-41d4-a716-446655440001",
                                  "roleName": "PRIMARY_OWNER",
                                  "roleDescription": "Business owner with full control",
                                  "permissions": ["VIEW", "TRANSACT", "MODIFY", "CLOSE"],
                                  "transactionLimit": null,
                                  "requiresApproval": false,
                                  "isActive": true
                                },
                                {
                                  "roleId": "role-992e8400-e29b-41d4-a716-446655440002",
                                  "roleName": "AUTHORIZED_USER",
                                  "roleDescription": "Finance manager with transaction limits",
                                  "permissions": ["VIEW", "TRANSACT"],
                                  "transactionLimit": 50000.00,
                                  "requiresApproval": true,
                                  "isActive": true
                                },
                                {
                                  "roleId": "role-993e8400-e29b-41d4-a716-446655440003",
                                  "roleName": "VIEW_ONLY",
                                  "roleDescription": "Accountant with read-only access",
                                  "permissions": ["VIEW"],
                                  "transactionLimit": 0.00,
                                  "requiresApproval": false,
                                  "isActive": true
                                }
                              ],
                              "totalElements": 3,
                              "totalPages": 1,
                              "number": 0,
                              "size": 10,
                              "numberOfElements": 3,
                              "empty": false
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Product Not Found",
                    value = """
                        {
                          "error": "Resource Not Found",
                          "message": "Product not found with code: SAV-INVALID-2025",
                          "timestamp": "2025-10-18T11:00:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<Page<ProductRoleDTO>> getRoles(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Pagination parameters:
                    - page: Page number (0-indexed)
                    - size: Items per page
                    - sort: Sort field and direction
                    
                    Example: ?page=0&size=10&sort=roleName,asc
                    """,
                example = "page=0&size=10"
            )
            Pageable pageable) {
        return ResponseEntity.ok(productRoleService.getRolesForProduct(productCode, pageable));
    }

    @GetMapping("/{roleId}")
    @Operation(
        summary = "Retrieve specific role configuration details",
        description = """
            Get detailed configuration for a single user role including all permissions and restrictions.
            
            **What You Get:**
            Complete role configuration:
            - Role name and description
            - Full permission list
            - Transaction limits
            - Approval requirements
            - Active/inactive status
            - Creation and modification history
            
            **Use Cases:**
            
            **Scenario 1: Permission Verification**
            User asks "What can I do with this account?":
            - Retrieve their assigned role
            - Show specific permissions
            - Explain transaction limits
            - Clarify restrictions
            
            **Scenario 2: Before Modifying Role**
            Review current configuration:
            - Check existing permissions
            - Verify transaction limits
            - Plan permission updates
            - Avoid unintended changes
            
            **Scenario 3: Access Dispute Resolution**
            User reports unexpected access denial:
            - Check assigned role
            - Verify permission settings
            - Compare with expected access
            - Identify configuration issues
            
            **Scenario 4: Role Documentation**
            Create detailed documentation:
            - Export role specifications
            - Document permission levels
            - Create training materials
            - Support integration testing
            
            **Related Endpoints:**
            - GET /api/products/{code}/roles - View all role configurations
            - PUT /api/products/{code}/roles/{id} - Update role permissions
            - DELETE /api/products/{code}/roles/{id} - Remove role access
            """,
        tags = {"Product Roles & Permissions"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Role configuration found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductRoleDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Primary Owner Details",
                        summary = "Full control role configuration",
                        value = """
                            {
                              "roleId": "role-991e8400-e29b-41d4-a716-446655440001",
                              "roleName": "PRIMARY_OWNER",
                              "roleDescription": "Primary account holder with full control",
                              "permissions": ["VIEW", "TRANSACT", "MODIFY", "CLOSE"],
                              "transactionLimit": null,
                              "requiresApproval": false,
                              "isActive": true,
                              "createdAt": "2025-01-15T10:00:00",
                              "updatedAt": "2025-01-15T10:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Authorized User Details",
                        summary = "Limited permissions with approval",
                        value = """
                            {
                              "roleId": "role-992e8400-e29b-41d4-a716-446655440002",
                              "roleName": "AUTHORIZED_USER",
                              "roleDescription": "Can perform transactions with daily limits and approval requirements",
                              "permissions": ["VIEW", "TRANSACT"],
                              "transactionLimit": 10000.00,
                              "requiresApproval": true,
                              "isActive": true,
                              "createdAt": "2025-01-15T10:00:00",
                              "updatedAt": "2025-03-20T14:30:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Role configuration or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Role Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Role configuration not found with ID: role-999e8400-invalid",
                              "timestamp": "2025-10-18T11:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Product Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Product not found with code: SAV-INVALID-2025",
                              "timestamp": "2025-10-18T11:00:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ProductRoleDTO> getRoleByCode(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Role configuration ID (UUID).
                    
                    Unique identifier for the role configuration.
                    """,
                required = true,
                example = "role-991e8400-e29b-41d4-a716-446655440001"
            )
            @PathVariable String roleId) {
        return ResponseEntity.ok(productRoleService.getRoleByCode(productCode, roleId));
    }

    @PutMapping("/{roleId}")
    @Operation(
        summary = "Update role permissions and configuration",
        description = """
            Modify an existing role's permissions, limits, and settings for product access control.
            
            **What You Can Update:**
            - Role description
            - Permission list
            - Transaction limits
            - Approval requirements
            - Active/inactive status
            
            **Update Scenarios:**
            
            **Scenario 1: Expand Permissions**
            Grant additional access rights:
            - Add MODIFY permission to VIEW_ONLY role
            - Enable transaction rights
            - Upgrade authorized user capabilities
            - Immediate effect on all users with this role
            
            **Scenario 2: Restrict Permissions**
            Tighten security controls:
            - Remove CLOSE permission
            - Reduce transaction limits
            - Add approval requirements
            - Prevent unauthorized actions
            
            **Scenario 3: Adjust Transaction Limits**
            Modify spending controls:
            - Increase limit for trusted users
            - Decrease limit due to fraud concerns
            - Remove limit (set to null)
            - Add new limit to unlimited role
            
            **Scenario 4: Enable Approval Workflow**
            Add oversight requirements:
            - Set requiresApproval to true
            - Transactions need secondary approval
            - Enhance compliance controls
            - Reduce fraud risk
            
            **Scenario 5: Temporarily Disable Role**
            Suspend without deletion:
            - Set isActive to false
            - Keep configuration intact
            - No permanent removal
            - Easy re-activation
            
            **Best Practices:**
            - Test changes in staging environment
            - Notify affected users of permission changes
            - Update during low-traffic periods
            - Document reason for changes
            - Monitor for unexpected access issues
            - Keep audit trail of modifications
            
            **Warning:**
            Permission changes take effect immediately for all users assigned this role.
            Reducing permissions may interrupt active user sessions.
            
            **Related Endpoints:**
            - GET /api/products/{code}/roles/{id} - View current configuration
            - DELETE /api/products/{code}/roles/{id} - Remove role
            - GET /api/products/{code}/roles - View all roles
            """,
        tags = {"Product Roles & Permissions"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Role configuration updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductRoleDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Expanded Permissions",
                        summary = "Upgraded view-only to transaction rights",
                        value = """
                            {
                              "roleId": "role-993e8400-e29b-41d4-a716-446655440003",
                              "roleName": "AUTHORIZED_USER",
                              "roleDescription": "Upgraded from view-only to transaction rights",
                              "permissions": ["VIEW", "TRANSACT"],
                              "transactionLimit": 5000.00,
                              "requiresApproval": false,
                              "isActive": true,
                              "createdAt": "2025-01-15T10:00:00",
                              "updatedAt": "2025-10-18T11:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Added Approval Requirement",
                        summary = "Enhanced security with approval workflow",
                        value = """
                            {
                              "roleId": "role-992e8400-e29b-41d4-a716-446655440002",
                              "roleName": "AUTHORIZED_USER",
                              "roleDescription": "Finance manager with mandatory approval for large transactions",
                              "permissions": ["VIEW", "TRANSACT"],
                              "transactionLimit": 50000.00,
                              "requiresApproval": true,
                              "isActive": true,
                              "createdAt": "2025-01-15T10:00:00",
                              "updatedAt": "2025-10-18T11:30:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Role configuration or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Role Not Found",
                    value = """
                        {
                          "error": "Resource Not Found",
                          "message": "Role configuration not found with ID: role-999e8400-invalid",
                          "timestamp": "2025-10-18T11:30:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid role configuration",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Permission",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Invalid permission: INVALID_PERM. Allowed: VIEW, TRANSACT, MODIFY, CLOSE",
                              "timestamp": "2025-10-18T11:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Negative Transaction Limit",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Transaction limit must be positive or null for unlimited",
                              "timestamp": "2025-10-18T11:30:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ProductRoleDTO> updateRole(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = "Role configuration ID (UUID) to update",
                required = true,
                example = "role-991e8400-e29b-41d4-a716-446655440001"
            )
            @PathVariable String roleId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = """
                    Updated role configuration.
                    
                    Any omitted fields will retain their current values.
                    """,
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductRoleRequestDTO.class),
                    examples = {
                        @ExampleObject(
                            name = "Expand Permissions",
                            summary = "Add transaction rights to view-only role",
                            value = """
                                {
                                  "roleName": "AUTHORIZED_USER",
                                  "roleDescription": "Upgraded from view-only to transaction rights",
                                  "permissions": ["VIEW", "TRANSACT"],
                                  "transactionLimit": 5000.00,
                                  "requiresApproval": false,
                                  "isActive": true
                                }
                                """
                        ),
                        @ExampleObject(
                            name = "Add Approval Workflow",
                            summary = "Require approval for transactions",
                            value = """
                                {
                                  "roleName": "AUTHORIZED_USER",
                                  "roleDescription": "Finance manager with mandatory approval for large transactions",
                                  "permissions": ["VIEW", "TRANSACT"],
                                  "transactionLimit": 50000.00,
                                  "requiresApproval": true,
                                  "isActive": true
                                }
                                """
                        )
                    }
                )
            )
            @Valid @RequestBody ProductRoleRequestDTO roleDto) {
        return ResponseEntity.ok(productRoleService.updateRole(productCode, roleId, roleDto));
    }

    @DeleteMapping("/{roleId}")
    @Operation(
        summary = "Permanently remove role configuration from product",
        description = """
            Delete a role configuration, removing access capability for this role type.
            
            **⚠️ CRITICAL WARNING:**
            This action is PERMANENT and IRREVERSIBLE!
            - Role configuration will be lost forever
            - Users assigned this role will lose access
            - Cannot be recovered
            - May disrupt active user sessions
            - No undo functionality
            
            **Before Deleting:**
            1. **Check User Assignments:** Verify no users currently have this role
            2. **Impact Assessment:** Understand who will be affected
            3. **Communication Plan:** Notify affected users in advance
            4. **Alternative Access:** Provide alternative role assignments
            5. **Document Reason:** Keep audit trail of why role was removed
            6. **Consider Disable:** Set isActive=false instead of deleting
            
            **When to DELETE:**
            - Role type permanently discontinued
            - Simplifying role structure
            - No users assigned to this role
            - Product being decommissioned
            - Duplicate role cleanup
            - Test/development role removal
            
            **When to DISABLE (Recommended Alternative):**
            - Temporary suspension needed
            - Want to preserve configuration
            - Users might need role later
            - Testing new access structure
            - Uncertain about permanent removal
            - Regulatory compliance requires history
            
            **Use PUT /api/products/{code}/roles/{id} to disable:**
            ```
            {
              "isActive": false
            }
            ```
            
            **Recommended Workflow:**
            1. Get role details (verify correct role)
            2. Check user assignments
            3. Notify affected users (30-day notice)
            4. Migrate users to alternative roles
            5. Disable role first (test impact)
            6. Monitor for 30 days
            7. Delete if no issues arise
            
            **What Happens:**
            - Role removed from database
            - Role type no longer available
            - Users with this role lose access immediately
            - New assignments impossible
            - Role ID becomes invalid
            - Historical data remains but role reference broken
            
            **Impact Examples:**
            - Delete PRIMARY_OWNER → Accounts become orphaned (DANGEROUS!)
            - Delete VIEW_ONLY → Read-only users lose access
            - Delete AUTHORIZED_USER → Transaction permissions revoked
            
            **Dangerous Deletions:**
            - PRIMARY_OWNER (critical for account ownership)
            - CO_OWNER (joint account management)
            - Any role with active user assignments
            
            **Related Endpoints:**
            - PUT /api/products/{code}/roles/{id} - Disable instead of delete
            - GET /api/products/{code}/roles/{id} - Verify before deletion
            - GET /api/products/{code}/roles - View remaining roles
            """,
        tags = {"Product Roles & Permissions"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Role permanently deleted (no content returned)"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Role configuration or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Role Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Role configuration not found with ID: role-999e8400-invalid",
                              "timestamp": "2025-10-18T11:45:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Product Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Product not found with code: SAV-INVALID-2025",
                              "timestamp": "2025-10-18T11:45:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<Void> deleteRole(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Role configuration ID (UUID) to permanently delete.
                    
                    ⚠️ WARNING: This action cannot be undone!
                    Users with this role will immediately lose access.
                    Consider disabling the role instead using PUT endpoint with isActive=false.
                    """,
                required = true,
                example = "role-991e8400-e29b-41d4-a716-446655440001"
            )
            @PathVariable String roleId) {
        productRoleService.deleteRole(productCode, roleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/audit-trail")
    @Operation(
        summary = "Get complete audit trail of all roles for a product",
        description = "Retrieve ALL versions of ALL role configurations for audit purposes.",
        tags = {"Product Role Management"}
    )
    public ResponseEntity<List<ProductRoleDTO>> getRolesAuditTrail(
            @Parameter(description = "Product code", required = true)
            @PathVariable String productCode) {
        return ResponseEntity.ok(productRoleService.getRolesAuditTrail(productCode));
    }

    @GetMapping("/{roleCode}/audit-trail")
    @Operation(
        summary = "Get complete audit trail of a specific role",
        description = "Retrieve ALL versions of a specific role configuration for audit purposes.",
        tags = {"Product Role Management"}
    )
    public ResponseEntity<List<ProductRoleDTO>> getRoleAuditTrail(
            @Parameter(description = "Product code", required = true)
            @PathVariable String productCode,
            @Parameter(description = "Role code", required = true)
            @PathVariable String roleCode) {
        return ResponseEntity.ok(productRoleService.getRoleAuditTrail(productCode, roleCode));
    }
}