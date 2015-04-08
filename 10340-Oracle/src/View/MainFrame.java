package view;

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

import database.OracleDB;
import model.*;
import view.panels.*;


public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private OracleDB 	db;
	private JPanel 		mainPanel;
	

	public MainFrame() {
		
		db = new OracleDB();
		
		if (db.openConnection())
			System.out.println("Connection Success");

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

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension panelSize = new Dimension();
		panelSize.setSize(screenSize.width * 0.6, screenSize.height * 0.6);
		setSize(panelSize);
		
		getContentPane().setLayout(new BorderLayout());

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		mainPanel.setSize(panelSize);
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		EmptyBorder border = new EmptyBorder(15, 15, 15, 15);
		mainPanel.setBorder(border);
		
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
	
	public void removePanel() {
		mainPanel.removeAll();
		refresh();
	}
	
	public OracleDB getDB() {
		return db;
	}
	
	public void endProgram() {
		
		if (db.closeConnection())
			System.out.println("Connection Closed");
		
		System.exit(0);
	}
	
	public void refresh() {
		setVisible(true);
		repaint();
	}
	
	public Dimension getSize() {
		return mainPanel.getSize();
	}
	
	public void newOrder(Customer c) {
		db.addNewOrder(c);
		changePanel(new NewOrderPanel(this));
	}
	
	public void newInvoice(Customer c) {
		db.addNewInvoice(c);
		changePanel(new NewInvoicePanel(this));
	}
	
}

