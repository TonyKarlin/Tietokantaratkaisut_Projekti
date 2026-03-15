CREATE OR REPLACE VIEW v_order_totals AS
SELECT
    orders.id                  AS order_id,
    orders.customer_id         AS customer_id,
    orders.order_date          AS order_date,
    orders.delivery_date       AS delivery_date,
    orders.shipping_address_id AS shipping_address_id,
    orders.status              AS status,
    COUNT(oi.product_id)              AS item_lines,
    COALESCE(SUM(oi.quantity), 0)     AS total_quantity,
    COALESCE(SUM(oi.quantity * oi.unit_price), 0) AS total_amount
FROM orders
         LEFT JOIN orderitems oi
                   ON oi.order_id = orders.id
GROUP BY
    orders.id,
    orders.customer_id,
    orders.order_date,
    orders.delivery_date,
    orders.shipping_address_id,
    orders.status;