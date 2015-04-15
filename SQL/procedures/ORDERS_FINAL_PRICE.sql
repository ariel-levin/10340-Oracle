create or replace
PROCEDURE ORDERS_FINAL_PRICE (o_num NUMBER) IS 
	sum		FLOAT;
	f_price FLOAT;
BEGIN
 	SELECT NVL(SUM(orders_lines.line_final_price),0)
 	INTO sum
 	FROM orders_lines
 	WHERE orders_lines.order_num = o_num;

 	SELECT ARIEL.calc_vat(sum)
	INTO f_price
	FROM dual;

 	UPDATE orders 
 	SET orders.order_price = f_price
	WHERE orders.order_num = o_num;

END ORDERS_FINAL_PRICE;