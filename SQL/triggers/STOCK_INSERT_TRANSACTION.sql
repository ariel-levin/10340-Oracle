create or replace
TRIGGER  "STOCK_INSERT_TRANSACTION" 
AFTER
insert on "STOCK"
for each row
begin
DECLARE
	trans_type VARCHAR2(10);
BEGIN
	IF ( :NEW.stock_quantity < 0 ) THEN
	trans_type := 'debit';
	ELSE
		trans_type := 'credit';
	END IF;

	INSERT INTO "ARIEL"."TRANSACTIONS" (TRANSACTION_DATE, ITEM_NUM, TRANSACTION_QUANTITY, TRANSACTION_TYPE, WH_NUM) 
	VALUES (ARIEL.get_date(), 
		:NEW.item_num, 
		:NEW.stock_quantity,
		trans_type, 
		:NEW.wh_num);
END;
end;