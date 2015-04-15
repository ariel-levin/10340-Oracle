create or replace
TRIGGER  "ORDERS_LINES_STOCK_CHECK" 
BEFORE
insert or update on "ORDERS_LINES"
for each row
begin
DECLARE
	check_stock NUMBER := 0;
	db_quantity NUMBER(32) := 0;
BEGIN
	SELECT ARIEL.check_item_stock(:NEW.item_num,:NEW.line_quantity)
	INTO check_stock
	FROM dual;

	IF (check_stock = 0) THEN
		raise_application_error (-20001,'Item Num: ' || :NEW.item_num 
				|| ' >> not enough in stock..');
	END IF;
END;
end;