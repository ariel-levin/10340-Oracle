package view.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import view.MainFrame;
import model.*;


public class NewForm extends JFrame {

	private static final long serialVersionUID = 1L;
	
	protected MainFrame 			mainFrame;
	protected JComboBox<Customer> 	cb_customers;
	protected JButton 				btnCommit;
	

	public NewForm(MainFrame mainFrame, String type) {
		
		this.mainFrame = mainFrame;
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e1) {}
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {


			}
		});
		
		setTitle("New " + type);
		setSize(new Dimension(300,120));
		
		initFrame();
		
		setLocationRelativeTo(null);
		setResizable(false);
		setAlwaysOnTop(true);
		setVisible(true);
	}
	
	private void initFrame() {
		
		ArrayList<Customer> customers = mainFrame.getDB().getAllCustomers();

		cb_customers = new JComboBox<Customer>();

		for (Customer c : customers)
			cb_customers.addItem(c);

		getContentPane().setLayout(new BorderLayout());
		
		JPanel pnlMain = new JPanel();
		pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.PAGE_AXIS));
		
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));
		
		JLabel lblcustomer = new JLabel("Select a Customer");
		lblcustomer.setHorizontalAlignment(JLabel.LEFT);
		pnlMain.add(lblcustomer);
		
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		
		pnlMain.add(cb_customers);
		
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));

		btnCommit = new JButton("Commit");
		
		add(btnCommit, BorderLayout.SOUTH);
		
		add(pnlMain, BorderLayout.CENTER);
		add(Box.createRigidArea(new Dimension(10,0)), BorderLayout.EAST);
		add(Box.createRigidArea(new Dimension(10,0)), BorderLayout.WEST);
		
	}
	
}