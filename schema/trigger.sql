-- Logitaulu tilauksen tapahtumia varten, kuten tilauksen luonti, tilausrivin lisäys, päivitys ja poisto
CREATE TABLE IF NOT EXISTS order_log
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    order_id     INT            NOT NULL,
    event_type   VARCHAR(50)    NOT NULL,
    total_amount DECIMAL(18, 2) NOT NULL,
    logged_at    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_log_order
        FOREIGN KEY (order_id) REFERENCES orders (id)
);

DELIMITER $$

-- AI generated procedure
DROP PROCEDURE IF EXISTS recompute_order_total $$
CREATE PROCEDURE recompute_order_total(IN p_order_id INT, OUT p_total DECIMAL(18,2))
BEGIN
    SELECT COALESCE(SUM(oi.quantity * COALESCE(oi.discounted_price, oi.unit_price)), 0.00)
    INTO p_total
    FROM orderitems oi
    WHERE oi.order_id = p_order_id;
END$$

-- Loggaa tilaus sen tekohetkellä
-- yleensä hinta ei vielä tiedossa, mutta voidaan laittaa 0.00 tai NULL ja päivittää myöhemmin
DROP TRIGGER IF EXISTS trg_orders_log $$
CREATE TRIGGER trg_orders_log
    AFTER INSERT ON orders
    FOR EACH ROW
BEGIN
    INSERT INTO order_log (order_id, event_type, total_amount, logged_at)
    VALUES (NEW.id, 'ORDER_CREATED', 0.00, NOW());
END$$

-- Loggaa tilausrivin lisäys ja päivitä tilauksen kokonaissumma
DROP TRIGGER IF EXISTS trg_orderitems_log_ai $$
CREATE TRIGGER trg_orderitems_log_ai
    AFTER INSERT ON orderitems
    FOR EACH ROW
BEGIN
    DECLARE v_total DECIMAL(18,2);
    CALL recompute_order_total(NEW.order_id, v_total);

    INSERT INTO order_log (order_id, event_type, total_amount, logged_at)
    VALUES (NEW.order_id, 'ORDERITEM_ADDED', v_total, NOW());
END$$

-- Loggaa tilausrivin päivitys ja päivitä tilauksen kokonaissumma
DROP TRIGGER IF EXISTS trg_oi_log_update $$
CREATE TRIGGER trg_oi_log_update
    AFTER UPDATE ON orderitems
    FOR EACH ROW
BEGIN
    DECLARE v_total_new DECIMAL(18,2);
    DECLARE v_total_old DECIMAL(18,2);

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
    AFTER DELETE ON orderitems
    FOR EACH ROW
BEGIN
    DECLARE v_total DECIMAL(18,2);
    CALL recompute_order_total(OLD.order_id, v_total);

    INSERT INTO order_log (order_id, event_type, total_amount, logged_at)
    VALUES (OLD.order_id, 'ORDERITEM_DELETED', v_total, NOW());
END$$

DELIMITER ;