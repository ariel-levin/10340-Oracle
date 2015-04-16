package view.utils;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import model.*;


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
public class DBErrors {

	public static final int UNIQUE_CONSTRAINT = 00001;
	public static final int STOCK_QUANTITY = 20001;
	
	
	public static void showError() {
		
		someError();
	}
	
	public static void showError(String str) {
		
		someError(str);
	}
	
	public static void showError(SQLException e) {
		
		showError(e, null);
	}
	
	public static void showError(SQLException e, Object obj) {
		
		switch (e.getErrorCode()) {
		
			case STOCK_QUANTITY:
				notEnoughInStock(obj);
				break;
				
			case UNIQUE_CONSTRAINT:
				uniqueConstraint(obj);
				break;
				
			default:
				e.printStackTrace();
				someError();
				break;
		}
		
	}
	
	private static void notEnoughInStock(Object obj) {
		Item item = (Item)obj;
		String msg = "ERROR: Item " + item + " >> Not enough in stock";
		JOptionPane.showMessageDialog(null,msg,"Stock Error",JOptionPane.ERROR_MESSAGE);
	}
	
	private static void uniqueConstraint(Object obj) {
		String msg = "ERROR: Unique Constraint >> " + (String)obj + " already exists";
		JOptionPane.showMessageDialog(null,msg,"Unique Constraint Error",JOptionPane.ERROR_MESSAGE);
	}
	
	private static void someError() {
		String msg = "Some error was occurred...";
		JOptionPane.showMessageDialog(null, msg, "Error",JOptionPane.ERROR_MESSAGE);
	}
	
	private static void someError(String str) {
		JOptionPane.showMessageDialog(null, str, "Error",JOptionPane.ERROR_MESSAGE);
	}
	
}
