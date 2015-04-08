package view.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.MainFrame;
import model.*;


public class NewOrderForm extends NewForm {

	private static final long serialVersionUID = 1L;
	

	public NewOrderForm(MainFrame mainFrame) {
		
		super(mainFrame, "Order");
		initFrame();
	}
	
	private void initFrame() {
		
		btnCommit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mainFrame.newOrder( (Customer)cb_customers.getSelectedItem() );
				dispose();
			}
		});
	}
	
}
