package view.panels;

import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import view.MainFrame;
import model.*;


public class ShowInvoicePanel extends SalePanel {

	private static final long serialVersionUID = 1L;

	private Invoice invoice;

	
	public ShowInvoicePanel(MainFrame mainFrame, Invoice invoice) {

		super(mainFrame, "Invoice");

		this.invoice = mainFrame.getDB().getInvoiceByNum(invoice.getNum());

		initPanel();
		
		loadInvoiceLines();
		
		table.setEnabled(false);
	}

	
	private void initPanel() {

		setBorder(BorderFactory.createTitledBorder("Invoice: " + invoice.getNum()));

		lblNum.setText(lblNum.getText() + invoice.getNum());
		lblDate.setText(lblDate.getText() + invoice.getDate());
		lblCustomer.setText(invoice.getCustomer().toString());
		
		btnCommit.setEnabled(false);
		btnCommit.setVisible(false);
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
		if (invoice.getOrder() != null) {
			JLabel lblOrder = new JLabel("Order: " + invoice.getOrder().getNum());
			lblOrder.setBorder(border);
			northPanel.add(lblOrder);
		}
		northPanel.add(northBtnPanel);
	}
	
	private void loadInvoiceLines() {
		tableModel.removeRow(0);
		ArrayList<InvoiceLine> list = invoice.getLines();
		
		for (InvoiceLine line : list) {
			tableModel.addRow(new Object[] { line.getItem(),
					line.getQuantity(), line.getPrice(), line.getDiscount(),
					line.getFinalPrice() });
		}
	}

}
