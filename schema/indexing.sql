-- Viiteavaimiin perustuvat indeksit

-- Osoite taulukkojen viiteavaimiin perustuvat indeksit:
CREATE INDEX idx_custadd_cust_id ON CustomerAddresses(customer_id);
CREATE INDEX idx_suppadd_supp_id ON SupplierAddresses(supplier_id);
-- Tuotetaulukon viiteavaimiin perustuvat indeksit:
CREATE INDEX idx_products_category_id ON Products(category_id);
CREATE INDEX idx_products_supplier_id ON Products(supplier_id);
-- Tilaustaulukon viiteavaimiin perustuvat indeksit:
CREATE INDEX idx_orders_customer_id ON Orders(customer_id);
CREATE INDEX idx_orders_shipadd_id ON Orders(shipping_address_id);


-- Muita indeksejä
-- Tilauksen päivämäärään perustuva indeksi:
CREATE INDEX idx_orders_customer_date ON Orders(customer_id, order_date);
-- Tilauksien tiettyihin tuotteisiin perustuva indeksi:
-- Esim kaikki tilakset, joissa on tuote id:llä 5
CREATE INDEX idx_oi_product_id ON OrderItems(product_id);