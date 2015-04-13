package view.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

import view.MainFrame;
import model.*;


/**
 * 
 * @author 	Ariel Levin
 * 			<br/><a href="http://about.me/ariel.levin">about.me/ariel.levin</a>
 * 			<br/><a href="mailto:ariel.lvn89@gmail.com">ariel.lvn89@gmail.com</a><br/><br/>
 * 
 * 			Matan Shulman
 * 			<br/><a href="mailto:matan.shulman87@gmail.com">matan.shulman87@gmail.com</a>
 *
 */
public class NewInvoicePanel extends SalePanel {

	private static final long serialVersionUID = 1L;

	private Invoice invoice;

	
	public NewInvoicePanel(MainFrame mainFrame) {

		super(mainFrame, "Invoice");

		int invoice_num = mainFrame.getDB().getCurrentInvoiceNum();
		this.invoice = mainFrame.getDB().getInvoiceByNum(invoice_num);

		initPanel();
		
		if (invoice.getOrder() != null)
			loadOrderLines();
	}

	
	private void initPanel() {

		setBorder(BorderFactory.createTitledBorder("New Invoice: " + invoice.getNum()));

		lblNum.setText("Invoice Number: " + invoice.getNum());
		lblDate.setText(lblDate.getText() + invoice.getDate());
		lblCustomer.setText(invoice.getCustomer().toString());

		btnCommit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				commitInvoice();
			}
		});

	}
	
	private void loadOrderLines() {
		tableModel.removeRow(0);
		ArrayList<OrderLine> list = invoice.getOrder().getLines();
		
		for (OrderLine line : list) {
			tableModel.addRow(new Object[] { line.getItem(),
					line.getQuantity(), line.getPrice(), line.getDiscount(),
					line.getFinalPrice() });
		}
	}

	private void commitInvoice() {
		
		if (tableModel.getRowCount() < 1) {
			String msg = "ERROR: Invoice with no lines";
			JOptionPane.showMessageDialog(null, msg, "Error",JOptionPane.ERROR_MESSAGE);
			return;
		}

		float invoice_price = 0;
		int line_num = 1;

		for (int i = 0; i < tableModel.getRowCount(); i++) {

			if (tableModel.getValueAt(i, ITEM_COL) != null) {

				Item item = (Item) tableModel.getValueAt(i, ITEM_COL);
				int quantity = (int) tableModel.getValueAt(i, QUANTITY_COL);
				float price = (float) tableModel.getValueAt(i, PRICE_COL);
				int discount = (int) tableModel.getValueAt(i, DISCOUNT_COL);
				float fprice = (float) tableModel.getValueAt(i, FPRICE_COL);

				invoice.addLine(new InvoiceLine(line_num++, item, quantity, price, discount, fprice));
				
				invoice_price += fprice;
			}
		}
		
		invoice.setPrice(invoice_price);
		
		boolean success1 = mainFrame.getDB().addInvoiceLines(invoice);
		boolean success2 = mainFrame.getDB().updateInvoicePrice(invoice.getNum(), invoice_price);
		boolean success3 = true;
		
		if (invoice.getOrder() != null)
			success3 = mainFrame.getDB().closeOrder(invoice.getOrder().getNum());

		if (success1 && success2 && success3) {
			String msg = "The Invoice was commited successfully";
			JOptionPane.showMessageDialog(null, msg, "Success",JOptionPane.INFORMATION_MESSAGE);
		} else {
			String msg = "Some error occurred...";
			JOptionPane.showMessageDialog(null, msg, "Error",JOptionPane.ERROR_MESSAGE);
		}

		mainFrame.removePanel();
	}

}
