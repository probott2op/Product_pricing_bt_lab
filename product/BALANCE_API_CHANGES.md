# Product Balance API - Simplified Implementation

## Overview
The Product Balance API has been **simplified** to match the actual requirement: **tracking which balance types are applicable to each product type**.

## Previous (Incorrect) Implementation
- Complex ledger schema with 14 balance types and 11+ fields
- Fields like: balanceCode, balanceName, description, isMandatory, allowsDebit, allowsCredit, isCalculated, calculationLogic, displayOrder
- Tried to define entire ledger structure and behavior

## Current (Correct) Implementation
- Simple association: **Product → Balance Types**
- Only tracks: `balanceType` and `isActive`
- 6 balance types aligned with business needs

---

## Balance Types Supported

### Enum: `PRODUCT_BALANCE_TYPE`

1. **LOAN_PRINCIPAL** - Principal balance for loans (outstanding loan amount)
2. **LOAN_INTEREST** - Interest balance for loans (accrued or charged interest)
3. **FD_PRINCIPAL** - Principal balance for Fixed Deposits (deposited amount)
4. **FD_INTEREST** - Interest balance for Fixed Deposits (earned interest)
5. **OVERDRAFT** - Overdraft balance (negative balance facility)
6. **PENALTY** - Penalty charges (late payment or violation penalties)

---

## Entity Structure

### `PRODUCT_BALANCE` Entity

```java
@Entity
@Table(name = "product_balances", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "balance_type"}))
public class PRODUCT_BALANCE extends AuditLoggable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID balanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private PRODUCT_DETAILS product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PRODUCT_BALANCE_TYPE balanceType;

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

**Key Features:**
- Unique constraint on (product_id, balance_type) - one entry per balance type per product
- Only 3 meaningful fields: balanceId, balanceType, isActive
- Simple and focused on the core requirement

---

## API Structure

### Base URL
```
/api/products/{productCode}/balances
```

### Endpoints

#### 1. Add Balance Type to Product
```http
POST /api/products/{productCode}/balances
```

**Request Body:**
```json
{
  "balanceType": "LOAN_PRINCIPAL",
  "isActive": true
}
```

**Response:** 201 Created
```json
{
  "balanceId": "uuid",
  "balanceType": "LOAN_PRINCIPAL",
  "isActive": true,
  "createdAt": "2025-10-15T10:30:00"
}
```

#### 2. Get All Balance Types for Product
```http
GET /api/products/{productCode}/balances?page=0&size=10
```

**Response:** 200 OK
```json
{
  "content": [
    {
      "balanceId": "uuid1",
      "balanceType": "LOAN_PRINCIPAL",
      "isActive": true,
      "createdAt": "2025-10-15T10:30:00"
    },
    {
      "balanceId": "uuid2",
      "balanceType": "LOAN_INTEREST",
      "isActive": true,
      "createdAt": "2025-10-15T10:31:00"
    }
  ],
  "pageable": {...},
  "totalElements": 2
}
```

#### 3. Get Specific Balance Type
```http
GET /api/products/{productCode}/balances/{balanceType}
```

Example: `GET /api/products/LOAN001/balances/LOAN_PRINCIPAL`

**Response:** 200 OK
```json
{
  "balanceId": "uuid",
  "balanceType": "LOAN_PRINCIPAL",
  "isActive": true,
  "createdAt": "2025-10-15T10:30:00"
}
```

#### 4. Update Balance Type
```http
PUT /api/products/{productCode}/balances/{balanceType}
```

**Request Body:**
```json
{
  "balanceType": "LOAN_PRINCIPAL",
  "isActive": false
}
```

#### 5. Remove Balance Type from Product
```http
DELETE /api/products/{productCode}/balances/{balanceType}
```

**Response:** 204 No Content

---

## Usage Examples

### Example 1: Loan Product Setup
```bash
# Add LOAN_PRINCIPAL
POST /api/products/LOAN001/balances
{
  "balanceType": "LOAN_PRINCIPAL",
  "isActive": true
}

