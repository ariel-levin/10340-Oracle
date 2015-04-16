package view.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.InputMismatchException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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
public class NewCustomerForm extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private MainFrame 	mainFrame;
	private JTextField 	txtfname, txtlname, txtid, txtstreet, txtcity, txtphone;
	

	public NewCustomerForm(MainFrame mainFrame) {
		
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
		
		setTitle("New Customer");
		setSize(new Dimension(250,400));
		
		initFrame();
		
		setLocationRelativeTo(null);
		setResizable(false);
		//setAlwaysOnTop(true);
		setVisible(true);
	}
	
	private void initFrame() {

		getContentPane().setLayout(new BorderLayout());
		
		JPanel pnlMain = new JPanel();
		pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.PAGE_AXIS));
		
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));
		
		JLabel lblfname = new JLabel("First Name / Company Name (*)");
		lblfname.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lblfname);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		txtfname = new JTextField();
		txtfname.setAlignmentX(Component.CENTER_ALIGNMENT);
		txtfname.setHorizontalAlignment(JTextField.CENTER);
		pnlMain.add(txtfname);
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));

		JLabel lbllname = new JLabel("Last Name");
		lbllname.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lbllname);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		txtlname = new JTextField();
		txtlname.setAlignmentX(Component.CENTER_ALIGNMENT);
		txtlname.setHorizontalAlignment(JTextField.CENTER);
		pnlMain.add(txtlname);
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));
		
		JLabel lblid = new JLabel("ID (*)");
		lblid.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lblid);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		txtid = new JTextField();
		txtid.setAlignmentX(Component.CENTER_ALIGNMENT);
		txtid.setHorizontalAlignment(JTextField.CENTER);
		pnlMain.add(txtid);
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));
		
		JLabel lblstreet = new JLabel("Street");
		lblstreet.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lblstreet);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		txtstreet = new JTextField();
		txtstreet.setAlignmentX(Component.CENTER_ALIGNMENT);
		txtstreet.setHorizontalAlignment(JTextField.CENTER);
		pnlMain.add(txtstreet);
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));
		
		JLabel lblcity = new JLabel("City");
		lblcity.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lblcity);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		txtcity = new JTextField();
		txtcity.setAlignmentX(Component.CENTER_ALIGNMENT);
		txtcity.setHorizontalAlignment(JTextField.CENTER);
		pnlMain.add(txtcity);
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));
		
		JLabel lblphone = new JLabel("Phone");
		lblphone.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lblphone);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		txtphone = new JTextField();
		txtphone.setAlignmentX(Component.CENTER_ALIGNMENT);
		txtphone.setHorizontalAlignment(JTextField.CENTER);
		pnlMain.add(txtphone);
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));
		
		add(pnlMain, BorderLayout.CENTER);
		
		JPanel pnlBtn = new JPanel();
		JButton btnCommit = new JButton("Commit");
		btnCommit.setPreferredSize(new Dimension(120,25));
		pnlBtn.add(btnCommit);
		pnlBtn.setBorder(new EmptyBorder(0, 20, 5, 20));
		
		add(pnlBtn, BorderLayout.SOUTH);
		
		add(Box.createRigidArea(new Dimension(15,0)), BorderLayout.EAST);
		add(Box.createRigidArea(new Dimension(15,0)), BorderLayout.WEST);
		
		btnCommit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Customer c = getCustomer();
				if (c != null) {
					boolean success = mainFrame.getDB().addNewCustomer(c);
					if (success) {
						String msg = "The Customer was added successfully";
						JOptionPane.showMessageDialog(null, msg, "Success",JOptionPane.INFORMATION_MESSAGE);
						dispose();
					}
				}				
			}
		});
	}
	
	private Customer getCustomer() {

		String fname = null, lname = null, street = null, city = null, phone = null;
		int id = 0;
		
		try {
			fname = txtfname.getText();
			if (fname.isEmpty())
				throw new Exception();
		} catch (Exception e) {
			String msg = "ERROR: First Name / Company Name";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		try {
			if (!txtlname.getText().isEmpty())
				lname = txtlname.getText();
		} catch (Exception e) {
			String msg = "ERROR: Last Name";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		try {
			if (txtid.getText().isEmpty())
				throw new InputMismatchException("Must have an ID");
			
			id = Integer.parseInt(txtid.getText());
			
			if (txtid.getText().length() != 9)
				throw new InputMismatchException("ID must be 9 numbers");
			
		} catch (NumberFormatException e) {
			String msg = "ERROR: ID is not a number";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		} catch (InputMismatchException e) {
			String msg = "ERROR: " + e.getMessage();
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		try {
			if (!txtstreet.getText().isEmpty())
				street = txtstreet.getText();
		} catch (Exception e) {
			String msg = "ERROR: Street";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		try {
			if (!txtcity.getText().isEmpty())
				city = txtcity.getText();
		} catch (Exception e) {
			String msg = "ERROR: City";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		try {
			if (!txtphone.getText().isEmpty()) {
				phone = txtphone.getText();
				
				if (phone.length() < 9 || phone.length() > 11)
					throw new InputMismatchException("Phone length is too short or too long");
			}
		} catch (InputMismatchException e) {
			String msg = "ERROR: " + e.getMessage();
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		return new Customer(fname, lname, id, street, city, phone);
	}
	
}

