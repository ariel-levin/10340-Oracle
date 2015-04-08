package database;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import model.*;


public class OracleDB {

	private Connection connection;
	private String dbUrl;
	
	
	public boolean openConnection() {
		
		boolean success = false;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			dbUrl = "jdbc:oracle:thin:@localhost:1521:xe";

			connection = DriverManager.getConnection(dbUrl, "ARIEL", "ariel");

			success = true;
			
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {

			JOptionPane.showMessageDialog(null,"Error connecting to DB\n"
					+ "Please connect to DB first","Database Error",JOptionPane.ERROR_MESSAGE);

			System.exit(0);
		}
		
		return success;
	}

	public boolean closeConnection() {
		
		boolean success = false;
		
		synchronized (connection) {
			try {
				connection.close();
				
				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}

	public Customer getCustomerByNum(int customer_num) {
		
		Customer c = null;
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM customers WHERE customer_num = ?";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, customer_num);
				
				ResultSet rs = ps.executeQuery();
				
				if (!rs.next())
					return c;
				
				String fname = rs.getString("customer_fname");
				String lname = rs.getString("customer_lname");
				int id = rs.getInt("customer_id");
				String street = rs.getString("customer_street");
				String city = rs.getString("customer_city");
				String phone = rs.getString("customer_phone");
				
				c = new Customer(customer_num, fname, lname, id, street, city, phone);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return c;
	}

	public Warehouse getWarehouseByNum(int wh_num) {
		
		Warehouse wh = null;
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM warehouses WHERE wh_num = ?";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, wh_num);
				
				ResultSet rs = ps.executeQuery();
				
				if (!rs.next())
					return wh;
				
				String name = rs.getString("wh_name");
				String street = rs.getString("wh_street");
				String city = rs.getString("wh_city");
				String phone = rs.getString("wh_phone");
				
				wh = new Warehouse(wh_num, name, street, city, phone);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return wh;
	}

	public Item getItemByNum(int item_num) {
		
		Item item = null;
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM items WHERE item_num = ?";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, item_num);
				
				ResultSet rs = ps.executeQuery();
				
				if (!rs.next())
					return item;
				
				String name = rs.getString("item_name");
				String desc = rs.getString("item_desc");
				float price = rs.getFloat("item_price");
				Blob img = rs.getBlob("item_img");
				
				int wh_num = rs.getInt("wh_num");
				Warehouse wh = getWarehouseByNum(wh_num);
				
				item = new Item(item_num, name, desc, price, img, wh);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return item;
	}
		
	public Order getOrderByNum(int order_num) {
		
		Order order = null;
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM orders WHERE order_num = ?";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, order_num);
				
				ResultSet rs = ps.executeQuery();
				
				if (!rs.next())
					return order;
				
				java.sql.Date date = rs.getDate("order_date");
				
				int customer_num = rs.getInt("customer_num");
				Customer customer = getCustomerByNum(customer_num);
				
				float price = rs.getFloat("order_price");
				String status = rs.getString("order_status");
				
				order = new Order(order_num, date, customer, price, status);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				String sqlQuery = "SELECT * FROM orders_lines "
						+ "WHERE order_num = ?"
						+ "ORDER BY line_num ASC";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, order_num);
				
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int num = rs.getInt("line_num");
					
					int item_num = rs.getInt("item_num");
					Item item = getItemByNum(item_num);
					
					int quantity = rs.getInt("line_quantity");
					float price = rs.getFloat("line_price");
					int discount = rs.getInt("line_discount");
					float finalPrice = rs.getFloat("line_final_price");
					
					order.addLine( new OrderLine(num, item, quantity, price, discount, finalPrice) );
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return order;
	}
	
	public Invoice getInvoiceByNum(int invoice_num) {
		
		Invoice inv = null;
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM invoice WHERE invoice_num = ?";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, invoice_num);
				
				ResultSet rs = ps.executeQuery();
				
				if (!rs.next())
					return inv;
				
				int order_num = -1;
				
				try {
					order_num = rs.getInt("order_num");
				} catch (Exception e) {}
				
				Order order = null;
				
				if (order_num != -1)
					order = getOrderByNum(order_num);
				
				
				java.sql.Date date = rs.getDate("invoice_date");
				
				int customer_num = rs.getInt("customer_num");
				Customer customer = getCustomerByNum(customer_num);
				
				float price = rs.getFloat("invoice_price");
				
				inv = new Invoice(invoice_num, order, date, customer, price);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				String sqlQuery = "SELECT * FROM invoice_lines "
						+ "WHERE invoice_num = ?"
						+ "ORDER BY line_num ASC";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, invoice_num);
				
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int num = rs.getInt("line_num");
					
					int item_num = rs.getInt("item_num");
					Item item = getItemByNum(item_num);
					
					int quantity = rs.getInt("line_quantity");
					float price = rs.getFloat("line_price");
					int discount = rs.getInt("line_discount");
					float finalPrice = rs.getFloat("line_final_price");
					
					inv.addLine( new InvoiceLine(num, item, quantity, price, discount, finalPrice) );
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return inv;
	}
	
