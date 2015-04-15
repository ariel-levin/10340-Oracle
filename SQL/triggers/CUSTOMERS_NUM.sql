create or replace
trigger "CUSTOMERS_NUM"  
  before insert on "CUSTOMERS"              
  for each row 
begin  
  if :NEW."CUSTOMER_NUM" is null then
    select "CUSTOMERS_SEQ".nextval into :NEW."CUSTOMER_NUM" from dual;
  end if;
end;