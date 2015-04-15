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
public class UpdateOrderPanel extends SalePanel {

	private static final long serialVersionUID = 1L;

	private Order order;

	
	public UpdateOrderPanel(MainFrame mainFrame, Order order) {

		super(mainFrame, "Order");

		this.order = order;
		mainFrame.getDB().getOrderLines(order);

		initPanel();
		
		loadOrderLines();
	}

	
	private void initPanel() {

		setBorder(BorderFactory.createTitledBorder("Order: " + order.getNum()));

		lblNum.setText(lblNum.getText() + order.getNum());
		lblDate.setText(lblDate.getText() + order.getDate());
		lblCustomer.setText(order.getCustomer().toString());
		
		btnCommit.setText("Update Order");

		btnCommit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateOrder();
			}
		});

	}
	
	private void loadOrderLines() {
		tableModel.removeRow(0);
		ArrayList<OrderLine> list = order.getLines();
		
		for (OrderLine line : list) {
			tableModel.addRow(new Object[] { line.getItem(),
					line.getQuantity(), line.getPrice(), line.getDiscount(),
					line.getFinalPrice() });
		}
	}

	private void updateOrder() {

		if (tableModel.getRowCount() < 1) {
			String msg = "ERROR: Order with no lines";
			JOptionPane.showMessageDialog(null, msg, "Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		order.removeLines();

		float order_price = 0;
		int line_num = 1;
		
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

		boolean success1 = mainFrame.getDB().deleteOrderLines(order);
		
		if (success1) {
			boolean success2 = mainFrame.getDB().addOrderLines(order);
			
			if (success2) {
				boolean success3 = mainFrame.getDB().updateOrderPrice(order.getNum());
				
				if (success3) {
					String msg = "The Order was updated successfully";
					JOptionPane.showMessageDialog(null, msg, "Success",JOptionPane.INFORMATION_MESSAGE);
					mainFrame.removePanel();
				}
			}
		}

	}

}
