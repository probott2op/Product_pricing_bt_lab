# API Testing Examples - Interest Type & Compounding Frequency

## Test Scenarios

### 1. Create Loan Product with Monthly Compound Interest

**Request:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "LOAN-PERSONAL-2025",
    "productName": "Personal Loan 2025",
    "productType": "LOAN",
    "currency": "USD",
    "status": "ACTIVE",
    "interestType": "COMPOUND",
    "compoundingFrequency": "MONTHLY",
    "efctv_date": "2025-01-01",
    "expr_date": "2030-12-31"
  }'
```

**Expected Response (201 Created):**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "productCode": "LOAN-PERSONAL-2025",
  "productName": "Personal Loan 2025",
  "productType": "LOAN",
  "currency": "USD",
  "status": "ACTIVE",
  "interestType": "COMPOUND",
  "compoundingFrequency": "MONTHLY",
  "productRules": [],
  "productCharges": [],
  "productBalances": [],
  "productRoles": [],
  "productTransactions": [],
  "productCommunications": [],
  "createdAt": "2025-10-15T10:30:00",
  "efctv_date": "2025-01-01"
}
```

---

### 2. Create Fixed Deposit with Simple Interest

**Request:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "FD-1YEAR-2025",
    "productName": "1 Year Fixed Deposit",
    "productType": "DEPOSIT",
    "currency": "USD",
    "status": "ACTIVE",
    "interestType": "SIMPLE",
    "efctv_date": "2025-01-01"
  }'
```

**Note:** `compoundingFrequency` is omitted for SIMPLE interest

**Expected Response (201 Created):**
```json
{
  "productId": "660e8400-e29b-41d4-a716-446655440111",
  "productCode": "FD-1YEAR-2025",
  "productName": "1 Year Fixed Deposit",
  "productType": "DEPOSIT",
  "currency": "USD",
  "status": "ACTIVE",
  "interestType": "SIMPLE",
  "compoundingFrequency": null,
  "productRules": [],
  "productCharges": [],
  "productBalances": [],
  "createdAt": "2025-10-15T10:31:00",
  "efctv_date": "2025-01-01"
}
```

---

### 3. Create Savings Account with Daily Compounding

**Request:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "SAVINGS-HIGH-YIELD",
    "productName": "High Yield Savings Account",
    "productType": "SAVINGS",
    "currency": "USD",
    "status": "ACTIVE",
    "interestType": "COMPOUND",
    "compoundingFrequency": "DAILY",
    "efctv_date": "2025-01-01"
  }'
```

---

### 4. Update Product Interest Configuration

**Request:**
```bash
curl -X PUT http://localhost:8080/api/products/LOAN-PERSONAL-2025 \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "LOAN-PERSONAL-2025",
    "productName": "Personal Loan 2025 - Updated",
    "productType": "LOAN",
    "currency": "USD",
    "status": "ACTIVE",
    "interestType": "COMPOUND",
    "compoundingFrequency": "QUARTERLY",
    "efctv_date": "2025-01-01"
  }'
```

**Expected Response (200 OK):**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "productCode": "LOAN-PERSONAL-2025",
  "productName": "Personal Loan 2025 - Updated",
  "productType": "LOAN",
  "currency": "USD",
  "status": "ACTIVE",
  "interestType": "COMPOUND",
  "compoundingFrequency": "QUARTERLY",
  "createdAt": "2025-10-15T10:30:00",
  "efctv_date": "2025-01-01"
}
```

---

### 5. Retrieve Product and Verify Interest Fields

**Request:**
```bash
curl -X GET http://localhost:8080/api/products/LOAN-PERSONAL-2025
```

**Expected Response (200 OK):**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "productCode": "LOAN-PERSONAL-2025",
  "productName": "Personal Loan 2025 - Updated",
  "productType": "LOAN",
  "currency": "USD",
  "status": "ACTIVE",
  "interestType": "COMPOUND",
  "compoundingFrequency": "QUARTERLY",
  "productRules": [],
  "productCharges": [],
  "productBalances": [],
  "createdAt": "2025-10-15T10:30:00",
  "efctv_date": "2025-01-01"
}
```

---

## Error Scenarios

### 6. Invalid Interest Type

**Request:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "TEST-001",
    "productName": "Test Product",
    "productType": "LOAN",
    "currency": "USD",
    "interestType": "COMPLEX",
    "efctv_date": "2025-01-01"
  }'