	public ArrayList<Customer> getAllCustomers() {
		
		ArrayList<Customer> list = new ArrayList<Customer>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM customers";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);
				
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					list.add(new Customer(	rs.getInt("customer_num"), 
											rs.getString("customer_fname"),
											rs.getString("customer_lname"),
											rs.getInt("customer_id"),
											rs.getString("customer_street"),
											rs.getString("customer_city"),
											rs.getString("customer_phone") 	));
				}
							
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public ArrayList<Item> getAllItems() {
		
		ArrayList<Item> list = new ArrayList<Item>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM items";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);
				
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int wh_num = rs.getInt("wh_num");
					Warehouse wh = getWarehouseByNum(wh_num);
					
					list.add(new Item(	rs.getInt("item_num"), 
										rs.getString("item_name"),
										rs.getString("item_desc"),
										rs.getInt("item_price"),
										rs.getBlob("item_img"),
										wh	));
				}
							
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

	public int getCurrentInvoiceNum() {
		
		int max = -1;
		
		synchronized (connection) {
			try {
				String sqlQuery = "select \"INVOICE_SEQ\".currval from dual";

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
	
	public boolean addNewOrder(Customer c) {
		
		boolean success = false;
		
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
				
				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	public boolean addNewInvoice(Customer c) {
		
		boolean success = false;
		
		synchronized (connection) {
			try {
				String sqlQuery = 	"INSERT INTO invoice "
						+ "(invoice_date, customer_num, invoice_price) "
						+ "VALUES (?,?,?)";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);

				ps.setDate(1, getCurrentDate());
				ps.setInt(2, c.getNum());
				ps.setInt(3, 0);
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	public boolean addOrderLines(Order order) {
		
		boolean success = false;
		
		synchronized (connection) {
			
			ArrayList<OrderLine> list = order.getLines();
			
			for (OrderLine line : list) {
				try {
					String sqlQuery = 	"INSERT INTO orders_lines "
							+ "(order_num, line_num, item_num, line_quantity, line_price, "
							+ "line_discount, line_final_price) "
							+ "VALUES (?,?,?,?,?,?,?)";
	
					PreparedStatement ps = connection.prepareStatement(sqlQuery);
	
					ps.setInt(1, order.getNum());
					ps.setInt(2, line.getNum());
					ps.setInt(3, line.getItem().getNum());
					ps.setInt(4, line.getQuantity());
					ps.setFloat(5, line.getPrice());
					ps.setInt(6, line.getDiscount());
					ps.setFloat(7, line.getFinalPrice());
					
					ps.executeUpdate();
					
					success = true;
	
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return success;
	}

	public boolean addInvoiceLines(Invoice invoice) {
		
		boolean success = false;
		
		synchronized (connection) {
			
			ArrayList<InvoiceLine> list = invoice.getLines();
			
			for (InvoiceLine line : list) {
				try {
					String sqlQuery = 	"INSERT INTO invoice_lines "
							+ "(invoice_num, line_num, item_num, line_quantity, line_price, "
							+ "line_discount, line_final_price) "
							+ "VALUES (?,?,?,?,?,?,?)";
	
					PreparedStatement ps = connection.prepareStatement(sqlQuery);
	
					ps.setInt(1, invoice.getNum());
					ps.setInt(2, line.getNum());
					ps.setInt(3, line.getItem().getNum());
					ps.setInt(4, line.getQuantity());
					ps.setFloat(5, line.getPrice());
					ps.setInt(6, line.getDiscount());
					ps.setFloat(7, line.getFinalPrice());
					
					ps.executeUpdate();
					
					success = true;
	
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return success;
	}
	
	public boolean closeOrder(int order_num) {
		
		boolean success = false;
		
		synchronized (connection) {

			try {
				String sqlQuery = "UPDATE orders "
						+ "SET order_status = ? "
						+ "WHERE order_num = ? ";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);

				ps.setString(1, "closed");
				ps.setInt(2, order_num);

				ps.executeUpdate();

				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		return success;
	}

	public boolean openOrder(int order_num) {
		
		boolean success = false;
		
		synchronized (connection) {

			try {
				String sqlQuery = "UPDATE orders "
						+ "SET order_status = ? "
						+ "WHERE order_num = ? ";

				PreparedStatement ps = connection.prepareStatement(sqlQuery);

				ps.setString(1, "open");
				ps.setInt(2, order_num);

				ps.executeUpdate();

				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		return success;
	}
	
	public static java.sql.Date getCurrentDate() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Date(today.getTime());
	}
	
}