# Add LOAN_INTEREST
POST /api/products/LOAN001/balances
{
  "balanceType": "LOAN_INTEREST",
  "isActive": true
}

# Add OVERDRAFT
POST /api/products/LOAN001/balances
{
  "balanceType": "OVERDRAFT",
  "isActive": true
}

# Add PENALTY
POST /api/products/LOAN001/balances
{
  "balanceType": "PENALTY",
  "isActive": true
}
```

**Result:** Loan product now supports 4 balance types: LOAN_PRINCIPAL, LOAN_INTEREST, OVERDRAFT, PENALTY

### Example 2: Fixed Deposit Product Setup
```bash
# Add FD_PRINCIPAL
POST /api/products/FD001/balances
{
  "balanceType": "FD_PRINCIPAL",
  "isActive": true
}

# Add FD_INTEREST
POST /api/products/FD001/balances
{
  "balanceType": "FD_INTEREST",
  "isActive": true
}
```

**Result:** FD product now supports 2 balance types: FD_PRINCIPAL, FD_INTEREST

---

## Files Changed

### 1. Enum
- `PRODUCT_BALANCE_TYPE.java` - Reduced from 14 to 6 balance types

### 2. Entity
- `PRODUCT_BALANCE.java` - Simplified from 11+ fields to 3 fields

### 3. DTOs
- `ProductBalanceDTO.java` - Simplified response DTO
- `ProductBalanceRequestDTO.java` - Simplified request DTO (only balanceType + isActive)

### 4. Mapper
- `ProductMapper.java` - Updated `toBalanceDto()` method

### 5. Repository
- `ProductBalanceRepository.java` - Updated methods to use balanceType instead of balanceCode

### 6. Service
- `ProductBalanceService.java` - Updated interface method signatures
- `ProductBalanceServiceImpl.java` - Simplified create/update/delete logic

### 7. Controller
- `ProductBalanceController.java` - Updated all endpoints and documentation

---

## Key Changes Summary

| Aspect | Before | After |
|--------|--------|-------|
| **Balance Types** | 14 generic types | 6 specific types |
| **Entity Fields** | 11+ fields | 3 fields |
| **Unique Key** | product_id + balance_code | product_id + balance_type |
| **Path Variable** | `{balanceCode}` | `{balanceType}` |
| **Request DTO** | 9 fields | 2 fields |
| **Response DTO** | 14 fields | 4 fields |
| **Complexity** | High | Low |
| **Purpose** | Ledger schema definition | Balance type association |

---

## Validation Rules

1. **Uniqueness**: A product cannot have duplicate balance types
2. **Required Fields**: balanceType is mandatory
3. **Valid Enum Values**: balanceType must be one of the 6 defined types
4. **Product Existence**: Product must exist before adding balance types

---

## Database Schema

```sql
CREATE TABLE product_balances (
    balance_id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    balance_type VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP,
    user_id VARCHAR(255),
    local_ts TIMESTAMP,
    -- audit fields from AuditLoggable
    FOREIGN KEY (product_id) REFERENCES product_details(product_id),
    UNIQUE (product_id, balance_type)
);
```

---

## Benefits of This Approach

1. **Simplicity**: Easy to understand - just marks which balance types apply
2. **Clear Purpose**: Directly matches the requirement
3. **Maintainable**: Less code, fewer fields, simpler logic
4. **Flexible**: Easy to add/remove balance types per product
5. **Type-Safe**: Enum ensures only valid balance types are used
6. **Extensible**: New balance types can be added to enum as needed

---

## Migration Notes

If migrating from the previous complex implementation:

1. The unique constraint changed from (product_id, balance_code) to (product_id, balance_type)
2. Multiple fields were removed - data migration may be needed
3. API endpoints changed from `/balances/{balanceCode}` to `/balances/{balanceType}`
4. Repository methods changed from `findByProductAndBalanceCode` to `findByProductAndBalanceType`
