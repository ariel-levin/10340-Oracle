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

import model.Item;
import model.Stock;
import model.Warehouse;
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
public class StockReport extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private MainFrame 				mainFrame;
	private DefaultTableModel 		tableModel;
	private JButton					btnFilterItem, btnFilterWH, btnRemoveFilter;
	private JPanel					northPanel, northBtnPanel;
	private JTable 					table;
	private JComboBox<Item> 		cb_items;
	private JComboBox<Warehouse> 	cb_wh;
	
	
	public StockReport(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		initPanel();
		loadTable(null);
		table.setEnabled(false);
	}
	
	
	private void initPanel() {
		
		setLayout(new BorderLayout());
		setPreferredSize(mainFrame.getSize());
		
		setBorder(BorderFactory.createTitledBorder("Stock Report"));
		
		ArrayList<Item> items = mainFrame.getDB().getAllItems();
		cb_items = new JComboBox<Item>();

		for (Item item : items)
			cb_items.addItem(item);
		
		ArrayList<Warehouse> warehouses = mainFrame.getDB().getAllWarehouses();
		cb_wh = new JComboBox<Warehouse>();

		for (Warehouse wh : warehouses)
			cb_wh.addItem(wh);
		
		northPanel = new JPanel();
		northPanel.add(cb_items);
		northPanel.add(Box.createRigidArea(new Dimension(5,0)));
		btnFilterItem = new JButton("Filter Item");
		northPanel.add(btnFilterItem);
		northPanel.add(Box.createRigidArea(new Dimension(50,0)));
		northPanel.add(cb_wh);
		northPanel.add(Box.createRigidArea(new Dimension(5,0)));
		btnFilterWH = new JButton("Filter Warehouse");
		northPanel.add(btnFilterWH);
		northPanel.add(Box.createRigidArea(new Dimension(50,0)));
		btnRemoveFilter = new JButton("Remove Filters");
		northPanel.add(btnRemoveFilter);
		northPanel.add(Box.createRigidArea(new Dimension(30,0)));

		JButton btnQuit = new JButton("Quit");
		northBtnPanel = new JPanel();
		northBtnPanel.add(btnQuit);
		northPanel.add(northBtnPanel);

		add(northPanel, BorderLayout.NORTH);

		createTable();

		btnFilterItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (cb_items.getSelectedItem() != null) {
					Item item = (Item) cb_items.getSelectedItem();
					ArrayList<Stock> stock = mainFrame.getDB().getStock(item);
					loadTable(stock);
				}
			}
		});

		btnFilterWH.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (cb_wh.getSelectedItem() != null) {
					Warehouse wh = (Warehouse) cb_wh.getSelectedItem();
					ArrayList<Stock> stock = mainFrame.getDB().getStock(wh);
					loadTable(stock);
				}
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

		String[] columnNames = { "Item", "Warehouse", "Quantity" };

		tableModel = new DefaultTableModel(columnNames, 0);
		table.setModel(tableModel);

		TableColumn itemsColumn = table.getColumnModel().getColumn(0);
		itemsColumn.setCellEditor(new DefaultCellEditor(cb_items));
		
		TableColumn whColumn = table.getColumnModel().getColumn(1);
		whColumn.setCellEditor(new DefaultCellEditor(cb_wh));

		table.getTableHeader().setReorderingAllowed(false);
	}

	private void loadTable(ArrayList<Stock> list) {
		
		ArrayList<Stock> stock;
		
		if (list == null)
			stock = mainFrame.getDB().getStock();
		else
			stock = list;
		
		removeAllRows();
		
		for (Stock s : stock)
			tableModel.addRow(new Object[] { s.getItem(), s.getWarehouse(), s.getQuantity() });
		
	}
	
	private void removeAllRows() {
		if (tableModel.getRowCount() > 0) {
		    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
		        tableModel.removeRow(i);
		    }
		}
	}
	
}

