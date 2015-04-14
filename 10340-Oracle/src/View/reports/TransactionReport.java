package view.reports;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.Item;
import model.Transaction;
import model.Warehouse;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
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
public class TransactionReport extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private MainFrame 				mainFrame;
	private DefaultTableModel 		tableModel;
	private JButton					btnFilterItem, btnFilterDates, btnFilterWH, btnRemoveFilter;
	private JPanel					northPanel1, northPanel2, northBtnPanel, pnlDates;
	private JTable 					table;
	private ButtonGroup 			typeGroup;
	private JComboBox<Item>		 	cb_items;
	private JComboBox<Warehouse> 	cb_wh;
	private JDatePickerImpl 		startDatePicker, endDatePicker;
	
	
	public TransactionReport(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		initPanel();
		loadTable(null);
		table.setEnabled(false);
	}
	
	
	private void initPanel() {
		
		setLayout(new BorderLayout());
		setPreferredSize(mainFrame.getSize());
		
		setBorder(BorderFactory.createTitledBorder("Transactions Report"));
		
		ArrayList<Item> items = mainFrame.getDB().getAllItems();
		cb_items = new JComboBox<Item>();
		for (Item item : items)
			cb_items.addItem(item);
		
		ArrayList<Warehouse> wh_list = mainFrame.getDB().getAllWarehouses();
		cb_wh = new JComboBox<Warehouse>();
		for (Warehouse wh : wh_list)
			cb_wh.addItem(wh);
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		
		northPanel1 = new JPanel();
		northPanel1.add(cb_items);
		northPanel1.add(Box.createRigidArea(new Dimension(5,0)));
		btnFilterItem = new JButton("Filter Item");
		northPanel1.add(btnFilterItem);
		
		northPanel1.add(Box.createRigidArea(new Dimension(20,0)));
		
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
		
		northPanel1.add(pnlDates);
		
		northPanel1.add(Box.createRigidArea(new Dimension(40,0)));

		JButton btnQuit = new JButton("Quit");
		northBtnPanel = new JPanel();
		northBtnPanel.add(btnQuit);
		northPanel1.add(northBtnPanel);

		northPanel.add(northPanel1);
		
		
		northPanel2 = new JPanel();
		northPanel2.add(cb_wh);
		northPanel2.add(Box.createRigidArea(new Dimension(5,0)));
		btnFilterWH = new JButton("Filter Warehouse");
		northPanel2.add(btnFilterWH);
		northPanel2.add(Box.createRigidArea(new Dimension(30,0)));
		
		JRadioButton rbDebit = new JRadioButton("Credit", true);
		JRadioButton rbCredit = new JRadioButton("Debit");
		typeGroup = new ButtonGroup();
		typeGroup.add(rbDebit);
		typeGroup.add(rbCredit);
		JPanel pnlType = new JPanel();
		pnlType.add(rbDebit);
		pnlType.add(rbCredit);
		northPanel2.add(pnlType);
		JButton btnFilterType = new JButton("Filter Type");
		northPanel2.add(btnFilterType);
		northPanel2.add(Box.createRigidArea(new Dimension(100,0)));
		
		btnRemoveFilter = new JButton("Remove Filters");
		northPanel2.add(btnRemoveFilter);

		northPanel.add(northPanel2);
		northPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		add(northPanel, BorderLayout.NORTH);
		
		
		createTable();

		btnFilterItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (cb_items.getSelectedItem() != null) {
					Item item = (Item) cb_items.getSelectedItem();
					ArrayList<Transaction> trans = mainFrame.getDB().getAllTransactions(item);
					loadTable(trans);
				}
			}
		});
		
		btnFilterDates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				dateFilter();
			}
		});
		
		btnFilterWH.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Warehouse wh = (Warehouse) cb_wh.getSelectedItem();
				ArrayList<Transaction> trans = mainFrame.getDB().getAllTransactions(wh);
				loadTable(trans);
			}
		});
		
		btnFilterType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String type = getSelectedType().toLowerCase();
				ArrayList<Transaction> trans = mainFrame.getDB().getAllTransactions(type);
				loadTable(trans);
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

		String[] columnNames = { "Transaction Number", "Date", "Item", "Quantity", "Type", "Warehouse" };

		tableModel = new DefaultTableModel(columnNames, 0);
		table.setModel(tableModel);

		TableColumn itemsColumn = table.getColumnModel().getColumn(2);
		itemsColumn.setCellEditor(new DefaultCellEditor(cb_items));
		
		TableColumn whColumn = table.getColumnModel().getColumn(5);
		whColumn.setCellEditor(new DefaultCellEditor(cb_wh));

		table.getTableHeader().setReorderingAllowed(false);
	}

	private void loadTable(ArrayList<Transaction> list) {
		
		ArrayList<Transaction> trans;
		
		if (list == null)
			trans = mainFrame.getDB().getAllTransactions();
		else
			trans = list;
		
		removeAllRows();
		
		for (Transaction t : trans)
			tableModel.addRow(new Object[] { t.getNum(), t.getDate(), t.getItem(), 
					t.getQuantity(), t.getType(), t.getWarehouse() });
		
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
			
			ArrayList<Transaction> trans = mainFrame.getDB().getAllTransactions(startDate, endDate);
			loadTable(trans);
			
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
	
	private String getSelectedType() {

		for (Enumeration<AbstractButton> rb = typeGroup.getElements(); rb.hasMoreElements();) {
			AbstractButton button = rb.nextElement();
			if (button.isSelected())
				return button.getText();
		}

		return null;
	}
	
}

