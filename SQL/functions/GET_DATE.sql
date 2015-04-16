create or replace
function "GET_DATE"
return DATE
IS
BEGIN
	RETURN TO_DATE(SYSDATE, 'DD/MM/rr');
END;