# Updated API Fields Summary

## Overview
This document summarizes all the fields that have been added to the API responses and request bodies to ensure consistency between POST, PUT, and GET operations.

---

## 1. Product Charges (`/api/products/{productCode}/charges`)

### POST/PUT Request Body
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

### GET Response
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

### New Fields Added:
- ✅ **chargeName** (String) - Descriptive name of the charge
- ✅ **debitCredit** (PRODUCT_DebitCredit enum) - Whether charge is DEBIT or CREDIT

---

## 2. Product Communications (`/api/products/{productCode}/communications`)

### POST/PUT Request Body
```json
{
  "commCode": "COMM_OPENING",
  "communicationType": "ALERT",
  "channel": "SMS",
  "event": "COMM_OPENING",
  "template": "Dear ${CUSTOMER_NAME}, welcome! Your new ${PRODUCT_NAME} account...",
  "frequencyLimit": 0
}
```

### GET Response
```json
{
  "commId": "e1946a5c-1979-4dfa-a721-302e7a89bc07",
  "commCode": "COMM_OPENING",
  "communicationType": "ALERT",
  "channel": "SMS",
  "event": "COMM_OPENING",
  "template": "Dear ${CUSTOMER_NAME}, welcome! Your new ${PRODUCT_NAME} account...",
  "frequencyLimit": 0
}
```

### Field Always Included:
- ✅ **template** (String) - Message template with placeholders
- ✅ **frequencyLimit** (Integer) - Maximum frequency for this communication

---

## 3. Product Transactions (`/api/products/{productCode}/transactions`)

### POST/PUT Request Body
```json
{
  "transactionCode": "FD_DEPOSIT",
  "transactionType": "DEPOSIT",
  "isAllowed": true
}
```

### GET Response
```json
{
  "transactionId": "3e6dcd5d-1615-4e93-8cd3-dc9a2bf8713f",
  "transactionCode": "FD_DEPOSIT",
  "transactionType": "DEPOSIT",
  "isAllowed": true
}
```

### Updated Field:
- ✅ **isAllowed** (boolean) - Replaces deprecated `amountLimit` field
  - `true` = transaction type is enabled
  - `false` = transaction type is disabled

---

## 4. Product Roles (`/api/products/{productCode}/roles`)

### POST/PUT Request Body
```json
{
  "roleCode": "ROLE001",
  "roleType": "OWNER",
  "isMandatory": true,
  "maxCount": 1
}
```

### GET Response
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

### New Fields Added:
- ✅ **isMandatory** (boolean) - Whether this role is required for the product
- ✅ **maxCount** (int) - Maximum number of users allowed in this role

---

## 5. Product Interest Rates (`/api/products/{productCode}/interest-rates`)

### POST/PUT Request Body
```json
{
  "rateCode": "INT12M001",
  "termInMonths": 12,
  "rateCumulative": 7.6,
  "rateNonCumulativeMonthly": 7.4,
  "rateNonCumulativeQuarterly": 7.5,
  "rateNonCumulativeYearly": 7.6
}
```

### GET Response
```json
{
  "rateId": "e75bed67-db57-405d-9f35-0af9d0d62e70",
  "rateCode": "INT12M001",
  "termInMonths": 12,
  "rateCumulative": 7.6,
  "rateNonCumulativeMonthly": 7.4,
  "rateNonCumulativeQuarterly": 7.5,
  "rateNonCumulativeYearly": 7.6
}
```

### All Fields Included (No Changes)

---

## 6. Product Balances (`/api/products/{productCode}/balances`)

### POST/PUT Request Body
```json
{
  "balanceType": "FD_PRINCIPAL",
  "isActive": true
}
```

### GET Response
```json
{
  "balanceId": "1360a975-0102-40d1-87d9-047c927dc794",
  "balanceType": "FD_PRINCIPAL",
  "isActive": true,
  "createdAt": "2025-10-19T18:44:59.9126"
}
```

### All Fields Included (No Changes)

---

## 7. Product Rules (`/api/products/{productCode}/rules`)

### POST/PUT Request Body
```json
{
  "ruleCode": "MIN001",
  "ruleName": "Minimum for FD001",
  "ruleType": "SIMPLE",
  "dataType": "NUMBER",
  "ruleValue": "10000",
  "validationType": "MIN_MAX"
}
```

