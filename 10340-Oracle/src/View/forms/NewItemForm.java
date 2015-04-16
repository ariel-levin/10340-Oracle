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
public class NewItemForm extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private MainFrame 	mainFrame;
	private JTextField 	txtname, txtdesc, txtprice;
	

	public NewItemForm(MainFrame mainFrame) {
		
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
		
		setTitle("New Item");
		setSize(new Dimension(220,230));
		
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
		
		JLabel lblname = new JLabel("Item Name (*)");
		lblname.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lblname);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		txtname = new JTextField();
		txtname.setAlignmentX(Component.CENTER_ALIGNMENT);
		txtname.setHorizontalAlignment(JTextField.CENTER);
		pnlMain.add(txtname);
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));

		JLabel lbldesc = new JLabel("Description");
		lbldesc.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lbldesc);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		txtdesc = new JTextField();
		txtdesc.setAlignmentX(Component.CENTER_ALIGNMENT);
		txtdesc.setHorizontalAlignment(JTextField.CENTER);
		pnlMain.add(txtdesc);
		pnlMain.add(Box.createRigidArea(new Dimension(0,10)));
		
		JLabel lblprice = new JLabel("Price (*)");
		lblprice.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlMain.add(lblprice);
		pnlMain.add(Box.createRigidArea(new Dimension(0,5)));
		txtprice = new JTextField();
		txtprice.setAlignmentX(Component.CENTER_ALIGNMENT);
		txtprice.setHorizontalAlignment(JTextField.CENTER);
		pnlMain.add(txtprice);
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
				Item item = getItem();
				if (item != null) {
					boolean success = mainFrame.getDB().addNewItem(item);
					if (success) {
						String msg = "The Item was added successfully";
						JOptionPane.showMessageDialog(null, msg, "Success",JOptionPane.INFORMATION_MESSAGE);
						dispose();
					}
				}				
			}
		});
	}
	
	private Item getItem() {

		String name = null, desc = null;
		float price = 0.0f;
		
		try {
			name = txtname.getText();
			if (name.isEmpty())
				throw new Exception();
		} catch (Exception e) {
			String msg = "ERROR: Item Name";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		try {
			if (!txtdesc.getText().isEmpty())
				desc = txtdesc.getText();
		} catch (Exception e) {
			String msg = "ERROR: Description";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		try {
			if (txtprice.getText().isEmpty())
				throw new InputMismatchException("Must have a Price");
			
			price = Float.parseFloat(txtprice.getText());
			
			if (price < 0)
				throw new InputMismatchException("Price can't be Negative");
			
		} catch (NumberFormatException e) {
			String msg = "ERROR: Price is not a number";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		} catch (InputMismatchException e) {
			String msg = "ERROR: " + e.getMessage();
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		
		return new Item(name, desc, price);
	}
	
}

