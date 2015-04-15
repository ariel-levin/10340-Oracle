create or replace
trigger "TRANSACTIONS_NUM"  
  before insert on "TRANSACTIONS"              
  for each row 
begin  
  if :NEW."TRANSACTION_NUM" is null then
    select "TRANSACTIONS_SEQ".nextval into :NEW."TRANSACTION_NUM" from dual;
  end if;
end;