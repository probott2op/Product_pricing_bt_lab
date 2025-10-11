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
- **Charge Types**: FIXED amount or PERCENTAGE-based
- **Calculation Methods**: Various calculation approaches
- **Frequency**: ONE_TIME, MONTHLY, QUARTERLY, ANNUALLY
- **Debit/Credit Indicators**: Define charge direction
- **Service Charges**: Account maintenance fees, transaction fees, penalty charges

#### Common Charges:
- Account maintenance fees
- Transaction charges
- ATM withdrawal fees
- Overdraft charges
- Late payment penalties
- Processing fees

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
- **Transaction Types**: DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT, etc.
- **Transaction Limits**: Minimum/maximum amounts, daily limits
- **Channel Availability**: ATM, ONLINE, BRANCH, MOBILE
- **Frequency Restrictions**: Daily transaction count limits
- **Processing Rules**: Define transaction behavior

#### Use Cases:
- Define allowed operations per product
- Set transaction limits by channel
- Configure fraud prevention rules
- Manage channel-specific restrictions

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
- **Communication Channels**: EMAIL, SMS, PUSH_NOTIFICATION, IN_APP, POSTAL_MAIL
- **Communication Types**: TRANSACTIONAL, PROMOTIONAL, REGULATORY, STATEMENT
- **Event Triggers**: ACCOUNT_OPENING, TRANSACTION_ALERT, STATEMENT_GENERATION, PAYMENT_DUE
- **Template Management**: Content templates for various events
- **Multi-channel Support**: Send via multiple channels simultaneously

#### Common Templates:
- Account opening confirmation
- Transaction alerts
- Statement generation
- Payment due reminders
- Promotional offers
- Regulatory notices

---

### 8. **Product Roles & Permissions** (`/api/products/{productCode}/roles`)
**Purpose**: Role-based access control

#### Endpoints:
- `POST /api/products/{productCode}/roles` - Add role
- `GET /api/products/{productCode}/roles` - Get all roles (paginated)
- `GET /api/products/{productCode}/roles/{roleCode}` - Get specific role
- `PUT /api/products/{productCode}/roles/{roleCode}` - Update role
- `DELETE /api/products/{productCode}/roles/{roleCode}` - Delete role

#### Key Features:
- **User Types**: CUSTOMER, BRANCH_MANAGER, RELATIONSHIP_OFFICER, ADMIN
- **Maker-Checker Workflows**: Define approval requirements
- **Access Levels**: Configure operation permissions
- **Approval Workflows**: Multi-level authorization
- **Audit Compliance**: Track who can perform what operations

#### Common Roles:
- Customer self-service operations
- Branch staff operations
- Manager approval requirements
- Admin configuration access

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
  "chargeType": "FIXED",
  "chargeValue": 10.00,
  "frequency": "MONTHLY",
  "debitCredit": "DEBIT",
  "calculationType": "FLAT_AMOUNT"
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
