package View.order;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
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

import Model.*;
import View.MainFrame;
import View.utils.SalesTableModelListener;


public class NewOrderPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private MainFrame 		mainFrame;
	private JPanel 			innerPanel;
	private Order 			order;	
	
	public NewOrderPanel(MainFrame mainFrame) {
		
		this.mainFrame = mainFrame;
		
		int order_num = mainFrame.getDB().getCurrentOrderNum();
		this.order = mainFrame.getDB().getOrderByNum(order_num);
		
		initPanel();
	}
	
	private void initPanel() {
		
		setLayout(new BorderLayout());
		
		innerPanel = new JPanel();
		innerPanel.setLayout(new BorderLayout());
		
		JScrollPane scroller = new JScrollPane(innerPanel);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroller, BorderLayout.CENTER);
		
		setBorder(BorderFactory.createTitledBorder("New Order: " + order.getNum()) );
		
		setPreferredSize(mainFrame.getSize());
		
		createTable();
	}
	
	private void createTable() {
		
		String[] columnNames = {"Item", "Quantity", "Price", "Discount(%)", "Final Price"};
        setLayout(new BorderLayout());
        JScrollPane pane = new JScrollPane();
        JTable table = new JTable();
        
        pane.setViewportView(table);
        JPanel eastPanel = new JPanel();
        JButton btnAdd = new JButton("Add Line");
        eastPanel.add(btnAdd);
        JPanel northPanel = new JPanel();

        JLabel lblField1 = new JLabel("Order Number: " + order.getNum());
        EmptyBorder border1 = new EmptyBorder(5, 0, 5, 50);
        lblField1.setBorder(border1);
        JLabel lblField2 = new JLabel("Date: " + order.getDate());
        EmptyBorder border2 = new EmptyBorder(5, 0, 5, 50);
        lblField2.setBorder(border2);
        JLabel lblField3 = new JLabel(order.getCustomer().toString());
        northPanel.add(lblField1);
        northPanel.add(lblField2);
        northPanel.add(lblField3);

        add(northPanel, BorderLayout.NORTH);
        add(eastPanel, BorderLayout.EAST);
        add(pane,BorderLayout.CENTER);
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 1);
        tableModel.addTableModelListener(new SalesTableModelListener(tableModel));
        table.setModel(tableModel);
        
        ArrayList<Item> items = mainFrame.getDB().getAllItems();
        JComboBox<Item> cb_items = new JComboBox<Item>();

		for (Item item : items)
			cb_items.addItem(item);
        
        TableColumn itemsColumn = table.getColumnModel().getColumn(0);
		itemsColumn.setCellEditor(new DefaultCellEditor(cb_items));

		
        btnAdd.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
//                int count = tableModel.getRowCount() + 1;
        		tableModel.addRow(new Object[] {});
            }
        });
	}
	
}