```

**Expected Response (400 Bad Request):**
```json
{
  "error": "Validation failed",
  "message": "Invalid enum value: No enum constant com.lab.product.entity.ENUMS.INTEREST_TYPE.COMPLEX",
  "timestamp": "2025-10-15T10:32:00"
}
```

---

### 7. Invalid Compounding Frequency

**Request:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "TEST-002",
    "productName": "Test Product",
    "productType": "LOAN",
    "currency": "USD",
    "interestType": "COMPOUND",
    "compoundingFrequency": "WEEKLY",
    "efctv_date": "2025-01-01"
  }'
```

**Expected Response (400 Bad Request):**
```json
{
  "error": "Validation failed",
  "message": "Invalid enum value: No enum constant com.lab.product.entity.ENUMS.COMPOUNDING_FREQUENCY.WEEKLY",
  "timestamp": "2025-10-15T10:33:00"
}
```

---

### 8. Case Sensitivity Test

**Request:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "TEST-003",
    "productName": "Test Product",
    "productType": "LOAN",
    "currency": "USD",
    "interestType": "compound",
    "compoundingFrequency": "monthly",
    "efctv_date": "2025-01-01"
  }'
```

**Expected Response (400 Bad Request):**
```json
{
  "error": "Validation failed",
  "message": "Invalid enum value: No enum constant com.lab.product.entity.ENUMS.INTEREST_TYPE.compound",
  "timestamp": "2025-10-15T10:34:00"
}
```

**Note:** Enum values are case-sensitive. Must use uppercase: `COMPOUND`, not `compound`

---

## All Valid Combinations

### Interest Type: SIMPLE
```json
{
  "interestType": "SIMPLE",
  "compoundingFrequency": null
}
```

### Interest Type: COMPOUND with DAILY compounding
```json
{
  "interestType": "COMPOUND",
  "compoundingFrequency": "DAILY"
}
```

### Interest Type: COMPOUND with MONTHLY compounding
```json
{
  "interestType": "COMPOUND",
  "compoundingFrequency": "MONTHLY"
}
```

### Interest Type: COMPOUND with QUARTERLY compounding
```json
{
  "interestType": "COMPOUND",
  "compoundingFrequency": "QUARTERLY"
}
```

### Interest Type: COMPOUND with SEMI_ANNUALLY compounding
```json
{
  "interestType": "COMPOUND",
  "compoundingFrequency": "SEMI_ANNUALLY"
}
```

### Interest Type: COMPOUND with ANNUALLY compounding
```json
{
  "interestType": "COMPOUND",
  "compoundingFrequency": "ANNUALLY"
}
```

---

## Postman Collection Variables

```json
{
  "baseUrl": "http://localhost:8080",
  "productCode": "LOAN-PERSONAL-2025",
  "validInterestTypes": ["SIMPLE", "COMPOUND"],
  "validCompoundingFrequencies": ["DAILY", "MONTHLY", "QUARTERLY", "SEMI_ANNUALLY", "ANNUALLY"]
}
```

---

## Sample Test Data Matrix

| Product Type | Interest Type | Compounding Frequency | Use Case |
|-------------|---------------|----------------------|-----------|
| LOAN | COMPOUND | MONTHLY | Personal Loan |
| LOAN | COMPOUND | QUARTERLY | Business Loan |
| LOAN | SIMPLE | null | Short-term Loan |
| DEPOSIT | SIMPLE | null | Certificate of Deposit |
| DEPOSIT | COMPOUND | QUARTERLY | Fixed Deposit |
| SAVINGS | COMPOUND | DAILY | High Yield Savings |
| SAVINGS | COMPOUND | MONTHLY | Regular Savings |
| CREDIT_CARD | COMPOUND | DAILY | Credit Card Balance |

---

## Integration Test Checklist

- [ ] Create product with SIMPLE interest
- [ ] Create product with COMPOUND interest + DAILY frequency
- [ ] Create product with COMPOUND interest + MONTHLY frequency
- [ ] Create product with COMPOUND interest + QUARTERLY frequency
- [ ] Create product with COMPOUND interest + SEMI_ANNUALLY frequency
- [ ] Create product with COMPOUND interest + ANNUALLY frequency
- [ ] Create product with null interest fields (optional)
- [ ] Update product to change interest type
- [ ] Update product to change compounding frequency
- [ ] Retrieve product and verify interest fields
- [ ] Test invalid interest type rejection
- [ ] Test invalid compounding frequency rejection
- [ ] Test case sensitivity validation
- [ ] Test backward compatibility (without new fields)
