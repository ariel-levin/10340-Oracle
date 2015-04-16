package view.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.InputMismatchException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class NewStockForm extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private MainFrame 				mainFrame;
	private JTextField 				txtquantity;
	private JComboBox<Item> 		cb_items;
	private JComboBox<Warehouse> 	cb_wh;
	

	public NewStockForm(MainFrame mainFrame) {
		
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
		
		setTitle("New Stock Line");
		setSize(new Dimension(240,230));
		
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
		
		ArrayList<Item> items = mainFrame.getDB().getAllItems();
		cb_items = new JComboBox<Item>();
		for (Item item : items)
			cb_items.addItem(item);
		
		ArrayList<Warehouse> warehouses = mainFrame.getDB().getAllWarehouses();
		cb_wh = new JComboBox<Warehouse>();
		for (Warehouse wh : warehouses)
			cb_wh.addItem(wh);
		
		JLabel lblitem = new JLabel("Item (*)");
		lblitem.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lblitem);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		cb_items.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(cb_items);
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));

		JLabel lblwh = new JLabel("Warehouse (*)");
		lblwh.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lblwh);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		cb_wh.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(cb_wh);
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));
		
		JLabel lblquantity = new JLabel("Quantity (*)");
		lblquantity.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lblquantity);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		txtquantity = new JTextField();
		txtquantity.setAlignmentX(Component.CENTER_ALIGNMENT);
		txtquantity.setHorizontalAlignment(JTextField.CENTER);
		pnlMain.add(txtquantity);
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
				Stock stock = getStock();
				if (stock != null) {
					boolean success = mainFrame.getDB().addNewStockLine(stock);
					if (success) {
						String msg = "The Stock Line was added successfully";
						JOptionPane.showMessageDialog(null, msg, "Success",JOptionPane.INFORMATION_MESSAGE);
						dispose();
					}
				}				
			}
		});
	}
	
	private Stock getStock() {

		Item item = null;
		Warehouse wh = null;
		int quantity = -1;
		
		try {
			item = (Item) cb_items.getSelectedItem();
			if (item == null)
				throw new Exception();
		} catch (Exception e) {
			String msg = "ERROR: Item not selected";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		try {
			wh = (Warehouse) cb_wh.getSelectedItem();
			if (wh == null)
				throw new Exception();
		} catch (Exception e) {
			String msg = "ERROR: Warehouse not selected";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		try {
			if (txtquantity.getText().isEmpty())
				throw new InputMismatchException("Must have a Quantity");
			
			quantity = Integer.parseInt(txtquantity.getText());
			
			if (quantity < 0)
				throw new InputMismatchException("Quantity can't be Negative");
			
		} catch (NumberFormatException e) {
			String msg = "ERROR: Quantity is not a number";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		} catch (InputMismatchException e) {
			String msg = "ERROR: " + e.getMessage();
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		
		return new Stock(item, wh, quantity);
	}
	
}

