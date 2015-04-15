create or replace
trigger "ITEMS_NUM"  
  before insert on "ITEMS"              
  for each row 
begin  
  if :NEW."ITEM_NUM" is null then
    select "ITEMS_SEQ".nextval into :NEW."ITEM_NUM" from dual;
  end if;
end;