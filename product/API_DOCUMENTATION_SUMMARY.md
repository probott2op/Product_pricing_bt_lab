# Product & Pricing API - Comprehensive Documentation Summary

## 🎯 Overview

This document provides a comprehensive overview of the Product and Pricing Management API, designed for banking and financial institutions to manage financial products, pricing structures, and product configurations.

---

## 📚 API Structure

### Base URL
```
http://localhost:8080/api
```

### Authentication
All endpoints require authentication. Contact your system administrator for access credentials.

---

## 🏗️ API Modules

### 1. **Product Management** (`/api/products`)
**Purpose**: Core product lifecycle management

#### Endpoints:
- `POST /api/products` - Create a new financial product
- `GET /api/products` - Get all products (paginated)
- `GET /api/products/{productCode}` - Get product by code
- `PUT /api/products/{productCode}` - Update a product
- `DELETE /api/products/{productCode}` - Delete a product
- `GET /api/products/search` - Search products with filters

#### Key Features:
- Support for multiple product types (SAVINGS, CURRENT, LOAN, CREDIT_CARD, FIXED_DEPOSIT, RECURRING_DEPOSIT)
- Product status management (ACTIVE, INACTIVE, PENDING, SUSPENDED, CLOSED)
- Multi-currency support
- Effective and expiry date management
- Comprehensive audit trail

#### Example Product Types:
- **Savings Account**: Interest-bearing accounts for personal savings
- **Current Account**: Transaction accounts for businesses
- **Loan Products**: Personal loans, home loans, auto loans
- **Fixed Deposits**: Time-bound investment products
- **Credit Cards**: Revolving credit facilities

---

### 2. **Product Interest Rates** (`/api/products/{productCode}/interest-rates`)
**Purpose**: Manage interest rate configurations

#### Endpoints:
- `POST /api/products/{productCode}/interest-rates` - Add interest rate
- `GET /api/products/{productCode}/interest-rates` - Get all interest rates (paginated)
- `GET /api/products/{productCode}/interest-rates/{rateCode}` - Get specific interest rate
- `PUT /api/products/{productCode}/interest-rates/{rateCode}` - Update interest rate
- `DELETE /api/products/{productCode}/interest-rates/{rateCode}` - Delete interest rate

#### Key Features:
- **Rate Types**: SIMPLE, COMPOUND
- **Calculation Periods**: DAILY, MONTHLY, QUARTERLY, ANNUALLY
- **Credit Frequencies**: Define when interest is credited
- **Tiered Rates**: Balance slab-based interest rates
- **Date-Effective Rates**: Support for time-bound rate changes

#### Use Cases:
- Configure savings account interest rates
- Define loan interest calculations
- Set up promotional rates with expiry dates
- Create tiered interest structures based on balance

---

### 3. **Product Charges & Fees** (`/api/products/{productCode}/charges`)
**Purpose**: Configure product charges and fee structures

#### Endpoints:
- `POST /api/products/{productCode}/charges` - Add charge
- `GET /api/products/{productCode}/charges` - Get all charges (paginated)
- `GET /api/products/{productCode}/charges/{chargeCode}` - Get specific charge
- `PUT /api/products/{productCode}/charges/{chargeCode}` - Update charge
- `DELETE /api/products/{productCode}/charges/{chargeCode}` - Delete charge

#### Key Features:
- **Charge Types**: FEE, PENALTY, SERVICE_CHARGE
- **Calculation Methods**: FLAT, PERCENTAGE
- **Frequency**: ONE_TIME, MONTHLY, QUARTERLY, ANNUALLY
- **Debit/Credit Indicators**: DEBIT or CREDIT
- **Service Charges**: Account maintenance fees, transaction fees, penalty charges

#### Common Charges:
- Account maintenance fees
- Transaction charges
- ATM withdrawal fees
- Overdraft charges
- Late payment penalties
- Processing fees

#### Example - POST Request:
```json
{
  "chargeCode": "FEE001",
  "chargeName": "Maintenance fee",
  "chargeType": "FEE",
  "calculationType": "FLAT",
  "frequency": "QUARTERLY",
  "amount": 200,
  "debitCredit": "DEBIT"
}
```

#### Example - GET Response:
```json
{
  "chargeId": "31435394-2af5-4118-9cc7-4616df6a64ee",
  "chargeCode": "FEE001",
  "chargeName": "Maintenance fee",
  "chargeType": "FEE",
  "calculationType": "FLAT",
  "frequency": "QUARTERLY",
  "amount": 200,
  "debitCredit": "DEBIT"
}
```