### GET Response
```json
{
  "ruleId": "a9e3f3f3-a120-41a3-8e5e-b2c1f0b5e080",
  "ruleCode": "MIN001",
  "ruleName": "Minimum for FD001",
  "ruleType": "SIMPLE",
  "dataType": "NUMBER",
  "ruleValue": "10000",
  "validationType": "MIN_MAX"
}
```

### All Fields Included (No Changes)

---

## Complete Product GET Response Example

```json
{
  "productId": "7fd263eb-1553-46d7-b4ce-60811521229a",
  "productCode": "FD001",
  "productName": "Fixed Deposit under 500000",
  "productType": "FIXED_DEPOSIT",
  "currency": "INR",
  "status": "ACTIVE",
  "interestType": "COMPOUND",
  "compoundingFrequency": "QUARTERLY",
  "productRules": [
    {
      "ruleId": "a9e3f3f3-a120-41a3-8e5e-b2c1f0b5e080",
      "ruleCode": "MIN001",
      "ruleName": "Minimum for FD001",
      "ruleType": "SIMPLE",
      "dataType": "NUMBER",
      "ruleValue": "10000",
      "validationType": "MIN_MAX"
    }
  ],
  "productCharges": [
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
  ],
  "productRoles": [
    {
      "roleId": "f1097ab9-789c-4bf2-984a-eaa6d42b1fed",
      "roleCode": "ROLE001",
      "roleType": "OWNER",
      "roleName": "OWNER",
      "isMandatory": true,
      "maxCount": 1
    }
  ],
  "productTransactions": [
    {
      "transactionId": "3e6dcd5d-1615-4e93-8cd3-dc9a2bf8713f",
      "transactionCode": "FD_DEPOSIT",
      "transactionType": "DEPOSIT",
      "isAllowed": true
    }
  ],
  "productBalances": [
    {
      "balanceId": "1360a975-0102-40d1-87d9-047c927dc794",
      "balanceType": "FD_PRINCIPAL",
      "isActive": true,
      "createdAt": "2025-10-19T18:44:59.9126"
    }
  ],
  "productCommunications": [
    {
      "commId": "e1946a5c-1979-4dfa-a721-302e7a89bc07",
      "commCode": "COMM_OPENING",
      "communicationType": "ALERT",
      "channel": "SMS",
      "event": "COMM_OPENING",
      "template": "Dear ${CUSTOMER_NAME}, welcome! Your new ${PRODUCT_NAME} account...",
      "frequencyLimit": 0
    }
  ],
  "productInterests": [
    {
      "rateId": "e75bed67-db57-405d-9f35-0af9d0d62e70",
      "rateCode": "INT12M001",
      "termInMonths": 12,
      "rateCumulative": 7.6,
      "rateNonCumulativeMonthly": 7.4,
      "rateNonCumulativeQuarterly": 7.5,
      "rateNonCumulativeYearly": 7.6
    }
  ],
  "createdAt": "2025-10-09T11:38:44.950171",
  "efctv_date": null
}
```

---

## Summary of Changes

| Entity | New/Updated Fields | Purpose |
|--------|-------------------|---------|
| **ProductChargeDTO** | `chargeName`, `debitCredit` | Descriptive name and charge direction |
| **ProductCommunicationDTO** | `template`, `frequencyLimit` | Always included in response |
| **ProductTransactionDTO** | `isAllowed` (replaced `amountLimit`) | Transaction enablement flag |
| **ProductRoleDTO** | `isMandatory`, `maxCount` | Role requirements and limits |
| **ProductInterestDTO** | No changes | Already complete |
| **ProductBalanceDTO** | No changes | Already complete |
| **ProductRuleDTO** | No changes | Already complete |

---

## INSERT-ONLY Pattern Note

All entities follow the INSERT-ONLY pattern:
- Every update creates a new version with `crud_value = 'U'`
- Deletes create a new version with `crud_value = 'D'`
- GET endpoints return only the **latest non-deleted version** (`crud_value != 'D'` and `MAX(createdAt)`)
- No duplicate records in GET responses

---

## Testing Checklist

- [x] Product Charges - `chargeName` and `debitCredit` returned
- [x] Product Communications - `template` and `frequencyLimit` returned
- [x] Product Transactions - `isAllowed` returned
- [x] Product Roles - `isMandatory` and `maxCount` returned
- [x] All POST/PUT requests accept the same fields as GET returns
- [x] No duplicate records in nested collections
- [x] Latest versions only returned for all child entities

---

**Last Updated**: October 25, 2025  
**Version**: 2.0.0 (Field Consistency Update)
