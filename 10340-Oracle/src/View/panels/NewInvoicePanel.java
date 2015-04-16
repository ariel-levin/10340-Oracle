package view.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

import model.*;
import view.MainFrame;


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

		if (invoice.getOrder() != null) {
			northPanel.remove(northBtnPanel);
			EmptyBorder border = new EmptyBorder(5, 50, 5, 0);
			JLabel lblOrder = new JLabel("Order: " + invoice.getOrder().getNum());
			lblOrder.setBorder(border);
			northPanel.add(lblOrder);
			northPanel.add(northBtnPanel);
		}
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
		invoice.removeLines();

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
		
		Item itemNotEnoughInStock = mainFrame.getDB().checkInvoiceLinesInStock(invoice);
		if (itemNotEnoughInStock != null) {
			String msg = "ERROR: Item " + itemNotEnoughInStock + " >> Not enough in stock..";
			JOptionPane.showMessageDialog(null, msg, "Stock Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		invoice.setPrice(invoice_price * VAT);
		
		boolean success1 = mainFrame.getDB().addInvoiceLines(invoice);

		if (success1) {
			boolean success2 = mainFrame.getDB().updateInvoicePrice(invoice.getNum());
			
			if (success2) {
				String msg = "The Invoice was commited successfully";
				JOptionPane.showMessageDialog(null, msg, "Success",JOptionPane.INFORMATION_MESSAGE);
				mainFrame.removePanel();		
			}

		}

	}

}
