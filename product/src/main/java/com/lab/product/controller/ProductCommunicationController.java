package com.lab.product.controller;

import com.lab.product.DTO.ProductCommunicationDTO;
import com.lab.product.DTO.ProductCommunicationRequestDTO;
import com.lab.product.service.ProductCommunicationService;
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
@RequestMapping("/api/products/{productCode}/communications")
@RequiredArgsConstructor
@Tag(
    name = "Product Communication Templates",
    description = "API for managing customer communication templates and notification configurations for products. " +
                  "Communication templates define automated messages sent to customers for various events " +
                  "(ACCOUNT_OPENING, TRANSACTION_ALERT, STATEMENT_GENERATION, PAYMENT_DUE, etc.). " +
                  "Supports multiple communication channels (EMAIL, SMS, PUSH_NOTIFICATION, IN_APP, POSTAL_MAIL) " +
                  "and communication types (TRANSACTIONAL, PROMOTIONAL, REGULATORY, STATEMENT). " +
                  "Critical for customer engagement, compliance notifications, and regulatory reporting."
)
public class ProductCommunicationController {

    @Autowired
    private final ProductCommunicationService productCommunicationService;

    @PostMapping
    @Operation(
        summary = "Configure customer communication template for product",
        description = """
            Create automated communication template for customer notifications related to product events.
            
            **Communication Types:**
            - **ALERT** - Security alerts, fraud warnings, transaction alerts
            - **NOTICE** - Compliance notifications, legal disclosures, regulatory notices
            - **STATEMENT** - Account statements, summaries, periodic reports
            
            **Communication Channels:**
            - **EMAIL** - Electronic mail
            - **SMS** - Text messages
            - **POST** - Physical postal mail
            
            **Trigger Events:**
            - **ACCOUNT_OPENING** - New account created
            - **TRANSACTION_COMPLETE** - Transaction processed
            - **BALANCE_LOW** - Balance below threshold
            - **PAYMENT_DUE** - Payment deadline approaching
            - **MATURITY_APPROACHING** - FD/Loan maturity soon
            - **INTEREST_CREDITED** - Interest posted
            - **STATEMENT_READY** - Monthly statement generated
            - **FRAUD_DETECTED** - Suspicious activity
            
            **Template Configuration Includes:**
            - Communication name and type
            - Trigger event
            - Communication channels
            - Subject line and message template
            - Frequency limits (prevent spam)
            - Personalization variables
            - Active/inactive status
            
            **Use Cases:**
            
            **Scenario 1: Transaction Alerts**
            Notify customers of account activity:
            - SMS for withdrawals over ₹50,000
            - Email for all transfers
            - Push notification for card purchases
            - Immediate delivery
            
            **Scenario 2: Statement Generation**
            Monthly account statements:
            - Email with PDF attachment
            - In-app notification
            - Optional postal mail
            - Scheduled monthly delivery
            
            **Scenario 3: Payment Reminders**
            Loan/credit card payment due:
            - Email 7 days before due date
            - SMS 3 days before due date
            - Phone call 1 day before (overdue only)
            - Prevent late fees
            
            **Scenario 4: Promotional Offers**
            Marketing campaigns:
            - Email with product upgrades
            - In-app promotional banners
            - Frequency limit: max 1 per week
            - Opt-out capability required
            
            **Scenario 5: Compliance Notifications**
            Regulatory requirements:
            - Annual disclosures via email
            - Terms & conditions updates
            - Privacy policy changes
            - Legally mandated
            
            **Related Endpoints:**
            - GET /api/products/{code}/communications - View all templates
            - PUT /api/products/{code}/communications/{id} - Update template
            - DELETE /api/products/{code}/communications/{id} - Remove template
            """,
        tags = {"Product Communication Templates"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Communication template created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductCommunicationDTO.class),
                examples = {
                    @ExampleObject(
                        name = "FD001 Monthly Statement",
                        summary = "Fixed Deposit monthly statement via email",
                        value = """
                            {
                              "commId": "d3ba2f49-7801-4c63-99bd-4e64e5f7c155",
                              "commCode": "COMM_MONTHLY_STATEMENT",
                              "communicationType": "STATEMENT",
                              "communicationChannel": "EMAIL",
                              "event": "COMM_MONTHLY_STATEMENT",
                              "template": "Dear Customer, Your Fixed Deposit statement for the month is ready.",
                              "frequencyLimit": 1,
                              "isActive": true,
                              "createdAt": "2025-10-18T12:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Transaction Alert",
                        summary = "SMS alert for large withdrawals",
                        value = """
                            {
                              "commId": "991e8400-e29b-41d4-a716-446655440001",
                              "commCode": "COMM_WITHDRAWAL_ALERT",
                              "communicationType": "ALERT",
                              "channel": "SMS",
                              "event": "COMM_WITHDRAWAL_ALERT",
                              "template": "Dear ${CUSTOMER_NAME}, ${AMOUNT} was withdrawn from your ${PRODUCT_NAME} account (${ACCOUNT_NUMBER}) on ${DATE}. If you did not authorize this, contact us immediately.",
                              "frequencyLimit": 0
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Notice Letter",
                        summary = "Postal notice for regulatory changes",
                        value = """
                            {
                              "commId": "993e8400-e29b-41d4-a716-446655440003",
                              "commCode": "COMM_REGULATORY_NOTICE",
                              "communicationType": "NOTICE",
                              "channel": "POST",
                              "event": "COMM_REGULATORY_NOTICE",
                              "template": "Dear ${CUSTOMER_NAME}, This is to notify you of important changes to ${PRODUCT_NAME} terms and conditions effective ${EFFECTIVE_DATE}. Please review the enclosed documents carefully.",
                              "frequencyLimit": null
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
                          "timestamp": "2025-10-18T12:00:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid communication template configuration",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Missing Required Field",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Communication type is required",
                              "timestamp": "2025-10-18T12:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Invalid Channel",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Invalid channel: INVALID_CHANNEL. Allowed: EMAIL, SMS, POST",
                              "timestamp": "2025-10-18T12:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Duplicate Template",
                        value = """
                            {
                              "error": "Duplicate Entry",
                              "message": "Communication template for event 'TRANSACTION_COMPLETE' with channel 'SMS' already exists for this product",
                              "timestamp": "2025-10-18T12:00:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ProductCommunicationDTO> addCommunication(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = """
                    Communication template configuration.
                    
                    Required fields:
                    - communicationCode: Unique identifier for the communication template
                    - communicationName: Display name of the communication
                    - communicationType: ALERT, NOTICE, or STATEMENT
                    - templateContent: The message template with placeholders
                    - communicationChannel: EMAIL, SMS, or POST
                    
                    Optional:
                    - frequencyLimit: Maximum frequency (null for unlimited, 0 for immediate, positive integer for limit)
                    - description: Additional details about the communication
                    - isActive: Enable/disable template (default: true)
                    """,
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductCommunicationRequestDTO.class),
                    examples = {
                        @ExampleObject(
                            name = "Transaction Alert",
                            summary = "SMS alert for large withdrawals",
                            value = """
                                {
                                  "communicationCode": "COMM_WITHDRAWAL_ALERT",
                                  "communicationName": "Large Withdrawal Alert",
                                  "communicationType": "ALERT",
                                  "templateContent": "Dear ${CUSTOMER_NAME}, ${AMOUNT} was withdrawn from your ${PRODUCT_NAME} account (${ACCOUNT_NUMBER}) on ${DATE}. If you did not authorize this, contact us immediately.",
                                  "communicationChannel": "SMS",
                                  "frequencyLimit": 0,
                                  "description": "Immediate SMS alert for withdrawals over threshold",
                                  "isActive": true
                                }
                                """
                        ),
                        @ExampleObject(
                            name = "Monthly Statement",
                            summary = "Email statement with PDF",
                            value = """
                                {
                                  "communicationCode": "COMM_MONTHLY_STATEMENT",
                                  "communicationName": "Monthly Account Statement",
                                  "communicationType": "STATEMENT",
                                  "templateContent": "Dear ${CUSTOMER_NAME}, Your monthly statement for ${PRODUCT_NAME} account ending in ${LAST_4_DIGITS} is now available. Opening balance: ${OPENING_BALANCE}, Closing balance: ${CLOSING_BALANCE}. View full statement in the attachment.",
                                  "communicationChannel": "EMAIL",
                                  "frequencyLimit": 1,
                                  "description": "Monthly account statement via email",
                                  "isActive": true
                                }
                                """
                        ),
                        @ExampleObject(
                            name = "Notice Letter",
                            summary = "Postal notice for regulatory changes",
                            value = """
                                {
                                  "communicationCode": "COMM_REGULATORY_NOTICE",
                                  "communicationName": "Regulatory Notice",
                                  "communicationType": "NOTICE",
                                  "templateContent": "Dear ${CUSTOMER_NAME}, This is to notify you of important changes to ${PRODUCT_NAME} terms and conditions effective ${EFFECTIVE_DATE}. Please review the enclosed documents carefully.",
                                  "communicationChannel": "POST",
                                  "frequencyLimit": null,
                                  "description": "Postal mail for regulatory notifications",
                                  "isActive": true
                                }
                                """
                        )
                    }
                )
            )
            @Valid @RequestBody ProductCommunicationRequestDTO communicationDto) {
        return new ResponseEntity<>(productCommunicationService.addCommunicationToProduct(productCode, communicationDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "Retrieve all communication templates configured for product",
        description = """
            Get a paginated list of all communication templates and notification configurations for a product.
            
            **What You Get:**
            Complete communication matrix showing:
            - All configured templates
            - Trigger events per template
            - Communication channels used
            - Message templates and subjects
            - Frequency limits and timing
            - Active/inactive status
            
            **Use Cases:**
            
            **Scenario 1: Communication Audit**
            Review all customer touchpoints:
            - List all automated communications
            - Verify compliance notifications included
            - Check channel distribution
            - Identify communication gaps
            
            **Scenario 2: Customer Experience Mapping**
            Understand customer journey:
            - Map communications to lifecycle stages
            - Identify excessive notifications (spam risk)
            - Balance promotional vs transactional
            - Optimize timing and frequency
            
            **Scenario 3: Channel Strategy Review**
            Analyze communication channels:
            - SMS vs Email distribution
            - Mobile app notification usage
            - Postal mail requirements
            - Channel effectiveness metrics
            
            **Scenario 4: Compliance Verification**
            Regulatory audit requires:
            - Document all customer notifications
            - Verify mandatory disclosures
            - Check opt-out mechanisms
            - Validate data privacy compliance
            
            **Scenario 5: Communication Template Management**
            Maintain template library:
            - Export for documentation
            - Clone templates for similar products
            - Identify templates needing updates
            - Plan communication improvements
            
            **Scenario 6: Troubleshooting**
            Customer reports missing notifications:
            - Check if template exists
            - Verify template is active
            - Review trigger conditions
            - Investigate delivery issues
            
            **Response Includes:**
            - Full template configurations
            - Pagination details
            - Total template count
            - Sorted by communication type
            
            **Related Endpoints:**
            - GET /api/products/{code}/communications/{id} - View specific template
            - POST /api/products/{code}/communications - Add new template
            - PUT /api/products/{code}/communications/{id} - Update template
            """,
        tags = {"Product Communication Templates"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Communication templates retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "FD001 Communication Templates",
                        summary = "Fixed Deposit under 500000 - monthly statement",
                        value = """
                            {
                              "content": [
                                {
                                  "commId": "d3ba2f49-7801-4c63-99bd-4e64e5f7c155",
                                  "commCode": "COMM_MONTHLY_STATEMENT",
                                  "communicationType": "STATEMENT",
                                  "communicationChannel": "EMAIL",
                                  "frequencyLimit": 1,
                                  "triggerEvent": "STATEMENT_READY",
                                  "subject": "Your Monthly FD Statement",
                                  "messageTemplate": "Dear Customer, Your Fixed Deposit statement for the month is ready.",
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
                        name = "Savings Account Communications",
                        summary = "Multiple notification templates",
                        value = """
                            {
                              "content": [
                                {
                                  "communicationId": "comm-991e8400-e29b-41d4-a716-446655440001",
                                  "communicationName": "Welcome Email",
                                  "communicationType": "TRANSACTIONAL",
                                  "triggerEvent": "ACCOUNT_OPENING",
                                  "channels": ["EMAIL"],
                                  "subject": "Welcome to ${PRODUCT_NAME}!",
                                  "frequencyLimit": "ONCE",
                                  "isActive": true
                                },
                                {
                                  "communicationId": "comm-992e8400-e29b-41d4-a716-446655440002",
                                  "communicationName": "Transaction Alert",
                                  "communicationType": "ALERT",
                                  "triggerEvent": "TRANSACTION_COMPLETE",
                                  "channels": ["SMS", "EMAIL"],
                                  "subject": "Transaction Alert: ${AMOUNT}",
                                  "frequencyLimit": "IMMEDIATE",
                                  "isActive": true
                                },
                                {
                                  "communicationId": "comm-993e8400-e29b-41d4-a716-446655440003",
                                  "communicationName": "Monthly Statement",
                                  "communicationType": "STATEMENT",
                                  "triggerEvent": "STATEMENT_READY",
                                  "channels": ["EMAIL", "IN_APP"],
                                  "subject": "Your ${MONTH} Statement",
                                  "frequencyLimit": "MONTHLY",
                                  "isActive": true
                                },
                                {
                                  "communicationId": "comm-994e8400-e29b-41d4-a716-446655440004",
                                  "communicationName": "Low Balance Warning",
                                  "communicationType": "ALERT",
                                  "triggerEvent": "BALANCE_LOW",
                                  "channels": ["SMS", "PUSH_NOTIFICATION"],
                                  "subject": "Low Balance Alert",
                                  "frequencyLimit": "DAILY",
                                  "isActive": true
                                }
                              ],
                              "totalElements": 4,
                              "totalPages": 1,
                              "number": 0,
                              "size": 10,
                              "numberOfElements": 4,
                              "empty": false
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Loan Product Communications",
                        summary = "Payment reminders and statements",
                        value = """
                            {
                              "content": [
                                {
                                  "communicationId": "comm-995e8400-e29b-41d4-a716-446655440005",
                                  "communicationName": "Loan Disbursement Confirmation",
                                  "communicationType": "TRANSACTIONAL",
                                  "triggerEvent": "ACCOUNT_OPENING",
                                  "channels": ["EMAIL", "SMS"],
                                  "subject": "Loan Disbursed: ${AMOUNT}",
                                  "frequencyLimit": "ONCE",
                                  "isActive": true
                                },
                                {
                                  "communicationId": "comm-996e8400-e29b-41d4-a716-446655440006",
                                  "communicationName": "Payment Reminder",
                                  "communicationType": "REMINDER",
                                  "triggerEvent": "PAYMENT_DUE",
                                  "channels": ["EMAIL", "SMS", "PUSH_NOTIFICATION"],
                                  "subject": "Payment Due: ${AMOUNT} on ${DUE_DATE}",
                                  "frequencyLimit": "7_DAYS_BEFORE",
                                  "isActive": true
                                },
                                {
                                  "communicationId": "comm-997e8400-e29b-41d4-a716-446655440007",
                                  "communicationName": "Overdue Notice",
                                  "communicationType": "ALERT",
                                  "triggerEvent": "PAYMENT_OVERDUE",
                                  "channels": ["EMAIL", "SMS", "PHONE"],
                                  "subject": "Urgent: Payment Overdue",
                                  "frequencyLimit": "IMMEDIATE",
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
                          "timestamp": "2025-10-18T12:00:00"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<Page<ProductCommunicationDTO>> getCommunications(
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
                    
                    Example: ?page=0&size=10&sort=communicationType,asc
                    """,
                example = "page=0&size=10"
            )
            Pageable pageable) {
        return ResponseEntity.ok(productCommunicationService.getCommunicationsForProduct(productCode, pageable));
    }

    @GetMapping("/{commId}")
    @Operation(
        summary = "Retrieve specific communication template details",
        description = """
            Get detailed configuration for a single communication template.
            
            **What You Get:**
            Complete template configuration:
            - Template name and type
            - Trigger event
            - Communication channels
            - Subject line and message template
            - Personalization variables
            - Frequency limits
            - Active/inactive status
            - Creation and modification history
            
            **Use Cases:**
            
            **Scenario 1: Template Review Before Update**
            Verify current configuration:
            - Review existing message template
            - Check trigger conditions
            - Verify channel settings
            - Plan template improvements
            
            **Scenario 2: Copy Template to Another Product**
            Clone successful templates:
            - Export template configuration
            - Adapt for different product
            - Maintain consistent messaging
            - Accelerate product setup
            
            **Scenario 3: Troubleshoot Missing Notification**
            Customer didn't receive expected message:
            - Verify template is active
            - Check trigger event configuration
            - Review channel settings
            - Validate message content
            
            **Scenario 4: Compliance Documentation**
            Audit requires template details:
            - Export exact message content
            - Document delivery channels
            - Show frequency limits
            - Prove regulatory compliance
            
            **Scenario 5: Content Review**
            Marketing/Legal review needed:
            - Extract message template
            - Review personalization variables
            - Verify brand consistency
            - Check legal disclaimers
            
            **Related Endpoints:**
            - GET /api/products/{code}/communications - View all templates
            - PUT /api/products/{code}/communications/{id} - Update this template
            - DELETE /api/products/{code}/communications/{id} - Remove template
            """,
        tags = {"Product Communication Templates"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Communication template found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductCommunicationDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Transaction Alert Template",
                        summary = "Complete SMS/Email alert configuration",
                        value = """
                            {
                              "communicationId": "comm-991e8400-e29b-41d4-a716-446655440001",
                              "communicationName": "Large Withdrawal Alert",
                              "communicationType": "ALERT",
                              "triggerEvent": "TRANSACTION_COMPLETE",
                              "channels": ["SMS", "EMAIL"],
                              "subject": "Withdrawal Alert: ${AMOUNT} withdrawn from your account",
                              "messageTemplate": "Dear ${CUSTOMER_NAME}, ${AMOUNT} was withdrawn from your ${PRODUCT_NAME} account (${ACCOUNT_NUMBER}) on ${DATE}. If you did not authorize this, contact us immediately at 1-800-BANK-HELP.",
                              "personalizationVariables": ["CUSTOMER_NAME", "AMOUNT", "PRODUCT_NAME", "ACCOUNT_NUMBER", "DATE"],
                              "frequencyLimit": "IMMEDIATE",
                              "isActive": true,
                              "createdAt": "2025-01-15T10:00:00",
                              "updatedAt": "2025-10-18T12:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Monthly Statement Template",
                        summary = "Email statement with attachment",
                        value = """
                            {
                              "communicationId": "comm-992e8400-e29b-41d4-a716-446655440002",
                              "communicationName": "Monthly Account Statement",
                              "communicationType": "STATEMENT",
                              "triggerEvent": "STATEMENT_READY",
                              "channels": ["EMAIL"],
                              "subject": "Your ${PRODUCT_NAME} Statement for ${MONTH}",
                              "messageTemplate": "Dear ${CUSTOMER_NAME}, Your monthly statement for ${PRODUCT_NAME} account ending in ${LAST_4_DIGITS} is now available. \\n\\nOpening Balance: ${OPENING_BALANCE}\\nTotal Deposits: ${TOTAL_DEPOSITS}\\nTotal Withdrawals: ${TOTAL_WITHDRAWALS}\\nClosing Balance: ${CLOSING_BALANCE}\\n\\nView your complete statement in the attached PDF. For questions, reply to this email or call us at 1-800-BANK-HELP.",
                              "personalizationVariables": ["CUSTOMER_NAME", "PRODUCT_NAME", "MONTH", "LAST_4_DIGITS", "OPENING_BALANCE", "TOTAL_DEPOSITS", "TOTAL_WITHDRAWALS", "CLOSING_BALANCE"],
                              "frequencyLimit": "MONTHLY",
                              "isActive": true,
                              "createdAt": "2025-01-15T10:00:00",
                              "updatedAt": "2025-01-15T10:00:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Communication template or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Template Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Communication template not found with ID: comm-999e8400-invalid",
                              "timestamp": "2025-10-18T12:00:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Product Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Product not found with code: SAV-INVALID-2025",
                              "timestamp": "2025-10-18T12:00:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ProductCommunicationDTO> getCommunicationByCode(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Communication template ID (UUID).
                    
                    Unique identifier for the template configuration.
                    """,
                required = true,
                example = "comm-991e8400-e29b-41d4-a716-446655440001"
            )
            @PathVariable String commId) {
        return ResponseEntity.ok(productCommunicationService.getCommunicationByCode(productCode, commId));
    }

    @PutMapping("/{commId}")
    @Operation(
        summary = "Update communication template configuration",
        description = """
            Modify an existing communication template's content, channels, or settings.
            
            **What You Can Update:**
            - Template name and description
            - Communication type
            - Trigger event conditions
            - Communication channels
            - Subject line and message template
            - Personalization variables
            - Frequency limits
            - Active/inactive status
            
            **Update Scenarios:**
            
            **Scenario 1: Improve Message Content**
            Enhance customer communication:
            - Update message template wording
            - Add more personalization
            - Improve clarity and tone
            - Fix grammar/spelling errors
            
            **Scenario 2: Add Communication Channel**
            Expand notification reach:
            - Add SMS to email-only template
            - Enable push notifications
            - Add in-app messaging
            - Maintain message consistency
            
            **Scenario 3: Adjust Frequency**
            Prevent notification fatigue:
            - Change IMMEDIATE to DAILY for low-priority alerts
            - Increase reminder frequency
            - Add daily digest instead of per-event
            - Balance urgency vs spam
            
            **Scenario 4: Rebrand or Legal Update**
            Corporate changes require updates:
            - Update company name
            - Change support contact info
            - Add legal disclaimers
            - Update privacy statements
            
            **Scenario 5: A/B Testing**
            Test communication effectiveness:
            - Create variation with different subject
            - Test different message lengths
            - Compare channel effectiveness
            - Optimize based on results
            
            **Scenario 6: Temporarily Disable Template**
            Pause without deletion:
            - Set isActive to false
            - Keep configuration intact
            - No permanent removal
            - Easy re-activation
            
            **Best Practices:**
            - Test changes in staging environment
            - Review with legal/compliance team
            - Update during low-traffic periods
            - Notify customer service team
            - Monitor delivery success rates
            - Track customer feedback
            - Maintain version history
            
            **Warning:**
            Template changes affect all future communications immediately.
            Customers receiving messages during update may see inconsistent content.
            
            **Related Endpoints:**
            - GET /api/products/{code}/communications/{id} - View current template
            - DELETE /api/products/{code}/communications/{id} - Remove template
            - GET /api/products/{code}/communications - View all templates
            """,
        tags = {"Product Communication Templates"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Communication template updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductCommunicationDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Enhanced Message Content",
                        summary = "Improved personalization and clarity",
                        value = """
                            {
                              "communicationId": "comm-991e8400-e29b-41d4-a716-446655440001",
                              "communicationName": "Enhanced Withdrawal Alert",
                              "communicationType": "ALERT",
                              "triggerEvent": "TRANSACTION_COMPLETE",
                              "channels": ["SMS", "EMAIL", "PUSH_NOTIFICATION"],
                              "subject": "Account Alert: ${AMOUNT} withdrawal at ${LOCATION}",
                              "messageTemplate": "Hello ${CUSTOMER_FIRST_NAME}! We noticed a withdrawal of ${AMOUNT} from your ${PRODUCT_NAME} (${MASKED_ACCOUNT}) at ${LOCATION} on ${DATE} ${TIME}. Your new balance is ${CURRENT_BALANCE}. Wasn't you? Call us immediately at 1-800-BANK-HELP. Stay secure! - Your ${BANK_NAME} Team",
                              "frequencyLimit": "IMMEDIATE",
                              "isActive": true,
                              "createdAt": "2025-01-15T10:00:00",
                              "updatedAt": "2025-10-18T12:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Added Multi-Channel Support",
                        summary = "Expanded from email-only to multi-channel",
                        value = """
                            {
                              "communicationId": "comm-992e8400-e29b-41d4-a716-446655440002",
                              "communicationName": "Multi-Channel Statement Notification",
                              "communicationType": "STATEMENT",
                              "triggerEvent": "STATEMENT_READY",
                              "channels": ["EMAIL", "SMS", "IN_APP", "PUSH_NOTIFICATION"],
                              "subject": "Your ${MONTH} ${PRODUCT_NAME} Statement is Ready",
                              "messageTemplate": "Hi ${CUSTOMER_FIRST_NAME}, your ${MONTH} statement is now available! Check your account activity, balances, and more. View now in our app or check your email for the PDF version.",
                              "frequencyLimit": "MONTHLY",
                              "isActive": true,
                              "createdAt": "2025-01-15T10:00:00",
                              "updatedAt": "2025-10-18T12:30:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Communication template or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Template Not Found",
                    value = """
                        {
                          "error": "Resource Not Found",
                          "message": "Communication template not found with ID: comm-999e8400-invalid",
                          "timestamp": "2025-10-18T12:30:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid communication template configuration",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Trigger Event",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Invalid trigger event: INVALID_EVENT. Allowed events: ACCOUNT_OPENING, TRANSACTION_COMPLETE, BALANCE_LOW, etc.",
                              "timestamp": "2025-10-18T12:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Missing Personalization Variable",
                        value = """
                            {
                              "error": "Validation Error",
                              "message": "Message template uses undefined variable: ${UNDEFINED_VAR}",
                              "timestamp": "2025-10-18T12:30:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ProductCommunicationDTO> updateCommunication(
            @Parameter(
                description = "Product code",
                required = true,
                example = "FD001"
            )
            @PathVariable String productCode,
            @Parameter(
                description = "Communication template ID (UUID) to update",
                required = true,
                example = "COMM_OPENING"
            )
            @PathVariable String commId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = """
                    Updated communication template configuration.
                    
                    Any omitted fields will retain their current values.
                    """,
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductCommunicationRequestDTO.class),
                    examples = {
                        @ExampleObject(
                            name = "Enhance Message",
                            summary = "Add personalization and improve tone",
                            value = """
                                {
                                  "communicationName": "Enhanced Withdrawal Alert",
                                  "communicationType": "ALERT",
                                  "triggerEvent": "TRANSACTION_COMPLETE",
                                  "channels": ["SMS", "EMAIL", "PUSH_NOTIFICATION"],
                                  "subject": "Account Alert: ${AMOUNT} withdrawal at ${LOCATION}",
                                  "messageTemplate": "Hello ${CUSTOMER_FIRST_NAME}! We noticed a withdrawal of ${AMOUNT} from your ${PRODUCT_NAME} (${MASKED_ACCOUNT}) at ${LOCATION} on ${DATE} ${TIME}. Your new balance is ${CURRENT_BALANCE}. Wasn't you? Call us immediately at 1-800-BANK-HELP.",
                                  "frequencyLimit": "IMMEDIATE",
                                  "isActive": true
                                }
                                """
                        ),
                        @ExampleObject(
                            name = "Add Channels",
                            summary = "Enable additional notification channels",
                            value = """
                                {
                                  "communicationName": "Multi-Channel Statement Notification",
                                  "communicationType": "STATEMENT",
                                  "triggerEvent": "STATEMENT_READY",
                                  "channels": ["EMAIL", "SMS", "IN_APP", "PUSH_NOTIFICATION"],
                                  "subject": "Your ${MONTH} ${PRODUCT_NAME} Statement is Ready",
                                  "messageTemplate": "Hi ${CUSTOMER_FIRST_NAME}, your ${MONTH} statement is now available! View now in our app or check your email.",
                                  "frequencyLimit": "MONTHLY",
                                  "isActive": true
                                }
                                """
                        )
                    }
                )
            )
            @Valid @RequestBody ProductCommunicationRequestDTO communicationDto) {
        return ResponseEntity.ok(productCommunicationService.updateCommunication(productCode, commId, communicationDto));
    }

    @DeleteMapping("/{commId}")
    @Operation(
        summary = "Permanently remove communication template",
        description = """
            Delete a communication template, stopping all automated notifications for this event.
            
            **⚠️ CRITICAL WARNING:**
            This action is PERMANENT and IRREVERSIBLE!
            - Template configuration will be lost forever
            - Customers will stop receiving these notifications
            - Cannot be recovered
            - No undo functionality
            - May violate regulatory requirements
            
            **Before Deleting:**
            1. **Regulatory Check:** Verify not legally required (statements, disclosures)
            2. **Impact Assessment:** Understand who will be affected
            3. **Customer Communication:** Consider notifying customers of change
            4. **Alternative Solutions:** Check if disabling is better option
            5. **Document Reason:** Keep audit trail of why template was removed
            6. **Test Impact:** Verify in staging environment first
            
            **When to DELETE:**
            - Template permanently obsolete
            - Product being decommissioned
            - Duplicate template cleanup
            - Test/development template removal
            - Campaign-specific template ended
            - Template never used
            
            **When to DISABLE (Recommended Alternative):**
            - Temporary suspension needed
            - Want to preserve configuration
            - Seasonal or campaign-based
            - Testing new approach
            - Uncertain about permanent removal
            - Compliance requires history
            - May reactivate later
            
            **Use PUT /api/products/{code}/communications/{id} to disable:**
            ```
            {
              "isActive": false
            }
            ```
            
            **Recommended Workflow:**
            1. Get template details (verify correct template)
            2. Check if legally required
            3. Document deletion reason
            4. Notify stakeholders (30-day notice)
            5. Disable template first (test impact)
            6. Monitor customer feedback for 30 days
            7. Delete if no issues or complaints
            
            **What Happens:**
            - Template removed from database
            - No more notifications sent
            - Scheduled communications cancelled
            - Template ID becomes invalid
            - Historical messages remain
            - No future reference possible
            
            **Impact Examples:**
            - Delete STATEMENT template → Customers miss monthly statements (REGULATORY ISSUE!)
            - Delete TRANSACTION_ALERT → No fraud detection notifications (SECURITY RISK!)
            - Delete PAYMENT_REMINDER → Customers miss due dates (CUSTOMER HARM!)
            - Delete PROMOTIONAL → Marketing campaign stops (OK if campaign ended)
            
            **Dangerous Deletions (DO NOT DELETE):**
            - **REGULATORY/COMPLIANCE** templates (statements, disclosures, privacy notices)
            - **SECURITY** alerts (fraud, suspicious activity)
            - **CRITICAL** notifications (payment due, overdraft, balance low)
            - **TRANSACTIONAL** confirmations (legally required receipts)
            
            **Safe Deletions:**
            - Promotional campaigns (ended)
            - Seasonal greetings
            - Test templates
            - Duplicate templates
            - Unused templates
            
            **Related Endpoints:**
            - PUT /api/products/{code}/communications/{id} - Disable instead of delete
            - GET /api/products/{code}/communications/{id} - Verify before deletion
            - GET /api/products/{code}/communications - View remaining templates
            """,
        tags = {"Product Communication Templates"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Communication template permanently deleted (no content returned)"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Communication template or product not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Template Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Communication template not found with ID: comm-999e8400-invalid",
                              "timestamp": "2025-10-18T12:45:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Product Not Found",
                        value = """
                            {
                              "error": "Resource Not Found",
                              "message": "Product not found with code: SAV-INVALID-2025",
                              "timestamp": "2025-10-18T12:45:00"
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<Void> deleteCommunication(
            @Parameter(
                description = "Product code",
                required = true,
                example = "SAV-HIGH-YIELD-2025"
            )
            @PathVariable String productCode,
            @Parameter(
                description = """
                    Communication template ID (UUID) to permanently delete.
                    
                    ⚠️ WARNING: This action cannot be undone!
                    Customers will stop receiving these notifications immediately.
                    Verify template is not legally required (regulatory/compliance).
                    Consider disabling the template instead using PUT endpoint with isActive=false.
                    """,
                required = true,
                example = "comm-991e8400-e29b-41d4-a716-446655440001"
            )
            @PathVariable String commId) {
        productCommunicationService.deleteCommunication(productCode, commId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/audit-trail")
    @Operation(
        summary = "Get complete audit trail of all communications for a product",
        description = "Retrieve ALL versions of ALL communication templates for audit purposes.",
        tags = {"Product Communication Templates"}
    )
    public ResponseEntity<List<ProductCommunicationDTO>> getCommunicationsAuditTrail(
            @Parameter(description = "Product code", required = true)
            @PathVariable String productCode) {
        return ResponseEntity.ok(productCommunicationService.getCommunicationsAuditTrail(productCode));
    }

    @GetMapping("/{commCode}/audit-trail")
    @Operation(
        summary = "Get complete audit trail of a specific communication template",
        description = "Retrieve ALL versions of a specific communication template for audit purposes.",
        tags = {"Product Communication Templates"}
    )
    public ResponseEntity<List<ProductCommunicationDTO>> getCommunicationAuditTrail(
            @Parameter(description = "Product code", required = true)
            @PathVariable String productCode,
            @Parameter(description = "Communication code", required = true)
            @PathVariable String commCode) {
        return ResponseEntity.ok(productCommunicationService.getCommunicationAuditTrail(productCode, commCode));
    }
}