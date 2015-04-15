create or replace
trigger "WAREHOUSES_NUM"  
  before insert on "WAREHOUSES"              
  for each row 
begin  
  if :NEW."WH_NUM" is null then
    select "WAREHOUSES_SEQ".nextval into :NEW."WH_NUM" from dual;
  end if;
end;