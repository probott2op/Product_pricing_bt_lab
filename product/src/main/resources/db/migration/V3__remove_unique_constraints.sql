-- Remove Unique Constraints for INSERT-ONLY Pattern
-- With INSERT-ONLY pattern, multiple versions of the same entity exist
-- Only primary keys should remain unique
-- Author: System Generated
-- Date: 2024

-- ============================================================
-- Drop unique constraints from interest_rates table
-- ============================================================
ALTER TABLE interest_rates DROP INDEX UK5ucsvg2nhjnuwf48yo88ovy7f;

-- ============================================================
-- Drop unique constraints from products table (productCode)
-- ============================================================
ALTER TABLE products DROP INDEX UK922x4t23nx64422orei4meb2y;

-- ============================================================
-- Drop unique constraints from product_transaction_types
-- ============================================================
ALTER TABLE product_transaction_types DROP INDEX UKe5qc456g6hvs7sam8me13ktpb;

-- ============================================================
-- Drop unique constraints from product_role_types
-- ============================================================
ALTER TABLE product_role_types DROP INDEX UKhvp8fjmgmxwnd678nxmhm3t5b;

-- ============================================================
-- Drop unique constraints from product_communications
-- ============================================================
ALTER TABLE product_communications DROP INDEX UKrjlpm6yc4xacvfomcgensbas7;

-- ============================================================
-- Drop unique constraints from product_balances
-- Note: Skipping UK59f7bh0tb6dxui735avjffrq0 as it's needed for FK constraint
-- This composite unique constraint on (product_id, balance_type) should remain
-- as it involves the foreign key relationship
-- ============================================================
-- ALTER TABLE product_balances DROP INDEX UK59f7bh0tb6dxui735avjffrq0;

