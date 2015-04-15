package view.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

		setBorder(BorderFactory.createTitledBorder("New Order: " + order.getNum()));

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

		if (tableModel.getRowCount() < 1) {
			String msg = "ERROR: Order with no lines";
			JOptionPane.showMessageDialog(null, msg, "Error",JOptionPane.ERROR_MESSAGE);
			return;
		}

		float order_price = 0;
		int line_num = 1;
		order.removeLines();
		
		for (int i = 0; i < tableModel.getRowCount(); i++) {

			if (tableModel.getValueAt(i, ITEM_COL) != null) {

				Item item = (Item) tableModel.getValueAt(i, ITEM_COL);
				int quantity = (int) tableModel.getValueAt(i, QUANTITY_COL);
				float price = (float) tableModel.getValueAt(i, PRICE_COL);
				int discount = (int) tableModel.getValueAt(i, DISCOUNT_COL);
				float fprice = (float) tableModel.getValueAt(i, FPRICE_COL);

				order.addLine(new OrderLine(line_num++, item, quantity, price, discount, fprice));
				
				order_price += fprice;
			}
		}
		
		Item itemNotEnoughInStock = mainFrame.getDB().checkOrderLinesInStock(order);
		if (itemNotEnoughInStock != null) {
			String msg = "ERROR: Item " + itemNotEnoughInStock + " >> Not enough in stock..";
			JOptionPane.showMessageDialog(null, msg, "Stock Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		order.setPrice(order_price * VAT);

		boolean success1 = mainFrame.getDB().addOrderLines(order);
		
		if (success1) {
			
			boolean success2 = mainFrame.getDB().updateOrderPrice(order.getNum());
			
			if (success2) {
				String msg = "The Order was commited successfully";
				JOptionPane.showMessageDialog(null, msg, "Success",JOptionPane.INFORMATION_MESSAGE);
				mainFrame.removePanel();
			}
		}
	}

}
