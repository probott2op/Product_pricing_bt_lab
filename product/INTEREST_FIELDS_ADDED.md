# Interest Type and Compounding Frequency Fields Added

## Summary
Added two new fields to the Product entity and related DTOs to support interest calculation configuration:
- `interestType` - Defines whether interest is SIMPLE or COMPOUND
- `compoundingFrequency` - Defines how often interest compounds (DAILY, MONTHLY, QUARTERLY, SEMI_ANNUALLY, ANNUALLY)

---

## New Enums Created

### 1. INTEREST_TYPE
**Location:** `src/main/java/com/lab/product/entity/ENUMS/INTEREST_TYPE.java`

```java
public enum INTEREST_TYPE {
    SIMPLE,    // Simple interest - calculated only on principal
    COMPOUND   // Compound interest - calculated on principal + accumulated interest
}
```

### 2. COMPOUNDING_FREQUENCY
**Location:** `src/main/java/com/lab/product/entity/ENUMS/COMPOUNDING_FREQUENCY.java`

```java
public enum COMPOUNDING_FREQUENCY {
    DAILY,          // Interest compounded daily
    MONTHLY,        // Interest compounded monthly
    QUARTERLY,      // Interest compounded quarterly (every 3 months)
    SEMI_ANNUALLY,  // Interest compounded semi-annually (every 6 months)
    ANNUALLY        // Interest compounded annually (once per year)
}
```

---

## Files Modified

### 1. Entity Layer

#### PRODUCT_DETAILS.java
**Changes:**
- Added imports for new enums
- Added two new fields:

```java
@Enumerated(EnumType.STRING)
@Column(name = "INTEREST_TYPE")
private INTEREST_TYPE interestType;

@Enumerated(EnumType.STRING)
@Column(name = "COMPOUNDING_FREQUENCY")
private COMPOUNDING_FREQUENCY compoundingFrequency;
```

**Database Columns Created:**
- `INTEREST_TYPE` VARCHAR - stores enum value
- `COMPOUNDING_FREQUENCY` VARCHAR - stores enum value

---

### 2. DTO Layer

#### CreateOrUpdateProductRequestDTO.java
**Changes:**
- Added imports for new enums
- Added two new request fields:

```java
private String interestType;
private String compoundingFrequency;
```

- Updated validation method to validate new enum fields:

```java
if (interestType != null) {
    INTEREST_TYPE.valueOf(interestType);
}
if (compoundingFrequency != null) {
    COMPOUNDING_FREQUENCY.valueOf(compoundingFrequency);
}
```

**Request Example:**
```json
{
  "productCode": "LOAN001",
  "productName": "Personal Loan",
  "productType": "LOAN",
  "currency": "USD",
  "status": "ACTIVE",
  "interestType": "COMPOUND",
  "compoundingFrequency": "MONTHLY",
  "efctv_date": "2025-01-01",
  "expr_date": "2026-12-31"
}
```

#### ProductDetailsDTO.java
**Changes:**
- Added imports for new enums
- Added two new response fields:

```java
private INTEREST_TYPE interestType;
private COMPOUNDING_FREQUENCY compoundingFrequency;
```

**Response Example:**
```json
{
  "productId": "uuid",
  "productCode": "LOAN001",
  "productName": "Personal Loan",
  "productType": "LOAN",
  "currency": "USD",
  "status": "ACTIVE",
  "interestType": "COMPOUND",
  "compoundingFrequency": "MONTHLY",
  "createdAt": "2025-10-15T10:30:00",
  "efctv_date": "2025-01-01",
  "productRules": [...],
  "productCharges": [...],
  "productBalances": [...]
}
```

---

### 3. Mapper Layer

#### ProductMapper.java
**Changes:**
- Updated `toDto()` method to map new fields:

```java
dto.setInterestType(product.getInterestType());
dto.setCompoundingFrequency(product.getCompoundingFrequency());
```

---

### 4. Service Layer

#### ProductServiceImpl.java
**Changes:**
- Added imports for new enums
- Updated `createProduct()` method to handle new fields:

```java
if (requestDTO.getInterestType() != null) {
    entity.setInterestType(INTEREST_TYPE.valueOf(requestDTO.getInterestType()));
}
if (requestDTO.getCompoundingFrequency() != null) {
    entity.setCompoundingFrequency(COMPOUNDING_FREQUENCY.valueOf(requestDTO.getCompoundingFrequency()));
}
```

- Updated `updateProduct()` method to handle new fields:

```java
if (requestDTO.getInterestType() != null) {
    existing.setInterestType(INTEREST_TYPE.valueOf(requestDTO.getInterestType()));
}
if (requestDTO.getCompoundingFrequency() != null) {
    existing.setCompoundingFrequency(COMPOUNDING_FREQUENCY.valueOf(requestDTO.getCompoundingFrequency()));
}
```

---

## Usage Examples

### Example 1: Create Loan Product with Compound Interest
```http
POST /api/products
Content-Type: application/json

{
  "productCode": "LOAN-PERSONAL-01",
  "productName": "Personal Loan - 5 Year",
  "productType": "LOAN",
  "currency": "USD",
  "status": "ACTIVE",
  "interestType": "COMPOUND",
  "compoundingFrequency": "MONTHLY",
  "efctv_date": "2025-01-01"
}
```

