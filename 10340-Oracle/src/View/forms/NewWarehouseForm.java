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
public class NewWarehouseForm extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private MainFrame mainFrame;
	private JTextField txtname, txtstreet, txtcity, txtphone;
	

	public NewWarehouseForm(MainFrame mainFrame) {
		
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
		
		setTitle("New Warehouse");
		setSize(new Dimension(250,280));
		
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
		
		JLabel lblname = new JLabel("Warehouse Name (*)");
		lblname.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lblname);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		txtname = new JTextField();
		txtname.setAlignmentX(Component.CENTER_ALIGNMENT);
		txtname.setHorizontalAlignment(JTextField.CENTER);
		pnlMain.add(txtname);
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
				Warehouse wh = getWarehouse();
				if (wh != null) {
					boolean success = mainFrame.getDB().addNewWarehouse(wh);
					if (success) {
						String msg = "The Warehouse was added successfully";
						JOptionPane.showMessageDialog(null, msg, "Success",JOptionPane.INFORMATION_MESSAGE);
						dispose();
					}
				}				
			}
		});
	}
	
	private Warehouse getWarehouse() {

		String name = null, street = null, city = null, phone = null;
		
		try {
			name = txtname.getText();
			if (name.isEmpty())
				throw new Exception();
		} catch (Exception e) {
			String msg = "ERROR: Warehouse Name";
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
		
		return new Warehouse(name, street, city, phone);
	}
	
}

