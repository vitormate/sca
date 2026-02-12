ALTER TABLE movement
ADD COLUMN product_name VARCHAR(150);

ALTER TABLE products
ALTER COLUMN description TYPE VARCHAR(150);

ALTER TABLE products
RENAME COLUMN description TO name;

