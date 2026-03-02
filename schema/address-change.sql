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