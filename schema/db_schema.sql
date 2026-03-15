USE `tkr-projekti`;

CREATE USER IF NOT EXISTS 'dbuser'@'localhost' IDENTIFIED BY 'tkr-projekti';

GRANT SELECT, INSERT, UPDATE, DELETE, ALTER, CREATE, DROP, INDEX ON `tkr-projekti`.* TO 'dbuser'@'localhost';

FLUSH PRIVILEGES;


DROP TABLE IF EXISTS OrderItems;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS ProductCategories;
DROP TABLE IF EXISTS SupplierAddresses;
DROP TABLE IF EXISTS CustomerAddresses;
DROP TABLE IF EXISTS Suppliers;
DROP TABLE IF EXISTS Customers;

CREATE TABLE Customers
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    phone      VARCHAR(30)
);

CREATE TABLE Suppliers
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    contact_name VARCHAR(100),
    phone        VARCHAR(30),
    email        VARCHAR(255)
);

CREATE TABLE Addresses
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    street_address VARCHAR(255) NOT NULL,
    postal_code    VARCHAR(20),
    city           VARCHAR(100) NOT NULL,
    country        VARCHAR(100) NOT NULL
);

CREATE TABLE CustomerAddresses
(
    id          INT PRIMARY KEY,
    customer_id INT NOT NULL,
    CONSTRAINT fk_customeraddresses_address FOREIGN KEY (id) REFERENCES Addresses (id),
    CONSTRAINT fk_customeraddresses_customer FOREIGN KEY (customer_id) REFERENCES customers (id),
    CONSTRAINT uq_customeraddresses_customer UNIQUE (customer_id)
);

-- SupplierAddresses

CREATE TABLE SupplierAddresses
(
    id          INT PRIMARY KEY,
    supplier_id INT NOT NULL,
    CONSTRAINT fk_supplieraddresses_address FOREIGN KEY (id) REFERENCES Addresses (id),
    CONSTRAINT fk_supplieraddresses_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers (id),
    CONSTRAINT uq_supplieraddresses_supplier UNIQUE (supplier_id)
);

CREATE TABLE ProductCategories
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE Products
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255)   NOT NULL,
    description    TEXT,
    price          DECIMAL(10, 2) NOT NULL,
    stock_quantity INT            NOT NULL DEFAULT 0,
    category_id    INT,
    supplier_id    INT,
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES ProductCategories (id),
    CONSTRAINT fk_product_supplier
        FOREIGN KEY (supplier_id) REFERENCES Suppliers (id)
);

CREATE TABLE Orders
(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    customer_id         INT      NOT NULL,
    order_date          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delivery_date       DATETIME,
    shipping_address_id INT,
    status              VARCHAR(50)       DEFAULT 'NEW',
    CONSTRAINT fk_order_customer
        FOREIGN KEY (customer_id) REFERENCES Customers (id),
    CONSTRAINT fk_order_shipping_address
        FOREIGN KEY (shipping_address_id) REFERENCES CustomerAddresses (id)
);

CREATE TABLE OrderItems
(
    order_id   INT            NOT NULL,
    product_id INT            NOT NULL,
    quantity   INT            NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_orderitem_order
        FOREIGN KEY (order_id) REFERENCES Orders (id),
    CONSTRAINT fk_orderitem_product
        FOREIGN KEY (product_id) REFERENCES Products (id)
);

DROP TABLE IF EXISTS contacts;


# Adding discounted_price column to orderitems table
# This column will store the price of the item after applying any discounts.
# And keep the original price in the price column for reference.
ALTER TABLE OrderItems
    ADD COLUMN discounted_price DECIMAL(10, 2);

-- Viiteavaimiin perustuvat indeksit

-- Osoite taulukkojen viiteavaimiin perustuvat indeksit:
CREATE INDEX idx_custadd_cust_id ON CustomerAddresses (customer_id);
CREATE INDEX idx_suppadd_supp_id ON SupplierAddresses (supplier_id);
-- Tuotetaulukon viiteavaimiin perustuvat indeksit:
CREATE INDEX idx_products_category_id ON Products (category_id);
CREATE INDEX idx_products_supplier_id ON Products (supplier_id);
-- Tilaustaulukon viiteavaimiin perustuvat indeksit:
CREATE INDEX idx_orders_customer_id ON Orders (customer_id);
CREATE INDEX idx_orders_shipadd_id ON Orders (shipping_address_id);


-- Muita indeksejä
-- Tilauksen päivämäärään perustuva indeksi:
CREATE INDEX idx_orders_customer_date ON Orders (customer_id, order_date);
-- Tilauksien tiettyihin tuotteisiin perustuva indeksi:
-- Esim kaikki tilakset, joissa on tuote id:llä 5
CREATE INDEX idx_oi_product_id ON OrderItems (product_id);

