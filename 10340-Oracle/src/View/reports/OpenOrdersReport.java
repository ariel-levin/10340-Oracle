package view.reports;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
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
public class OpenOrdersReport extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private MainFrame 				mainFrame;
	private DefaultTableModel 		tableModel;
	private JButton					btnFilterCustomer, btnFilterDates, btnRemoveFilter;
	private JPanel					northPanel, northBtnPanel, pnlDates;
	private JTable 					table;
	private JComboBox<Customer> 	cb_customers;
	private JDatePickerImpl 		startDatePicker, endDatePicker;
	
	
	public OpenOrdersReport(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		initPanel();
		loadTable(null);
		table.setEnabled(false);
	}
	
	
	private void initPanel() {
		
		setLayout(new BorderLayout());
		setPreferredSize(mainFrame.getSize());
		
		setBorder(BorderFactory.createTitledBorder("Open Orders Report"));
		
		ArrayList<Customer> customers = mainFrame.getDB().getAllCustomers();
		cb_customers = new JComboBox<Customer>();

		for (Customer c : customers)
			cb_customers.addItem(c);
		
		northPanel = new JPanel();
		northPanel.add(cb_customers);
		northPanel.add(Box.createRigidArea(new Dimension(5,0)));
		btnFilterCustomer = new JButton("Filter Customer");
		northPanel.add(btnFilterCustomer);
		
		northPanel.add(Box.createRigidArea(new Dimension(20,0)));
		
		pnlDates = new JPanel();
		/////
		JPanel pnlStart = new JPanel();
		pnlStart.setLayout(new BoxLayout(pnlStart, BoxLayout.Y_AXIS));
		JLabel lblStart = new JLabel("Start Date");
		pnlStart.add(lblStart);
		pnlStart.add(Box.createRigidArea(new Dimension(0, 5)));
		UtilDateModel startModel = new UtilDateModel();
		JDatePanelImpl startPanel = new JDatePanelImpl(startModel);
		startDatePicker = new JDatePickerImpl(startPanel);
		pnlStart.add(startDatePicker);
		pnlStart.add(Box.createRigidArea(new Dimension(0, 15)));
		pnlDates.add(pnlStart);
		pnlDates.add(Box.createRigidArea(new Dimension(10,0)));
		/////
		JPanel pnlEnd = new JPanel();
		pnlEnd.setLayout(new BoxLayout(pnlEnd, BoxLayout.Y_AXIS));
		JLabel lblEnd = new JLabel("End Date");
		lblEnd.setHorizontalAlignment(JLabel.CENTER);
		pnlEnd.add(lblEnd);
		pnlEnd.add(Box.createRigidArea(new Dimension(0, 5)));
		UtilDateModel endModel = new UtilDateModel();
		JDatePanelImpl endPanel = new JDatePanelImpl(endModel);
		endDatePicker = new JDatePickerImpl(endPanel);
		pnlEnd.add(endDatePicker);
		pnlEnd.add(Box.createRigidArea(new Dimension(0, 15)));
		pnlDates.add(pnlEnd);
		pnlDates.add(Box.createRigidArea(new Dimension(10,0)));
		/////
		btnFilterDates = new JButton("Filter Dates");
		pnlDates.add(btnFilterDates);
		
		northPanel.add(pnlDates);
		
		northPanel.add(Box.createRigidArea(new Dimension(10,0)));
		
		btnRemoveFilter = new JButton("Remove Filters");
		northPanel.add(btnRemoveFilter);
		northPanel.add(Box.createRigidArea(new Dimension(30,0)));

		JButton btnQuit = new JButton("Quit");
		northBtnPanel = new JPanel();
		northBtnPanel.add(btnQuit);
		northPanel.add(northBtnPanel);

		add(northPanel, BorderLayout.NORTH);

		createTable();

		btnFilterCustomer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (cb_customers.getSelectedItem() != null) {
					Customer customer = (Customer) cb_customers.getSelectedItem();
					ArrayList<Order> orders = mainFrame.getDB().getAllOpenOrders(customer);
					loadTable(orders);
				}
			}
		});
		
		btnFilterDates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				dateFilter();
			}
		});

		btnRemoveFilter.addActionListener(new ActionListener() {
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

		String[] columnNames = { "Order Number", "Order Date", "Customer", "Order Price", "Order Status" };

		tableModel = new DefaultTableModel(columnNames, 0);
		table.setModel(tableModel);

		TableColumn itemsColumn = table.getColumnModel().getColumn(0);
		itemsColumn.setCellEditor(new DefaultCellEditor(cb_customers));

		table.getTableHeader().setReorderingAllowed(false);
	}

	private void loadTable(ArrayList<Order> list) {
		
		ArrayList<Order> orders;
		
		if (list == null)
			orders = mainFrame.getDB().getAllOpenOrders();
		else
			orders = list;
		
		removeAllRows();
		
		for (Order o : orders)
			tableModel.addRow(new Object[] { o.getNum(), o.getDate(), 
					o.getCustomer(), o.getPrice(), o.getStatus() });
		
	}
	
	private void removeAllRows() {
		if (tableModel.getRowCount() > 0) {
		    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
		        tableModel.removeRow(i);
		    }
		}
	}
	
	private void dateFilter() {
		
		// start date
		Date date = (Date) startDatePicker.getModel().getValue();
		Calendar startDate = null;
		if (date != null) {
			startDate = Calendar.getInstance();
			startDate.setTime(date);
			startDate.set(Calendar.HOUR, 0);
			startDate.set(Calendar.MINUTE, 0);
			startDate.set(Calendar.SECOND, 0);
		}

		// end date
		date = (Date) endDatePicker.getModel().getValue();
		Calendar endDate = null;
		if (date != null) {
			endDate = Calendar.getInstance();
			endDate.setTime(date);
			endDate.set(Calendar.HOUR, 0);
			endDate.set(Calendar.MINUTE, 0);
			endDate.set(Calendar.SECOND, 0);
		}

		if (isDatesOK(startDate, endDate)) {
			
			ArrayList<Order> orders = mainFrame.getDB().getAllOpenOrders(startDate, endDate);
			loadTable(orders);
			
		} else {
			JOptionPane.showMessageDialog(null, "Incorrect Dates, "
					+ "could be from the following reasons:\n"
					+ "- One or both of the dates are not selected\n"
					+ "- The End Date is before the Start Date\n"
					+ "- The Start Date is after now", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private boolean isDatesOK(Calendar startDate, Calendar endDate) {
		Date date = new java.util.Date();
		Calendar now = Calendar.getInstance();
		now.setTime(date);

		if (startDate == null || endDate == null)
			return false;

		if (endDate.after(now))
			endDate.setTime(now.getTime());

		if (endDate.before(startDate) || startDate.after(now))
			return false;
		else
			return true;
	}
	
}

