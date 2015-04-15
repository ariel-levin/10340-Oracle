create or replace
function "CALC_VAT"
(num in FLOAT)
return FLOAT
IS
BEGIN
	RETURN ROUND(num * 1.18 , 2);
END;