CREATE OR REPLACE VIEW v_order_totals AS
SELECT Orders.id                                     AS order_id,
       Orders.customer_id                            AS customer_id,
       Orders.order_date                             AS order_date,
       Orders.delivery_date                          AS delivery_date,
       Orders.shipping_address_id                    AS shipping_address_id,
       Orders.status                                 AS status,
       COUNT(oi.product_id)                          AS item_lines,
       COALESCE(SUM(oi.quantity), 0)                 AS total_quantity,
       COALESCE(SUM(oi.quantity * oi.unit_price), 0) AS total_amount
FROM Orders
         LEFT JOIN OrderItems oi
                   ON oi.order_id = Orders.id
GROUP BY Orders.id,
         Orders.customer_id,
         Orders.order_date,
         Orders.delivery_date,
         Orders.shipping_address_id,
         Orders.status;

-- Logitaulu tilauksen tapahtumia varten, kuten tilauksen luonti, tilausrivin lisäys, päivitys ja poisto
CREATE TABLE IF NOT EXISTS order_log
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    order_id     INT            NOT NULL,
    event_type   VARCHAR(50)    NOT NULL,
    total_amount DECIMAL(18, 2) NOT NULL,
    logged_at    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_log_order
        FOREIGN KEY (order_id) REFERENCES Orders (id)
);

DELIMITER $$

-- AI generated procedure
DROP PROCEDURE IF EXISTS recompute_order_total $$
CREATE PROCEDURE recompute_order_total(IN p_order_id INT, OUT p_total DECIMAL(18, 2))
BEGIN
    SELECT COALESCE(SUM(oi.quantity * COALESCE(oi.discounted_price, oi.unit_price)), 0.00)
    INTO p_total
    FROM OrderItems oi
    WHERE oi.order_id = p_order_id;
END$$

-- Loggaa tilaus sen tekohetkellä
-- yleensä hinta ei vielä tiedossa, mutta voidaan laittaa 0.00 tai NULL ja päivittää myöhemmin
DROP TRIGGER IF EXISTS trg_orders_log $$
CREATE TRIGGER trg_orders_log
    AFTER INSERT
    ON Orders
    FOR EACH ROW
BEGIN
    INSERT INTO order_log (order_id, event_type, total_amount, logged_at)
    VALUES (NEW.id, 'ORDER_CREATED', 0.00, NOW());
END$$

-- Loggaa tilausrivin lisäys ja päivitä tilauksen kokonaissumma
DROP TRIGGER IF EXISTS trg_orderitems_log_ai $$
CREATE TRIGGER trg_orderitems_log_ai
    AFTER INSERT
    ON OrderItems
    FOR EACH ROW
BEGIN
    DECLARE v_total DECIMAL(18, 2);
    CALL recompute_order_total(NEW.order_id, v_total);

    INSERT INTO order_log (order_id, event_type, total_amount, logged_at)
    VALUES (NEW.order_id, 'ORDERITEM_ADDED', v_total, NOW());
END$$

-- Loggaa tilausrivin päivitys ja päivitä tilauksen kokonaissumma
DROP TRIGGER IF EXISTS trg_oi_log_update $$
CREATE TRIGGER trg_oi_log_update
    AFTER UPDATE
    ON OrderItems
    FOR EACH ROW
BEGIN
    DECLARE v_total_new DECIMAL(18, 2);
    DECLARE v_total_old DECIMAL(18, 2);

    CALL recompute_order_total(NEW.order_id, v_total_new);
    INSERT INTO order_log (order_id, event_type, total_amount, logged_at)
    VALUES (NEW.order_id, 'ORDERITEM_UPDATED', v_total_new, NOW());

    IF NEW.order_id <> OLD.order_id THEN
        CALL recompute_order_total(OLD.order_id, v_total_old);
        INSERT INTO order_log (order_id, event_type, total_amount, logged_at)
        VALUES (OLD.order_id, 'ORDERITEM_MOVED_FROM_ORDER', v_total_old, NOW());
    END IF;
END$$

-- Loggaa tilausrivin poisto ja päivitä tilauksen kokonaissumma
DROP TRIGGER IF EXISTS trg_oi_log_remove $$
CREATE TRIGGER trg_oi_log_remove
    AFTER DELETE
    ON OrderItems
    FOR EACH ROW
BEGIN
    DECLARE v_total DECIMAL(18, 2);
    CALL recompute_order_total(OLD.order_id, v_total);

    INSERT INTO order_log (order_id, event_type, total_amount, logged_at)
    VALUES (OLD.order_id, 'ORDERITEM_DELETED', v_total, NOW());
END$$

DELIMITER ;