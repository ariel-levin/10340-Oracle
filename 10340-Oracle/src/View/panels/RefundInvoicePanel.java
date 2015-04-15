package view.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

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
public class RefundInvoicePanel extends SalePanel {

	private static final long serialVersionUID = 1L;

	private Invoice invoice, refundInvoice;

	
	public RefundInvoicePanel(MainFrame mainFrame, Invoice refundInvoice) {

		super(mainFrame, "Invoice");

		this.refundInvoice = mainFrame.getDB().getInvoiceByNum(refundInvoice.getNum());
		
		int invoice_num = mainFrame.getDB().getCurrentInvoiceNum();
		this.invoice = mainFrame.getDB().getInvoiceByNum(invoice_num);
		
		this.invoice.setPrice(-refundInvoice.getPrice());

		initPanel();
		loadInvoiceLines();
		
		table.setEnabled(false);
	}

	
	private void initPanel() {

		setBorder(BorderFactory.createTitledBorder("New Refund Invoice: " + invoice.getNum()));

		lblNum.setText("Invoice Number: " + invoice.getNum());
		lblDate.setText(lblDate.getText() + invoice.getDate());
		lblCustomer.setText(invoice.getCustomer().toString());

		btnCommit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				commitInvoice();
			}
		});

		btnAdd.setEnabled(false);
		btnAdd.setVisible(false);
		btnRem.setEnabled(false);
		btnRem.setVisible(false);
		eastPanel.setVisible(false);
		
		northPanel.remove(northBtnPanel);
		EmptyBorder border = new EmptyBorder(5, 50, 5, 0);
		JLabel lblPrice = new JLabel("Price: " + invoice.getPrice());
		lblPrice.setBorder(border);
		northPanel.add(lblPrice);
		JLabel lblRefund = new JLabel("Refunding Invoice Number: " + refundInvoice.getNum());
		lblRefund.setBorder(border);
		northPanel.add(lblRefund);
		northPanel.add(northBtnPanel);

	}
	
	private void loadInvoiceLines() {
		tableModel.removeRow(0);
		ArrayList<InvoiceLine> list = refundInvoice.getLines();
		
		for (InvoiceLine line : list) {
			tableModel.addRow(new Object[] { line.getItem(),
					-line.getQuantity(), line.getPrice(), line.getDiscount(),
					-line.getFinalPrice() });
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
				String msg = "Source Invoice: " + refundInvoice.getNum() + " was refunded successfully\n"
						+ "by Current Invoice: " + invoice.getNum();
				JOptionPane.showMessageDialog(null, msg, "Success",JOptionPane.INFORMATION_MESSAGE);
			}
		}

		mainFrame.removePanel();
	}

}
