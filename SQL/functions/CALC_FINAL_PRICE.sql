create or replace
function "CALC_FINAL_PRICE"
(quantity in NUMBER,
price in FLOAT,
discount in NUMBER)
return FLOAT
IS
	final_price FLOAT;
BEGIN
	final_price := quantity * price * (1 - (discount / 100));
	RETURN ROUND(final_price, 2);
END;