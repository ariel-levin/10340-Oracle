package view.reports;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.*;
import view.MainFrame;


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
public class ItemCustomerReport extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private MainFrame 				mainFrame;
	private DefaultTableModel 		tableModel;
	private JButton					btnSelectCustomer, btnRemoveSelect;
	private JPanel					northPanel, northBtnPanel;
	private JTable 					table;
	private JComboBox<Item> 		cb_items;
	private JComboBox<Customer> 	cb_customers;
	
	
	public ItemCustomerReport(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		initPanel();
		loadTable(null);
		table.setEnabled(false);
	}
	
	
	private void initPanel() {
		
		setLayout(new BorderLayout());
		setPreferredSize(mainFrame.getSize());
		
		setBorder(BorderFactory.createTitledBorder("Items Sales Per Customer Report"));
		
		ArrayList<Customer> customers = mainFrame.getDB().getAllCustomers();
		cb_customers = new JComboBox<Customer>();
		for (Customer c : customers)
			cb_customers.addItem(c);
		
		ArrayList<Item> items = mainFrame.getDB().getAllItems();
		cb_items = new JComboBox<Item>();
		for (Item item : items)
			cb_items.addItem(item);
		
		northPanel = new JPanel();
		northPanel.add(cb_customers);
		northPanel.add(Box.createRigidArea(new Dimension(5,0)));
		btnSelectCustomer = new JButton("Select Customer");
		northPanel.add(btnSelectCustomer);
		northPanel.add(Box.createRigidArea(new Dimension(50,0)));
		btnRemoveSelect = new JButton("Remove Selection");
		northPanel.add(btnRemoveSelect);
		northPanel.add(Box.createRigidArea(new Dimension(30,0)));

		JButton btnQuit = new JButton("Quit");
		northBtnPanel = new JPanel();
		northBtnPanel.add(btnQuit);
		northPanel.add(northBtnPanel);

		add(northPanel, BorderLayout.NORTH);

		createTable();

		btnSelectCustomer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (cb_items.getSelectedItem() != null) {
					Customer customer = (Customer) cb_customers.getSelectedItem();
					ArrayList<Invoice> invoices = mainFrame.getDB().getAllInvoice(true, customer);
					loadTable(invoices);
				}
			}
		});

		btnRemoveSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				loadTable(null);
			}
		});

		btnQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.removePanel();
			}
		});

	}

	private void createTable() {

		JScrollPane scroller = new JScrollPane();
		table = new JTable();

		scroller.setViewportView(table);

		add(scroller, BorderLayout.CENTER);

		String[] columnNames = { "Customer", "Date", "Invoice Number", "Invoice Line", 
				"Item", "Quantity", "Final Price" };

		tableModel = new DefaultTableModel(columnNames, 0);
		table.setModel(tableModel);

		TableColumn customerColumn = table.getColumnModel().getColumn(0);
		customerColumn.setCellEditor(new DefaultCellEditor(cb_customers));
		
		TableColumn itemsColumn = table.getColumnModel().getColumn(3);
		itemsColumn.setCellEditor(new DefaultCellEditor(cb_items));

		table.getTableHeader().setReorderingAllowed(false);
	}

	private void loadTable(ArrayList<Invoice> list) {
		
		ArrayList<Invoice> invoices;
		
		if (list == null)
			invoices = mainFrame.getDB().getAllInvoice(true);
		else
			invoices = list;
		
		removeAllRows();
		
		for (Invoice inv : invoices) {
			
			for (InvoiceLine line : inv.getLines())
			
				tableModel.addRow(new Object[] { inv.getCustomer(), inv.getDate(), 
						inv.getNum(), line.getNum(), line.getItem(), line.getQuantity(), 
						line.getFinalPrice() });
		}
		
	}
	
	private void removeAllRows() {
		if (tableModel.getRowCount() > 0) {
		    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
		        tableModel.removeRow(i);
		    }
		}
	}
	
	
	
	
}

