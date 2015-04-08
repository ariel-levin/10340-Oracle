package view.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

import view.MainFrame;
import model.*;


public class NewInvoicePanel extends SalePanel {

	private static final long serialVersionUID = 1L;

	private Invoice invoice;

	
	public NewInvoicePanel(MainFrame mainFrame) {

		super(mainFrame, "Invoice");

		int invoice_num = mainFrame.getDB().getCurrentInvoiceNum();
		this.invoice = mainFrame.getDB().getInvoiceByNum(invoice_num);

		initPanel();
	}

	
	private void initPanel() {

		setBorder(BorderFactory.createTitledBorder("New Invoice: " + invoice.getNum()));

		lblNum.setText(lblNum.getText() + invoice.getNum());
		lblDate.setText(lblDate.getText() + invoice.getDate());
		lblCustomer.setText(invoice.getCustomer().toString());

		btnCommit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				commitInvoice();
			}
		});

	}

	private void commitInvoice() {

		for (int i = 0; i < tableModel.getRowCount(); i++) {

			if (tableModel.getValueAt(i, ITEM_COL) != null) {

				Item item = (Item) tableModel.getValueAt(i, ITEM_COL);
				int quantity = (int) tableModel.getValueAt(i, QUANTITY_COL);
				float price = (float) tableModel.getValueAt(i, PRICE_COL);
				int discount = (int) tableModel.getValueAt(i, DISCOUNT_COL);
				;
				float fprice = (float) tableModel.getValueAt(i, FPRICE_COL);
				;

				invoice.addLine(new InvoiceLine(i + 1, item, quantity, price, discount, fprice));
			}
		}

		boolean success = mainFrame.getDB().addInvoiceLines(invoice);

		if (success) {
			String msg = "Invoice commited successfully";
			JOptionPane.showMessageDialog(null, msg, "Success",JOptionPane.INFORMATION_MESSAGE);
		}

		mainFrame.removePanel();
	}

}
