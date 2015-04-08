package view.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.MainFrame;
import model.*;


public class NewInvoiceForm extends NewForm {

	private static final long serialVersionUID = 1L;
	

	public NewInvoiceForm(MainFrame mainFrame) {
		
		super(mainFrame, "Invoice");
		initFrame();
	}
	
	private void initFrame() {
		
		btnCommit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mainFrame.newInvoice( (Customer)cb_customers.getSelectedItem() );
				dispose();
			}
		});
	}
	
}
