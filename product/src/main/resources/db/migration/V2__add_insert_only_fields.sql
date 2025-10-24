-- INSERT-ONLY Pattern Migration
-- This migration adds the required fields for INSERT-ONLY audit trail pattern
-- Author: System Generated
-- Date: 2024
-- Note: PRODUCT_CRTN_DATE (createdAt) serves as version timestamp via @CreationTimestamp

-- ============================================================
-- PHASE 1: Add PRODUCT_CODE column to all component tables
-- ============================================================

-- Add product_code to interest_rates (PRODUCT_INTEREST)
ALTER TABLE interest_rates 
ADD COLUMN PRODUCT_CODE VARCHAR(50);

-- Add product_code to product_charges (PRODUCT_CHARGES)
ALTER TABLE product_charges 
ADD COLUMN PRODUCT_CODE VARCHAR(50);

-- Add product_code to product_balances (PRODUCT_BALANCE)
ALTER TABLE product_balances 
ADD COLUMN PRODUCT_CODE VARCHAR(50);

-- Add product_code to PRODUCT_RULES
ALTER TABLE PRODUCT_RULES 
ADD COLUMN PRODUCT_CODE VARCHAR(50);

-- Add product_code to product_transaction_types (PRODUCT_TRANSACTION)
ALTER TABLE product_transaction_types 
ADD COLUMN PRODUCT_CODE VARCHAR(50);

-- Add product_code to product_role_types (PRODUCT_ROLE)
ALTER TABLE product_role_types 
ADD COLUMN PRODUCT_CODE VARCHAR(50);

-- Add product_code to product_communications (PRODUCT_COMMUNICATION)
ALTER TABLE product_communications 
ADD COLUMN PRODUCT_CODE VARCHAR(50);

-- ============================================================
-- PHASE 2: Populate PRODUCT_CODE in existing component records
-- ============================================================

-- Populate product_code in interest_rates from parent product
UPDATE interest_rates ir
SET PRODUCT_CODE = (
    SELECT p.PRODUCT_CODE 
    FROM products p 
    WHERE p.PRODUCT_ID = ir.product_id
)
WHERE ir.PRODUCT_CODE IS NULL;

-- Populate product_code in product_charges from parent product
UPDATE product_charges pc
SET PRODUCT_CODE = (
    SELECT p.PRODUCT_CODE 
    FROM products p 
    WHERE p.PRODUCT_ID = pc.product_id
)
WHERE pc.PRODUCT_CODE IS NULL;

-- Populate product_code in product_balances from parent product
UPDATE product_balances pb
SET PRODUCT_CODE = (
    SELECT p.PRODUCT_CODE 
    FROM products p 
    WHERE p.PRODUCT_ID = pb.product_id
)
WHERE pb.PRODUCT_CODE IS NULL;

-- Populate product_code in PRODUCT_RULES from parent product
UPDATE PRODUCT_RULES pr
SET PRODUCT_CODE = (
    SELECT p.PRODUCT_CODE 
    FROM products p 
    WHERE p.PRODUCT_ID = pr.product_id
)
WHERE pr.PRODUCT_CODE IS NULL;

-- Populate product_code in product_transaction_types from parent product
UPDATE product_transaction_types pt
SET PRODUCT_CODE = (
    SELECT p.PRODUCT_CODE 
    FROM products p 
    WHERE p.PRODUCT_ID = pt.product_id
)
WHERE pt.PRODUCT_CODE IS NULL;

-- Populate product_code in product_role_types from parent product
UPDATE product_role_types pr
SET PRODUCT_CODE = (
    SELECT p.PRODUCT_CODE 
    FROM products p 
    WHERE p.PRODUCT_ID = pr.product_id
)
WHERE pr.PRODUCT_CODE IS NULL;

-- Populate product_code in product_communications from parent product
UPDATE product_communications pc
SET PRODUCT_CODE = (
    SELECT p.PRODUCT_CODE 
    FROM products p 
    WHERE p.PRODUCT_ID = pc.product_id
)
WHERE pc.PRODUCT_CODE IS NULL;

-- ============================================================
-- PHASE 3: Make PRODUCT_CODE NOT NULL in component tables
-- ============================================================

-- Make product_code NOT NULL after population
ALTER TABLE interest_rates 
ALTER COLUMN PRODUCT_CODE SET NOT NULL;

ALTER TABLE product_charges 
ALTER COLUMN PRODUCT_CODE SET NOT NULL;

ALTER TABLE product_balances 
ALTER COLUMN PRODUCT_CODE SET NOT NULL;

