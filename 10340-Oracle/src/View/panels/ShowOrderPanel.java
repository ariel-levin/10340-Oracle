package view.panels;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
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
public class ShowOrderPanel extends SalePanel {

	private static final long serialVersionUID = 1L;

	private Order order;

	
	public ShowOrderPanel(MainFrame mainFrame, Order order) {

		super(mainFrame, "Order");

		this.order = order;
		mainFrame.getDB().getOrderLines(order);

		initPanel();
		
		loadOrderLines();
		
		table.setEnabled(false);
	}

	
	private void initPanel() {

		setBorder(BorderFactory.createTitledBorder("Order: " + order.getNum()));

		lblNum.setText(lblNum.getText() + order.getNum());
		lblDate.setText(lblDate.getText() + order.getDate());
		lblCustomer.setText(order.getCustomer().toString());
		
		btnCommit.setEnabled(false);
		btnCommit.setVisible(false);
		btnAdd.setEnabled(false);
		btnAdd.setVisible(false);
		btnRem.setEnabled(false);
		btnRem.setVisible(false);
		eastPanel.setVisible(false);
		
		northPanel.remove(northBtnPanel);
		EmptyBorder border = new EmptyBorder(5, 50, 5, 0);
		JLabel lblPrice = new JLabel("Price: " + String.format("%.2f", order.getPrice()) );
		lblPrice.setBorder(border);
		northPanel.add(lblPrice);
		northPanel.add(northBtnPanel);
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

}
