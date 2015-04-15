create or replace
trigger "ORDERS_NUM"  
  before insert on "ORDERS"              
  for each row 
begin  
  if :NEW."ORDER_NUM" is null then
    select "ORDERS_SEQ".nextval into :NEW."ORDER_NUM" from dual;
  end if;
end;