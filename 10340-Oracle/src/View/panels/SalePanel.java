package view.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
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
public class SalePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public static final int ITEM_COL 		= 0;
	public static final int QUANTITY_COL 	= 1;
	public static final int PRICE_COL 		= 2;
	public static final int DISCOUNT_COL 	= 3;
	public static final int FPRICE_COL 		= 4;
	
	public static final float VAT			= 1.18f;
	
	
	protected MainFrame 			mainFrame;
	protected DefaultTableModel 	tableModel;
	protected JLabel				lblNum, lblDate, lblCustomer, lbltotalPrice, lblfinalPrice;
	protected JButton				btnCommit, btnAdd, btnRem;
	protected JPanel				northPanel, eastPanel, northBtnPanel;
	protected JTable 				table;
	protected float					finalPrice;
	
	
	public SalePanel(MainFrame mainFrame, String type) {
		this.mainFrame = mainFrame;
		this.finalPrice = 0.00f;
		initPanel(type);
	}
	
	
	private void initPanel(String type) {
		
		setLayout(new BorderLayout());
		setPreferredSize(mainFrame.getSize());
		
		eastPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(eastPanel, BoxLayout.Y_AXIS);
		
		eastPanel.setLayout(boxLayout);
		btnAdd = new JButton("Add Line");
		btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
		eastPanel.add(btnAdd);
		eastPanel.add(Box.createRigidArea(new Dimension(0,15)));
		btnRem = new JButton("Remove Line");
		btnRem.setAlignmentX(Component.CENTER_ALIGNMENT);
		eastPanel.add(btnRem);
		eastPanel.add(Box.createRigidArea(new Dimension(0,30)));
		JLabel lblPriceTitle = new JLabel("Total Price:");
		lblPriceTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		eastPanel.add(lblPriceTitle);
		lbltotalPrice = new JLabel(finalPrice + "");
		lbltotalPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
		eastPanel.add(lbltotalPrice);
		eastPanel.add(Box.createRigidArea(new Dimension(0,15)));
		JLabel lblFinalTitle = new JLabel("Final Price:");
		lblFinalTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		eastPanel.add(lblFinalTitle);
		lblfinalPrice = new JLabel(finalPrice + "");
		lblfinalPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
		eastPanel.add(lblfinalPrice);
		
		eastPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		northPanel = new JPanel();
		lblNum = new JLabel(type + " Num: ");
		EmptyBorder border = new EmptyBorder(5, 50, 5, 0);
		lblDate = new JLabel("Date: ");
		lblDate.setBorder(border);
		lblCustomer = new JLabel();
		lblCustomer.setBorder(border);
		northPanel.add(lblNum);
		northPanel.add(lblDate);
		northPanel.add(lblCustomer);
		northBtnPanel = new JPanel();
		btnCommit = new JButton("Commit " + type);
		northBtnPanel.add(btnCommit);
		JButton btnCancel = new JButton("Cancel");
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
		tableModel.addTableModelListener(new SalesTableModelListener(tableModel, this));
		table.setModel(tableModel);

		ArrayList<Item> items = mainFrame.getDB().getAllItems();
		JComboBox<Item> cb_items = new JComboBox<Item>();

		for (Item item : items)
			cb_items.addItem(item);

		TableColumn itemsColumn = table.getColumnModel().getColumn(0);
		itemsColumn.setCellEditor(new DefaultCellEditor(cb_items));

		table.getTableHeader().setReorderingAllowed(false);
	}

	public void updatePrice(float price) {
		finalPrice = price;
		lbltotalPrice.setText( String.format("%.2f", finalPrice) + "");
		lblfinalPrice.setText( String.format("%.2f", round(finalPrice * VAT, 2) ) + "");
	}
	
	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
	
}
