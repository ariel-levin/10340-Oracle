create or replace
PROCEDURE INVOICE_FINAL_PRICE (i_num NUMBER) IS 
	sum		FLOAT;
	f_price FLOAT;
BEGIN
 	SELECT NVL(SUM(invoice_lines.line_final_price),0)
 	INTO sum
 	FROM invoice_lines
 	WHERE invoice_lines.invoice_num = i_num;

 	SELECT ARIEL.calc_vat(sum)
	INTO f_price
	FROM dual;

 	UPDATE invoice 
 	SET invoice.invoice_price = f_price
	WHERE invoice.invoice_num = i_num;

	BEGIN ARIEL.invoice_close_order(i_num); END;

END INVOICE_FINAL_PRICE;