package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import View.order.*;



public class SysMenu extends JMenuBar {
	
	private static final long serialVersionUID = 1L;

	
	public SysMenu(MainFrame mainFrame) {
				
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				mainFrame.endProgram();
			}
		});
		fileMenu.add(exitMenuItem);
		
		this.add(fileMenu);
		
		/////////////////////////////////////////////////////////////////////////////////////
		
		JMenu orderMenu = new JMenu("Order");
		
		JMenuItem searchOrderItem = new JMenuItem("Search Order");
		searchOrderItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		orderMenu.add(searchOrderItem);
		
		JMenuItem newOrderItem = new JMenuItem("New Order");
		newOrderItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				new NewOrderForm(mainFrame);
			}
		});
		orderMenu.add(newOrderItem);
		
		JMenuItem updateOrderItem = new JMenuItem("Update Order");
		updateOrderItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		orderMenu.add(updateOrderItem);
		
		JMenuItem cancelOrderItem = new JMenuItem("Cancel Order");
		cancelOrderItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		orderMenu.add(cancelOrderItem);
		
		this.add(orderMenu);
		
		/////////////////////////////////////////////////////////////////////////////////////		
		
		JMenu invoiceMenu = new JMenu("Invoice");
		
		JMenuItem searchInvoiceItem = new JMenuItem("Search Invoice");
		searchInvoiceItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		invoiceMenu.add(searchInvoiceItem);
		
		JMenuItem newInvoiceItem = new JMenuItem("New Invoice");
		newInvoiceItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		invoiceMenu.add(newInvoiceItem);
		
		JMenuItem invoiceFromOrderItem = new JMenuItem("New Invoice From Order");
		invoiceFromOrderItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		invoiceMenu.add(invoiceFromOrderItem);
		
		JMenuItem updateInvoiceItem = new JMenuItem("Update Invoice");
		updateInvoiceItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		invoiceMenu.add(updateInvoiceItem);
		
		JMenuItem cancelInvoiceItem = new JMenuItem("Cancel Invoice");
		cancelInvoiceItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		invoiceMenu.add(cancelInvoiceItem);
		
		this.add(invoiceMenu);
		
		/////////////////////////////////////////////////////////////////////////////////////

		JMenu reportMenu = new JMenu("Reports/Queries");
		
		JMenuItem itemBalanceItem = new JMenuItem("Items / Warehouse Balance");
		itemBalanceItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		reportMenu.add(itemBalanceItem);
		
		JMenuItem openOrdersItem = new JMenuItem("Open Orders");
		openOrdersItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		reportMenu.add(openOrdersItem);
		
		JMenuItem transactionItem = new JMenuItem("Periodic Transactions");
		transactionItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		reportMenu.add(transactionItem);
		
		JMenuItem salesItem = new JMenuItem("Item Sales");
		salesItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		reportMenu.add(salesItem);
		
		this.add(reportMenu);
		
		/////////////////////////////////////////////////////////////////////////////////////

		JMenu helpMenu = new JMenu("Help");
		
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = 	"Ariel Levin\n" +
								"ariel.lvn89@gmail.com\n" +
								"http://about.me/ariel.levin\n\n" +
								"Matan Shulman\n" +
								"matan.shulman87@gmail.com";
				
				JOptionPane.showMessageDialog(null,msg,"About Sales Management",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpMenu.add(aboutItem);
		
		this.add(helpMenu);
		
	}
	
}