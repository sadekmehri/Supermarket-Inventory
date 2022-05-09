use commerce;

DROP PROCEDURE IF EXISTS `process_order_verification`;
DELIMITER $$
CREATE PROCEDURE `process_order_verification`(IN store_id INT, IN product_id INT)
BEGIN
    DECLARE nbrLignes INTEGER DEFAULT 0;

    -- Check if there is a record that matches our conditions
    SELECT count(*) as nbrRecords
    INTO nbrLignes
    FROM stocks s
    WHERE s.product_id = product_id
      AND s.store_id = store_id;

    IF nbrLignes = 0 THEN
        SIGNAL SQLSTATE '12001' SET MESSAGE_TEXT = 'No data found';
    END IF;

END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS `payment_process`;
DELIMITER $$
CREATE PROCEDURE `payment_process`(IN `order_id` INT, IN `store_id` INT, IN `product_id` INT, IN `quantity` INT)
BEGIN

    -- Check if the current quantity in stock is sufficient
    DECLARE productPrice INTEGER DEFAULT 0;
    DECLARE nbrProductInStock INTEGER DEFAULT 0;

    -- Check if the given quantity is positive
    IF quantity <= 0 THEN
        SIGNAL SQLSTATE '12000' SET MESSAGE_TEXT = 'Quantity value is negative';
    END IF;

    SELECT s.quantity
    INTO nbrProductInStock
    FROM stocks s
    WHERE s.product_id = product_id
      AND s.store_id = store_id;

    -- Check if we subtract the given quantity then whe we got negative  result or not
    IF nbrProductInStock - quantity < 0 THEN
        SIGNAL SQLSTATE '12001' SET MESSAGE_TEXT = 'Stock insufficient';
    END IF;

    SELECT p.product_price
    INTO productPrice
    FROM products p
    WHERE p.product_id = product_id;

    UPDATE stocks s
    SET s.quantity = s.quantity - quantity
    WHERE s.product_id = product_id
      AND s.store_id = store_id;

    INSERT INTO order_item (order_id, product_id, quantity, price)
    VALUES (order_id, product_id, quantity, productPrice);
END $$
DELIMITER ;