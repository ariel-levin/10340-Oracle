package View.order;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Model.Customer;
import View.MainFrame;


public class NewOrderPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private MainFrame mainFrame;
	private JPanel innerPanel;
	private Customer customer;
	private int orderNum;
	
	
	public NewOrderPanel(Customer c, MainFrame mainFrame) {
		
		this.mainFrame = mainFrame;
		this.customer = c;
		
		this.orderNum = mainFrame.getDB().getCurrentOrderNum();
		
		initPanel();
	}
	
	private void initPanel() {
		
		setLayout(new BorderLayout());
		
		innerPanel = new JPanel();
		innerPanel.setLayout(new BorderLayout());
		
		JScrollPane scroller = new JScrollPane(innerPanel);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroller, BorderLayout.CENTER);
		
		setBorder(BorderFactory.createTitledBorder("New Order: " + orderNum));
		
		setPreferredSize(mainFrame.getSize());
		
		createTable();
	}
	
	private void createTable() {
		
		String[] columnNames = {"Item", "Quantity", "Price", "Discount", "Final Price"};
        setLayout(new BorderLayout());
        JScrollPane pane = new JScrollPane();
        JTable table = new JTable();
        pane.setViewportView(table);
        JPanel eastPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        eastPanel.add(btnAdd);
        JPanel northPanel = new JPanel();
        JTextField txtField1 = new JTextField();
        JTextField txtField2 = new JTextField();
        JLabel lblField1 = new JLabel("Column1   ");
        JLabel lblField2 = new JLabel("Column2   ");
        northPanel.add(lblField1);
        northPanel.add(txtField1);
        northPanel.add(lblField2);
        northPanel.add(txtField2);
        txtField1.setPreferredSize(lblField1.getPreferredSize());
        txtField2.setPreferredSize(lblField2.getPreferredSize());

        add(northPanel, BorderLayout.NORTH);
        add(eastPanel, BorderLayout.EAST);
        add(pane,BorderLayout.CENTER);
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table.setModel(tableModel);
        btnAdd.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int count = tableModel.getRowCount()+1;
                tableModel.addRow(new Object[]{txtField1.getText(),txtField1.getText()});
            }
        });
	}
	
}
