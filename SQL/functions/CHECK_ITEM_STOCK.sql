create or replace
function "CHECK_ITEM_STOCK"
(item in items.item_num%TYPE,
quantity in NUMBER)
return NUMBER
IS
	db_quantity NUMBER := 0; 
BEGIN
	SELECT NVL(SUM(stock_quantity),0)
	INTO db_quantity
	FROM stock
	WHERE stock.item_num = item;

	IF quantity > db_quantity THEN
		RETURN 0;
	ELSE
		RETURN 1;
	END IF;
END;