### Example 2: Create Fixed Deposit with Simple Interest
```http
POST /api/products
Content-Type: application/json

{
  "productCode": "FD-001",
  "productName": "Fixed Deposit - 1 Year",
  "productType": "DEPOSIT",
  "currency": "USD",
  "status": "ACTIVE",
  "interestType": "SIMPLE",
  "compoundingFrequency": null,
  "efctv_date": "2025-01-01"
}
```

### Example 3: Update Product Interest Configuration
```http
PUT /api/products/LOAN-PERSONAL-01
Content-Type: application/json

{
  "productCode": "LOAN-PERSONAL-01",
  "productName": "Personal Loan - 5 Year",
  "productType": "LOAN",
  "currency": "USD",
  "status": "ACTIVE",
  "interestType": "COMPOUND",
  "compoundingFrequency": "QUARTERLY",
  "efctv_date": "2025-01-01"
}
```

---

## Validation Rules

### Interest Type
- **Optional field** - can be null
- **Valid values:** `SIMPLE`, `COMPOUND`
- Case-sensitive string values in requests
- Stored as enum in database

### Compounding Frequency
- **Optional field** - can be null
- **Valid values:** `DAILY`, `MONTHLY`, `QUARTERLY`, `SEMI_ANNUALLY`, `ANNUALLY`
- Case-sensitive string values in requests
- Stored as enum in database
- Typically only relevant when `interestType = COMPOUND`

### Error Responses

**Invalid Interest Type:**
```json
{
  "error": "Validation failed",
  "message": "Invalid enum value: No enum constant INTEREST_TYPE.INVALID",
  "timestamp": "2025-10-15T10:30:00"
}
```

**Invalid Compounding Frequency:**
```json
{
  "error": "Validation failed",
  "message": "Invalid enum value: No enum constant COMPOUNDING_FREQUENCY.WEEKLY",
  "timestamp": "2025-10-15T10:30:00"
}
```

---

## Business Logic Considerations

### When to Use SIMPLE Interest
- Fixed deposits with simple interest calculation
- Short-term loans where compounding is not applied
- Promotional deposit products

### When to Use COMPOUND Interest
- Savings accounts with regular compounding
- Long-term loans with compound interest
- Investment products
- Credit cards with compound interest on balances

### Compounding Frequency Impact
- **DAILY**: Maximum compounding effect, highest APY
- **MONTHLY**: Common for mortgages and savings accounts
- **QUARTERLY**: Common for some loan products
- **SEMI_ANNUALLY**: Common for some bonds and CDs
- **ANNUALLY**: Least frequent compounding

---

## Database Migration

If migrating existing database, add columns:

```sql
ALTER TABLE products 
ADD COLUMN interest_type VARCHAR(50),
ADD COLUMN compounding_frequency VARCHAR(50);

-- Optional: Add indexes if querying by these fields
CREATE INDEX idx_products_interest_type ON products(interest_type);
CREATE INDEX idx_products_compounding_frequency ON products(compounding_frequency);
```

---

## API Documentation Updates

The Swagger/OpenAPI documentation will automatically include these new fields in:
- Request schemas for POST `/api/products`
- Request schemas for PUT `/api/products/{productCode}`
- Response schemas for all GET endpoints

**Enum values will be documented** showing valid options:
- `interestType`: ["SIMPLE", "COMPOUND"]
- `compoundingFrequency`: ["DAILY", "MONTHLY", "QUARTERLY", "SEMI_ANNUALLY", "ANNUALLY"]

---

## Testing Recommendations

### Unit Tests
1. Test enum validation in service layer
2. Test null handling for optional fields
3. Test invalid enum value rejection

### Integration Tests
1. Create product with all interest configurations
2. Update product interest configuration
3. Retrieve product and verify interest fields
4. Test combinations: SIMPLE with no frequency, COMPOUND with each frequency

### Validation Tests
1. Test invalid interest type values
2. Test invalid compounding frequency values
3. Test case sensitivity of enum values
4. Test null/empty value handling

---

## Files Summary

**Created (2 files):**
1. `INTEREST_TYPE.java` - Interest calculation type enum
2. `COMPOUNDING_FREQUENCY.java` - Compounding frequency enum

**Modified (5 files):**
1. `PRODUCT_DETAILS.java` - Entity with new fields
2. `CreateOrUpdateProductRequestDTO.java` - Request DTO with new fields
3. `ProductDetailsDTO.java` - Response DTO with new fields
4. `ProductMapper.java` - Mapping logic updated
5. `ProductServiceImpl.java` - Service logic updated

**No changes needed:**
- Controller (auto-documented via annotations)
- Repository (JPA handles new columns automatically)

---

## Backward Compatibility

✅ **Fully backward compatible**
- New fields are optional (nullable)
- Existing API calls without these fields will work
- Existing products in database will have NULL values
- No breaking changes to API contracts
