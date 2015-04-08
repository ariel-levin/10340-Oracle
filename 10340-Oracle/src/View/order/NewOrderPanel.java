package View.order;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

import Model.*;
import View.MainFrame;


public class NewOrderPanel extends SalePanel {

	private static final long serialVersionUID = 1L;

	private Order order;

	
	public NewOrderPanel(MainFrame mainFrame) {

		super(mainFrame, "Order");

		int order_num = mainFrame.getDB().getCurrentOrderNum();
		this.order = mainFrame.getDB().getOrderByNum(order_num);

		initPanel();
	}

	
	private void initPanel() {

		setBorder(BorderFactory.createTitledBorder("New Order: "
				+ order.getNum()));

		lblNum.setText(lblNum.getText() + order.getNum());
		lblDate.setText(lblDate.getText() + order.getDate());
		lblCustomer.setText(order.getCustomer().toString());

		btnCommit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				commitOrder();
			}
		});

	}

	private void commitOrder() {

		for (int i = 0; i < tableModel.getRowCount(); i++) {

			if (tableModel.getValueAt(i, ITEM_COL) != null) {

				Item item = (Item) tableModel.getValueAt(i, ITEM_COL);
				int quantity = (int) tableModel.getValueAt(i, QUANTITY_COL);
				float price = (float) tableModel.getValueAt(i, PRICE_COL);
				int discount = (int) tableModel.getValueAt(i, DISCOUNT_COL);
				;
				float fprice = (float) tableModel.getValueAt(i, FPRICE_COL);
				;

				order.addLine(new OrderLine(i + 1, item, quantity, price,
						discount, fprice));
			}
		}

		boolean success = mainFrame.getDB().addOrderLines(order);

		if (success) {
			String msg = "Order commited successfully";
			JOptionPane.showMessageDialog(null, msg, "Success",JOptionPane.INFORMATION_MESSAGE);
		}

		mainFrame.removePanel();
	}

}
