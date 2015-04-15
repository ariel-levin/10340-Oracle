create or replace
TRIGGER  "INVOICE_LINES_FINAL_PRICE" 
BEFORE
insert or update on "INVOICE_LINES"
for each row
begin
DECLARE
	final_price FLOAT;
BEGIN
	SELECT ARIEL.calc_final_price(	:NEW.line_quantity,
									:NEW.line_price,
									:NEW.line_discount	) 
	INTO final_price
	FROM dual;

	:NEW.line_final_price := final_price;
END;
end;