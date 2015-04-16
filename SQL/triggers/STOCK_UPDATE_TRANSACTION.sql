create or replace
TRIGGER  "STOCK_UPDATE_TRANSACTION" 
AFTER
update on "STOCK"
for each row
begin
DECLARE
	trans_type VARCHAR2(10);
BEGIN
	IF ( :OLD.stock_quantity >= :NEW.stock_quantity ) THEN
	trans_type := 'debit';
	ELSE
		trans_type := 'credit';
	END IF;

	INSERT INTO "ARIEL"."TRANSACTIONS" (TRANSACTION_DATE, ITEM_NUM, TRANSACTION_QUANTITY, TRANSACTION_TYPE, WH_NUM) 
	VALUES (ARIEL.get_date(), 
			:NEW.item_num, 
			ABS(:OLD.stock_quantity - :NEW.stock_quantity), 
			trans_type, 
			:OLD.wh_num);
END;
end;