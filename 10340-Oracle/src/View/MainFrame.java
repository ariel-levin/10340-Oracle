package View;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import Database.OracleDB;
import Model.Customer;
import View.order.NewOrderPanel;


public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private OracleDB db;
	
	private JPanel mainPanel;
	

	public MainFrame() {
		
		db = new OracleDB();
		
		db.openConnection();

		initFrame();
		
	}
	
	
	private void initFrame() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e1) {}

		setTitle("Sales Management System");
		
		// set the frame's Close operation
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				// add on window close
				endProgram();
			}
		});

		Dimension frameSize = new Dimension();
		frameSize.setSize(1080,650);
		setSize(frameSize);
		
		getContentPane().setLayout(new BorderLayout());

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension panelSize = new Dimension();
		panelSize.setSize(screenSize.width * 0.5, screenSize.height * 0.5);
		mainPanel.setSize(panelSize);
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		EmptyBorder border = new EmptyBorder(15, 15, 15, 15);
		mainPanel.setBorder(border);
		
//		JPanel lowerPanel = new JPanel(new BorderLayout());
//		console = new JTextArea();
//		console.setFont(console.getFont().deriveFont(Font.PLAIN,12));
//		console.setEditable(false);

//		DefaultCaret caret = (DefaultCaret)console.getCaret();
//		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

//		JScrollPane scrollPane = new JScrollPane();
//		scrollPane.setViewportView(console);
//		scrollPane.setPreferredSize(new Dimension(this.getWidth()-20, 150));
//		lowerPanel.add(scrollPane, BorderLayout.CENTER);
		
		setJMenuBar(new SysMenu(this));
		setLocationRelativeTo(null);
//		setAlwaysOnTop(true);
		setVisible(true);
	}
	
	public void changePanel(JPanel panel) {
		mainPanel.removeAll();
		mainPanel.add(panel, BorderLayout.CENTER);
		refresh();
	}
	
	public OracleDB getDB() {
		return db;
	}
	
	public void endProgram() {
		db.closeConnection();
		System.exit(0);
	}
	
	public void refresh() {
		setVisible(true);
	}
	
	public Dimension getSize() {
		return mainPanel.getSize();
	}
	
	public void newOrder(Customer c) {
		db.addNewOrder(c);
		changePanel(new NewOrderPanel(c, this));
	}
	
}