ALTER TABLE PRODUCT_RULES 
ALTER COLUMN PRODUCT_CODE SET NOT NULL;

ALTER TABLE product_transaction_types 
ALTER COLUMN PRODUCT_CODE SET NOT NULL;

ALTER TABLE product_role_types 
ALTER COLUMN PRODUCT_CODE SET NOT NULL;

ALTER TABLE product_communications 
ALTER COLUMN PRODUCT_CODE SET NOT NULL;

-- ============================================================
-- PHASE 4: Create indexes for performance
-- ============================================================

-- Index on product_code + crud_value + creation date for products
CREATE INDEX idx_products_code_crud_crtn 
ON products(PRODUCT_CODE, PRODUCT_CRUD_VALUE, PRODUCT_CRTN_DATE DESC);

-- Index on product_code + crud_value for component tables
CREATE INDEX idx_interest_code_crud 
ON interest_rates(PRODUCT_CODE, PRODUCT_CRUD_VALUE);

CREATE INDEX idx_charges_code_crud 
ON product_charges(PRODUCT_CODE, PRODUCT_CRUD_VALUE);

CREATE INDEX idx_balances_code_crud 
ON product_balances(PRODUCT_CODE, PRODUCT_CRUD_VALUE);

CREATE INDEX idx_rules_code_crud 
ON PRODUCT_RULES(PRODUCT_CODE, PRODUCT_CRUD_VALUE);

CREATE INDEX idx_transactions_code_crud 
ON product_transaction_types(PRODUCT_CODE, PRODUCT_CRUD_VALUE);

CREATE INDEX idx_roles_code_crud 
ON product_role_types(PRODUCT_CODE, PRODUCT_CRUD_VALUE);

CREATE INDEX idx_comms_code_crud 
ON product_communications(PRODUCT_CODE, PRODUCT_CRUD_VALUE);

-- ============================================================
-- PHASE 5: Add comments for documentation
-- ============================================================

COMMENT ON COLUMN products.PRODUCT_CRTN_DATE IS 'INSERT-ONLY Pattern: Version timestamp via @CreationTimestamp';
COMMENT ON COLUMN products.PRODUCT_CRUD_VALUE IS 'INSERT-ONLY Pattern: C=Create, U=Update, D=Delete (soft delete)';

COMMENT ON COLUMN interest_rates.PRODUCT_CODE IS 'INSERT-ONLY Pattern: Business identifier for linking across versions';
COMMENT ON COLUMN interest_rates.PRODUCT_CRTN_DATE IS 'INSERT-ONLY Pattern: Version timestamp via @CreationTimestamp';

COMMENT ON COLUMN product_charges.PRODUCT_CODE IS 'INSERT-ONLY Pattern: Business identifier for linking across versions';
COMMENT ON COLUMN product_charges.PRODUCT_CRTN_DATE IS 'INSERT-ONLY Pattern: Version timestamp via @CreationTimestamp';

COMMENT ON COLUMN product_balances.PRODUCT_CODE IS 'INSERT-ONLY Pattern: Business identifier for linking across versions';
COMMENT ON COLUMN product_balances.PRODUCT_CRTN_DATE IS 'INSERT-ONLY Pattern: Version timestamp via @CreationTimestamp';

COMMENT ON COLUMN PRODUCT_RULES.PRODUCT_CODE IS 'INSERT-ONLY Pattern: Business identifier for linking across versions';
COMMENT ON COLUMN PRODUCT_RULES.PRODUCT_CRTN_DATE IS 'INSERT-ONLY Pattern: Version timestamp via @CreationTimestamp';

COMMENT ON COLUMN product_transaction_types.PRODUCT_CODE IS 'INSERT-ONLY Pattern: Business identifier for linking across versions';
COMMENT ON COLUMN product_transaction_types.PRODUCT_CRTN_DATE IS 'INSERT-ONLY Pattern: Version timestamp via @CreationTimestamp';

COMMENT ON COLUMN product_role_types.PRODUCT_CODE IS 'INSERT-ONLY Pattern: Business identifier for linking across versions';
COMMENT ON COLUMN product_role_types.PRODUCT_CRTN_DATE IS 'INSERT-ONLY Pattern: Version timestamp via @CreationTimestamp';

COMMENT ON COLUMN product_communications.PRODUCT_CODE IS 'INSERT-ONLY Pattern: Business identifier for linking across versions';
COMMENT ON COLUMN product_communications.PRODUCT_CRTN_DATE IS 'INSERT-ONLY Pattern: Version timestamp via @CreationTimestamp';