---

### 4. **Product Balance Configuration** (`/api/products/{productCode}/balances`)
**Purpose**: Define balance types and restrictions

#### Endpoints:
- `POST /api/products/{productCode}/balances` - Add balance configuration
- `GET /api/products/{productCode}/balances` - Get all balance configs (paginated)
- `GET /api/products/{productCode}/balances/{balanceCode}` - Get specific balance config
- `PUT /api/products/{productCode}/balances/{balanceCode}` - Update balance config
- `DELETE /api/products/{productCode}/balances/{balanceCode}` - Delete balance config

#### Key Features:
- **Balance Types**: AVAILABLE_BALANCE, LEDGER_BALANCE, MINIMUM_BALANCE, OVERDRAFT_LIMIT
- **Minimum Balance Requirements**: Define mandatory balance levels
- **Maximum Balance Limits**: Set upper limits
- **Overdraft Allowances**: Configure overdraft facilities
- **Balance Calculation Methods**: Define how balances are computed

#### Use Cases:
- Set minimum balance requirements for accounts
- Configure overdraft limits
- Define balance calculation rules
- Set maximum deposit limits

---

### 5. **Product Business Rules** (`/api/products/{productCode}/rules`)
**Purpose**: Define business logic and validation rules

#### Endpoints:
- `POST /api/products/{productCode}/rules` - Add business rule
- `GET /api/products/{productCode}/rules` - Get all rules (paginated)
- `GET /api/products/{productCode}/rules/{ruleCode}` - Get specific rule
- `PUT /api/products/{productCode}/rules/{ruleCode}` - Update rule
- `DELETE /api/products/{productCode}/rules/{ruleCode}` - Delete rule

#### Key Features:
- **Rule Types**: ELIGIBILITY, TRANSACTION_LIMIT, VALIDATION, AGE_CRITERIA, KYC_REQUIREMENT
- **Data Types**: STRING, NUMBER, BOOLEAN, DATE
- **Validation Types**: MANDATORY, OPTIONAL, CONDITIONAL
- **Rule Values**: Define specific criteria and thresholds

#### Common Rules:
- Age eligibility (minimum/maximum age)
- KYC requirements
- Transaction limits
- Account opening criteria
- Credit score requirements
- Employment status validation

---

### 6. **Product Transaction Types** (`/api/products/{productCode}/transactions`)
**Purpose**: Configure allowed transaction types

#### Endpoints:
- `POST /api/products/{productCode}/transactions` - Add transaction type
- `GET /api/products/{productCode}/transactions` - Get all transaction types (paginated)
- `GET /api/products/{productCode}/transactions/{transactionCode}` - Get specific transaction type
- `PUT /api/products/{productCode}/transactions/{transactionCode}` - Update transaction type
- `DELETE /api/products/{productCode}/transactions/{transactionCode}` - Delete transaction type

#### Key Features:
- **Transaction Types**: DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT, INTEREST_ACCRUED, etc.
- **Transaction Control**: Enable/disable specific transaction types (isAllowed)
- **Channel Availability**: ATM, ONLINE, BRANCH, MOBILE
- **Frequency Restrictions**: Daily transaction count limits
- **Processing Rules**: Define transaction behavior

#### Use Cases:
- Define allowed operations per product
- Set transaction limits by channel
- Configure fraud prevention rules
- Manage channel-specific restrictions

#### Example - POST Request:
```json
{
  "transactionCode": "FD_DEPOSIT",
  "transactionType": "DEPOSIT",
  "isAllowed": true
}
```

#### Example - GET Response:
```json
{
  "transactionId": "3e6dcd5d-1615-4e93-8cd3-dc9a2bf8713f",
  "transactionCode": "FD_DEPOSIT",
  "transactionType": "DEPOSIT",
  "isAllowed": true
}
```

---

### 7. **Product Communication Templates** (`/api/products/{productCode}/communications`)
**Purpose**: Manage customer communication configurations

#### Endpoints:
- `POST /api/products/{productCode}/communications` - Add communication template
- `GET /api/products/{productCode}/communications` - Get all templates (paginated)
- `GET /api/products/{productCode}/communications/{commCode}` - Get specific template
- `PUT /api/products/{productCode}/communications/{commCode}` - Update template
- `DELETE /api/products/{productCode}/communications/{commCode}` - Delete template

