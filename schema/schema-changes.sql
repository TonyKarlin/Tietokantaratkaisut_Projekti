DROP TABLE IF EXISTS contacts;

CREATE TABLE addresses
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    street_address VARCHAR(255) NOT NULL,
    postal_code    VARCHAR(20),
    city           VARCHAR(100) NOT NULL,
    country        VARCHAR(100) NOT NULL
);

CREATE TABLE customeraddresses
(
    id          INT PRIMARY KEY,
    customer_id INT NOT NULL,
    FOREIGN KEY (id) REFERENCES addresses (id),
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);

CREATE TABLE supplieraddresses
(
    id          INT PRIMARY KEY,
    supplier_id INT NOT NULL,
    FOREIGN KEY (id) REFERENCES addresses (id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers (id)
);

# Adding discounted_price column to orderitems table
# This column will store the price of the item after applying any discounts.
# And keep the original price in the price column for reference.
ALTER TABLE orderitems ADD COLUMN discounted_price DECIMAL(10,2);