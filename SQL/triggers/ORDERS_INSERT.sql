create or replace
trigger "ORDERS_INSERT"  
	before insert on "ORDERS"              
	for each row 
begin  
	if :NEW."ORDER_NUM" is null then
		select "ORDERS_SEQ".nextval into :NEW."ORDER_NUM" from dual;
	end if;
  
	:NEW."ORDER_DATE" := ARIEL.get_date();
	:NEW."ORDER_PRICE" := 0;
	:NEW."ORDER_STATUS" := 'open';
end;