#### Key Features:
- **Communication Channels**: EMAIL, SMS, PUSH_NOTIFICATION, IN_APP, POST
- **Communication Types**: ALERT, NOTICE, STATEMENT, NOTIFICATION
- **Event Triggers**: ACCOUNT_OPENING, TRANSACTION_ALERT, STATEMENT_GENERATION, PAYMENT_DUE
- **Template Management**: Content templates for various events with placeholders
- **Frequency Control**: Limit communication frequency (frequencyLimit)
- **Multi-channel Support**: Send via multiple channels simultaneously

#### Common Templates:
- Account opening confirmation
- Transaction alerts
- Statement generation
- Payment due reminders
- Promotional offers
- Regulatory notices

#### Example - POST Request:
```json
{
  "commCode": "COMM_OPENING",
  "communicationType": "ALERT",
  "channel": "SMS",
  "event": "COMM_OPENING",
  "template": "Dear ${CUSTOMER_NAME}, welcome! Your new ${PRODUCT_NAME} account (${ACCOUNT_NUMBER}) was successfully opened on ${DATE}. If you did not authorize this account opening, contact us immediately.",
  "frequencyLimit": 0
}
```

#### Example - GET Response:
```json
{
  "commId": "e1946a5c-1979-4dfa-a721-302e7a89bc07",
  "commCode": "COMM_OPENING",
  "communicationType": "ALERT",
  "channel": "SMS",
  "event": "COMM_OPENING",
  "template": "Dear ${CUSTOMER_NAME}, welcome! Your new ${PRODUCT_NAME} account (${ACCOUNT_NUMBER}) was successfully opened on ${DATE}. If you did not authorize this account opening, contact us immediately.",
  "frequencyLimit": 0
}
```

---

### 8. **Product Roles & Permissions** (`/api/products/{productCode}/roles`)
**Purpose**: Role-based access control for product accounts

#### Endpoints:
- `POST /api/products/{productCode}/roles` - Add role
- `GET /api/products/{productCode}/roles` - Get all roles (paginated)
- `GET /api/products/{productCode}/roles/{roleCode}` - Get specific role
- `PUT /api/products/{productCode}/roles/{roleCode}` - Update role
- `DELETE /api/products/{productCode}/roles/{roleCode}` - Delete role

#### Key Features:
- **Role Types**: OWNER, JOINT_HOLDER, NOMINEE, AUTHORIZED_SIGNATORY, GUARDIAN
- **Mandatory Roles**: Specify if role is required (isMandatory)
- **Role Limits**: Define maximum number allowed (maxCount)
- **Maker-Checker Workflows**: Define approval requirements
- **Access Levels**: Configure operation permissions
- **Audit Compliance**: Track who can perform what operations

#### Common Roles:
- Primary account owner (mandatory, maxCount: 1)
- Joint account holders (optional, maxCount: multiple)
- Nominees (optional)
- Authorized signatories
- Guardians for minor accounts

#### Example - POST Request:
```json
{
  "roleCode": "ROLE001",
  "roleType": "OWNER",
  "isMandatory": true,
  "maxCount": 1
}
```

#### Example - GET Response:
```json
{
  "roleId": "f1097ab9-789c-4bf2-984a-eaa6d42b1fed",
  "roleCode": "ROLE001",
  "roleType": "OWNER",
  "roleName": "OWNER",
  "isMandatory": true,
  "maxCount": 1
}
```

---

## 🔍 Common Patterns

### Pagination
All list endpoints support pagination:
```
GET /api/products?page=0&size=20&sort=productName,asc
```

**Parameters:**
- `page`: Page number (0-indexed, default: 0)
- `size`: Items per page (default: 20, max: 100)
- `sort`: Sort field and direction (e.g., `productCode,desc`)

### Code-Based Routing
All endpoints use business-friendly codes instead of technical UUIDs:
```
✅ Good: /api/products/SAV001
❌ Avoid: /api/products/123e4567-e89b-12d3-a456-426614174000
```

### Nested Resources
Sub-resources follow a hierarchical pattern:
```
/api/products/{productCode}/{subresource}/{code}
```

Example:
```
/api/products/SAV001/interest-rates/RATE001
/api/products/CURR001/charges/MAINT_FEE
/api/products/LOAN001/rules/AGE_LIMIT
```

---

## 📊 HTTP Status Codes

| Code | Meaning | Usage |
|------|---------|-------|
| 200 | OK | Successful GET/PUT request |
| 201 | Created | Successful POST request with resource creation |
| 204 | No Content | Successful DELETE request |
| 400 | Bad Request | Invalid request data or validation failure |
| 404 | Not Found | Resource not found |
| 500 | Internal Server Error | Server-side error |

