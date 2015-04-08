package view.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.Item;
import view.MainFrame;
import view.utils.SalesTableModelListener;


public class SalePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public static final int ITEM_COL 		= 0;
	public static final int QUANTITY_COL 	= 1;
	public static final int PRICE_COL 		= 2;
	public static final int DISCOUNT_COL 	= 3;
	public static final int FPRICE_COL 		= 4;
	
	
	protected MainFrame 			mainFrame;
	protected DefaultTableModel 	tableModel;
	protected JLabel				lblNum, lblDate, lblCustomer;
	protected JButton				btnCommit;
	protected JTable 				table;
	
	
	
	public SalePanel(MainFrame mainFrame, String type) {
		this.mainFrame = mainFrame;
		initPanel(type);
	}
	
	
	private void initPanel(String type) {
		
		setLayout(new BorderLayout());
		setPreferredSize(mainFrame.getSize());
		
		JPanel eastPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(eastPanel, BoxLayout.Y_AXIS);
		
		eastPanel.setLayout(boxLayout);
		JButton btnAdd = new JButton("Add Line");
		btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
		eastPanel.add(btnAdd);
		eastPanel.add(Box.createRigidArea(new Dimension(0,15)));
		JButton btnRem = new JButton("Remove Line");
		btnRem.setAlignmentX(Component.CENTER_ALIGNMENT);
		eastPanel.add(btnRem);
		eastPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		JPanel northPanel = new JPanel();
		lblNum = new JLabel("Order Number: ");
		EmptyBorder border = new EmptyBorder(5, 50, 5, 0);
		lblDate = new JLabel("Date: ");
		lblDate.setBorder(border);
		lblCustomer = new JLabel();
		lblCustomer.setBorder(border);
		northPanel.add(lblNum);
		northPanel.add(lblDate);
		northPanel.add(lblCustomer);
		JPanel northBtnPanel = new JPanel();
		btnCommit = new JButton("Commit " + type);
		northBtnPanel.add(btnCommit);
		JButton btnCancel = new JButton("Cancel " + type);
		northBtnPanel.add(btnCancel);
		northBtnPanel.setBorder(border);
		northPanel.add(northBtnPanel);

		add(northPanel, BorderLayout.NORTH);
		add(eastPanel, BorderLayout.EAST);

		createTable();

		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableModel.addRow(new Object[] {});
			}
		});
		
		btnRem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selection = table.getSelectedRows();
				for (int i = 0; i < selection.length; i++)
					selection[i] = table.convertRowIndexToModel(selection[i]);
				if (selection.length > 0)
					tableModel.removeRow(selection[0]);
			}
		});

		btnCancel.addActionListener(new ActionListener() {
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

		String[] columnNames = { "Item", "Quantity", "Price", "Discount(%)", "Final Price" };

		tableModel = new DefaultTableModel(columnNames, 1);
		tableModel
				.addTableModelListener(new SalesTableModelListener(tableModel));
		table.setModel(tableModel);

		ArrayList<Item> items = mainFrame.getDB().getAllItems();
		JComboBox<Item> cb_items = new JComboBox<Item>();

		for (Item item : items)
			cb_items.addItem(item);

		TableColumn itemsColumn = table.getColumnModel().getColumn(0);
		itemsColumn.setCellEditor(new DefaultCellEditor(cb_items));

	}

}