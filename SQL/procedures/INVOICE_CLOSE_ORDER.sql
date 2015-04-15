create or replace
PROCEDURE INVOICE_CLOSE_ORDER (i_num NUMBER) IS
	o_num NUMBER; 
BEGIN
 	SELECT NVL(order_num, -1)
 	INTO o_num
 	FROM invoice
 	WHERE invoice.invoice_num = i_num;

 	IF (o_num != -1) THEN
	 	UPDATE orders 
	 	SET orders.order_status = 'closed'
		WHERE orders.order_num = o_num;
 	END IF;

END INVOICE_CLOSE_ORDER;