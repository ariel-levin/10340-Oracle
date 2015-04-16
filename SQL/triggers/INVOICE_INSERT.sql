create or replace
trigger "INVOICE_INSERT"  
	before insert on "INVOICE"              
	for each row 
begin  
	if :NEW."INVOICE_NUM" is null then
		select "INVOICE_SEQ".nextval into :NEW."INVOICE_NUM" from dual;
	end if;

	:NEW."INVOICE_DATE" := ARIEL.get_date();
	:NEW."INVOICE_PRICE" := 0;
end;