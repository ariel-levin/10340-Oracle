package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Model.*;


public class OracleDB {

	private Connection connection;
	private String dbUrl;
	
	
	public OracleDB() {
		
		
		
		
	}
		
	
	public void openConnection() {
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			dbUrl = "jdbc:oracle:thin:@localhost:1521:xe";

			connection = DriverManager.getConnection(dbUrl, "ARIEL", "ariel");

			System.out.println("Connection Success");
			
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {

			JOptionPane.showMessageDialog(null,"Error connecting to DB\n"
					+ "Please connect to DB first","Database Error",JOptionPane.ERROR_MESSAGE);

			System.exit(0);
		}
		
	}

	public void closeConnection() {
		synchronized (connection) {
			try {
				connection.close();
				System.out.println("Connection Closed");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Customer> getAllCustomers() {
		
		ArrayList<Customer> list = new ArrayList<Customer>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM customers";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);
				
				ResultSet rs = ps.executeQuery();
				
				while (rs.next())
					list.add(new Customer(	rs.getInt("customer_num"), 
											rs.getString("customer_fname"),
											rs.getString("customer_lname"),
											rs.getInt("customer_id"),
											rs.getString("customer_street"),
											rs.getString("customer_city"),
											rs.getString("customer_phone") 	));
							
							
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public int getCurrentOrderNum() {
		
		int max = -1;
		
		synchronized (connection) {
			try {
				String sqlQuery = "select \"ORDERS_SEQ\".currval from dual";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);
				
				ResultSet rs = ps.executeQuery();
				
				rs.next();
				max = rs.getInt(1);
							
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return max;
	}

	public void addNewOrder(Customer c) {
		synchronized (connection) {
			try {
				String sqlQuery = 	"INSERT INTO orders "
						+ "(order_date, customer_num, order_price, order_status) "
						+ "VALUES (?,?,?,?)";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);

				ps.setDate(1, getCurrentDate());
				ps.setInt(2, c.getNum());
				ps.setInt(3, 0);
				ps.setString(4, "open");
				ps.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static java.sql.Date getCurrentDate() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Date(today.getTime());
	}
	
	/////////////////////////////////////////////////////////////
	
	public void test() {
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * "
								+ "FROM emp ";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);
				
				//ps.setTimestamp(1, sqlStart);
				//ps.setTimestamp(2, sqlEnd);

				ResultSet rs = ps.executeQuery();
				
				while (rs.next())
					System.out.println(rs.getString("ename"));

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

}
