create or replace
TRIGGER  "INVOICE_LINES_STOCK_UPDATE" 
AFTER
insert or update on "INVOICE_LINES"
for each row
BEGIN
DECLARE
	check_stock NUMBER := 0;
	counter NUMBER := :NEW.line_quantity;

	CURSOR c_stock IS
		SELECT *
		FROM stock
		WHERE item_num = :NEW.item_num;

	stock_line c_stock%ROWTYPE;
BEGIN
	SELECT ARIEL.check_item_stock(:NEW.item_num,:NEW.line_quantity)
	INTO check_stock
	FROM dual;

	IF (check_stock = 0) THEN
		raise_application_error (-20001,'Item Num: ' || :NEW.item_num 
				|| ' >> not enough in stock..');
	END IF;

	OPEN c_stock;

	FETCH c_stock INTO stock_line;

	-- taking care of negative quantity
	IF counter <= 0 THEN
		UPDATE stock
		SET stock_quantity = stock_quantity - counter
		WHERE stock.item_num = stock_line.item_num
			AND stock.wh_num = stock_line.wh_num;
	END IF;

	WHILE counter > 0 LOOP
		IF stock_line.stock_quantity >= counter THEN
			UPDATE stock
			SET stock_quantity = stock_quantity - counter
			WHERE stock.item_num = stock_line.item_num
				AND stock.wh_num = stock_line.wh_num;
			counter := 0;
		ELSE
			UPDATE stock
			SET stock_quantity = 0
			WHERE stock.item_num = stock_line.item_num
				AND stock.wh_num = stock_line.wh_num;
			counter := counter - stock_line.stock_quantity;
		END IF;

		FETCH c_stock INTO stock_line;
	END LOOP;

	CLOSE c_stock;
END;
END;