---

## 🛡️ Error Response Format

```json
{
  "error": "Validation failed",
  "message": "Product code is required",
  "timestamp": "2025-10-10T10:30:00"
}
```

---

## 🔐 Security & Compliance

### Audit Trail
All operations automatically track:
- `createdBy`: User who created the record
- `createdDate`: Creation timestamp
- `updatedBy`: User who last updated
- `updatedDate`: Last update timestamp

### Data Validation
- Jakarta Bean Validation annotations
- Enum validation for predefined values
- Date range validation
- Custom business rule validation

### Deletion Policy
- Cascade deletion for related entities
- Consider using INACTIVE status instead of deletion for audit trail
- Irreversible operation - use with caution

---

## 📝 Best Practices

### 1. **Product Code Naming**
- Use meaningful, human-readable codes
- Examples: `SAV001`, `CURR_BASIC`, `LOAN_HOME_001`
- Keep codes concise but descriptive

### 2. **Date Management**
- Use ISO format: `YYYY-MM-DD`
- Set effective dates for future activation
- Use expiry dates for time-bound configurations

### 3. **Status Management**
- Use INACTIVE status instead of deletion when possible
- PENDING status for approval workflows
- SUSPENDED for temporary deactivation

### 4. **Search Optimization**
- Use search endpoint instead of loading all records
- Combine filters for precise results
- Use pagination for large datasets

### 5. **Error Handling**
- Always check response codes
- Parse error messages for details
- Implement retry logic for 500 errors

---

## 🚀 Quick Start Examples

### Create a Product
```http
POST /api/products
Content-Type: application/json

{
  "productCode": "SAV001",
  "productName": "Premium Savings Account",
  "productType": "SAVINGS",
  "status": "ACTIVE",
  "currency": "USD",
  "description": "High-interest savings account with premium features"
}
```

### Add Interest Rate
```http
POST /api/products/SAV001/interest-rates
Content-Type: application/json

{
  "rateCode": "RATE001",
  "rateType": "COMPOUND",
  "interestRate": 4.5,
  "calculationPeriod": "MONTHLY",
  "creditFrequency": "MONTHLY",
  "minBalance": 1000,
  "maxBalance": 100000
}
```

### Add Service Charge
```http
POST /api/products/SAV001/charges
Content-Type: application/json

{
  "chargeCode": "MAINT_FEE",
  "chargeName": "Monthly Maintenance Fee",
  "chargeType": "FEE",
  "calculationType": "FLAT",
  "frequency": "MONTHLY",
  "amount": 10.00,
  "debitCredit": "DEBIT"
}
```

### Add Communication Template
```http
POST /api/products/SAV001/communications
Content-Type: application/json

{
  "commCode": "COMM_WELCOME",
  "communicationType": "ALERT",
  "channel": "EMAIL",
  "event": "ACCOUNT_OPENING",
  "template": "Welcome ${CUSTOMER_NAME}! Your ${PRODUCT_NAME} account is now active.",
  "frequencyLimit": 1
}
```

### Add Transaction Type
```http
POST /api/products/SAV001/transactions
Content-Type: application/json

{
  "transactionCode": "ATM_WITHDRAWAL",
  "transactionType": "WITHDRAWAL",
  "isAllowed": true
}
```

### Add Role Configuration
```http
POST /api/products/SAV001/roles
Content-Type: application/json

{
  "roleCode": "PRIMARY_OWNER",
  "roleType": "OWNER",
  "isMandatory": true,
  "maxCount": 1
}
```

### Search Products
```http
GET /api/products/search?productType=SAVINGS&status=ACTIVE
```

---

## 📞 Support & Resources

### Documentation Access
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

### Contact Information
- **Email**: support@bankinglabs.com
- **Website**: https://www.bankinglabs.com/support
- **Full Documentation**: https://docs.bankinglabs.com/product-pricing-api

### Development Environments
- **Local**: `http://localhost:8080`
- **Development**: `https://api-dev.bankinglabs.com`
- **UAT**: `https://api-uat.bankinglabs.com`
- **Production**: `https://api.bankinglabs.com`

---

## 📄 License

This API is licensed under the Apache License 2.0.
See https://www.apache.org/licenses/LICENSE-2.0.html for details.

---

**Last Updated**: October 10, 2025  
**Version**: 1.0.0  
**API Status**: Production Ready ✅
