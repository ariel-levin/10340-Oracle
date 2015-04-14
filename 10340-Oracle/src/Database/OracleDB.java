package database;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
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
public class OracleDB {

	private Connection 			connection;
	private PreparedStatement 	ps;
	private String 				dbUrl;
	
	
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
		
		ResultSet rs = null;
		Customer c = null;
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM customers "
								+ "WHERE customer_num = ?";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, customer_num);
				
				rs = ps.executeQuery();
				
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
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return c;
	}

	public Warehouse getWarehouseByNum(int wh_num) {
		
		ResultSet rs = null;
		Warehouse wh = null;
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM warehouses WHERE wh_num = ?";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, wh_num);
				
				rs = ps.executeQuery();
				
				if (!rs.next())
					return wh;
				
				String name = rs.getString("wh_name");
				String street = rs.getString("wh_street");
				String city = rs.getString("wh_city");
				String phone = rs.getString("wh_phone");
				
				wh = new Warehouse(wh_num, name, street, city, phone);
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return wh;
	}

	public Item getItemByNum(int item_num) {
		
		ResultSet rs = null;
		Item item = null;
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM items WHERE item_num = ?";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, item_num);
				
				rs = ps.executeQuery();
				
				if (!rs.next())
					return item;
				
				String name = rs.getString("item_name");
				String desc = rs.getString("item_desc");
				float price = rs.getFloat("item_price");
				Blob img = rs.getBlob("item_img");
				
				item = new Item(item_num, name, desc, price, img);
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return item;
	}
		
	public Order getOrderByNum(int order_num) {
		
		ResultSet rs = null;
		Order order = null;
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM orders WHERE order_num = ?";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, order_num);
				
				rs = ps.executeQuery();
				
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
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
			
			try {
				String sqlQuery = "SELECT * FROM orders_lines "
						+ "WHERE order_num = ?"
						+ "ORDER BY line_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, order_num);
				
				rs = ps.executeQuery();
				
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
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return order;
	}
	
	public Invoice getInvoiceByNum(int invoice_num) {
		
		ResultSet rs = null;
		Invoice inv = null;
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM invoice WHERE invoice_num = ?";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, invoice_num);
				
				rs = ps.executeQuery();
				
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
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
			
			try {
				String sqlQuery = "SELECT * FROM invoice_lines "
						+ "WHERE invoice_num = ? "
						+ "ORDER BY line_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, invoice_num);
				
				rs = ps.executeQuery();
				
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
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return inv;
	}
	
	public ArrayList<Customer> getAllCustomers() {
		
		ResultSet rs = null;
		ArrayList<Customer> list = new ArrayList<Customer>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM customers";

				ps = connection.prepareStatement(sqlQuery);
				
				rs = ps.executeQuery();
				
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
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
	public ArrayList<Item> getAllItems() {
		
		ResultSet rs = null;
		ArrayList<Item> list = new ArrayList<Item>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM items";

				ps = connection.prepareStatement(sqlQuery);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					list.add(new Item(	rs.getInt("item_num"), 
										rs.getString("item_name"),
										rs.getString("item_desc"),
										rs.getInt("item_price"),
										rs.getBlob("item_img") ));
				}
							
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}

	public ArrayList<Warehouse> getAllWarehouses() {
		
		ResultSet rs = null;
		ArrayList<Warehouse> list = new ArrayList<Warehouse>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM warehouses";

				ps = connection.prepareStatement(sqlQuery);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					list.add(new Warehouse(	rs.getInt("wh_num"), 
											rs.getString("wh_name"),
											rs.getString("wh_street"),
											rs.getString("wh_city"),
											rs.getString("wh_phone") ));
				}
							
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
	public ArrayList<Order> getAllOpenOrders() {
		
		ResultSet rs = null;
		ArrayList<Order> list = new ArrayList<Order>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM orders "
								+ "WHERE order_status LIKE 'open' "
								+ "ORDER BY order_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int order_num = rs.getInt("order_num");
					java.sql.Date date = rs.getDate("order_date");
					
					int customer_num = rs.getInt("customer_num");
					Customer customer = getCustomerByNum(customer_num);
					
					float price = rs.getFloat("order_price");
					String status = rs.getString("order_status");
					
					list.add( new Order(order_num, date, customer, price, status) );
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}

	public ArrayList<Order> getAllOpenOrders(Customer customer) {
		
		ResultSet rs = null;
		ArrayList<Order> list = new ArrayList<Order>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM orders "
								+ "WHERE order_status LIKE 'open' "
								+ "AND customer_num = ? "
								+ "ORDER BY order_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, customer.getNum());
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int order_num = rs.getInt("order_num");
					java.sql.Date date = rs.getDate("order_date");
					
					float price = rs.getFloat("order_price");
					String status = rs.getString("order_status");
					
					list.add( new Order(order_num, date, customer, price, status) );
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
	public ArrayList<Order> getAllOpenOrders(Calendar startDate, Calendar endDate) {
		
		ResultSet rs = null;
		ArrayList<Order> list = new ArrayList<Order>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM orders "
								+ "WHERE order_status LIKE 'open' "
								+ "AND order_date BETWEEN ? AND ? "
								+ "ORDER BY order_num ASC";

				ps = connection.prepareStatement(sqlQuery);

				ps.setDate(1, convertCalendarToSql(startDate));
				ps.setDate(2, convertCalendarToSql(endDate));
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int order_num = rs.getInt("order_num");
					java.sql.Date date = rs.getDate("order_date");
					
					int customer_num = rs.getInt("customer_num");
					Customer customer = getCustomerByNum(customer_num);
					
					float price = rs.getFloat("order_price");
					String status = rs.getString("order_status");
					
					list.add( new Order(order_num, date, customer, price, status) );
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
		
	public ArrayList<Order> getAllOrders() {
		
		ResultSet rs = null;
		ArrayList<Order> list = new ArrayList<Order>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM orders";

				ps = connection.prepareStatement(sqlQuery);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int order_num = rs.getInt("order_num");
					java.sql.Date date = rs.getDate("order_date");
					
					int customer_num = rs.getInt("customer_num");
					Customer customer = getCustomerByNum(customer_num);
					
					float price = rs.getFloat("order_price");
					String status = rs.getString("order_status");
					
					list.add( new Order(order_num, date, customer, price, status) );
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}

	public ArrayList<Invoice> getAllInvoice(boolean withLines) {
		
		ResultSet rs1 = null;
		ArrayList<Invoice> list = new ArrayList<Invoice>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM invoice "
								+ "ORDER BY customer_num ASC, invoice_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				rs1 = ps.executeQuery();
				
				while (rs1.next()) {
					
					int invoice_num = rs1.getInt("invoice_num");
					
					java.sql.Date date = rs1.getDate("invoice_date");
					
					int customer_num = rs1.getInt("customer_num");
					Customer customer = getCustomerByNum(customer_num);
					
					float inv_price = rs1.getFloat("invoice_price");
					
					Invoice invoice = new Invoice(invoice_num, null, date, customer, inv_price);
					
					if (withLines) {
						
						ResultSet rs2 = null;
						
						try {
							sqlQuery = "SELECT * FROM invoice_lines "
									+ "WHERE invoice_num = ? "
									+ "ORDER BY line_num ASC";
	
							ps = connection.prepareStatement(sqlQuery);
							
							ps.setInt(1, invoice_num);
							
							rs2 = ps.executeQuery();
							
							while (rs2.next()) {
								
								int num = rs2.getInt("line_num");
								
								int item_num = rs2.getInt("item_num");
								Item item = getItemByNum(item_num);
								
								int quantity = rs2.getInt("line_quantity");
								float line_price = rs2.getFloat("line_price");
								int discount = rs2.getInt("line_discount");
								float finalPrice = rs2.getFloat("line_final_price");
								
								invoice.addLine( new InvoiceLine(num, item, quantity, line_price, discount, finalPrice) );
							}
							
						} catch (SQLException e) {
							e.printStackTrace();
						} finally {
							try { rs2.close(); ps.close(); }
							catch (Exception e1) {}
						}
					}
					
					list.add(invoice);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs1.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
	public ArrayList<Invoice> getAllInvoice(boolean withLines, Customer customer) {
		
		ResultSet rs1 = null;
		ArrayList<Invoice> list = new ArrayList<Invoice>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM invoice "
								+ "WHERE customer_num = ? "
								+ "ORDER BY customer_num ASC, invoice_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, customer.getNum());
				
				rs1 = ps.executeQuery();
				
				while (rs1.next()) {
					
					int invoice_num = rs1.getInt("invoice_num");
					
					java.sql.Date date = rs1.getDate("invoice_date");
					
					float inv_price = rs1.getFloat("invoice_price");
					
					Invoice invoice = new Invoice(invoice_num, null, date, customer, inv_price);
					
					if (withLines) {
						
						ResultSet rs2 = null;
						
						try {
							sqlQuery = "SELECT * FROM invoice_lines "
									+ "WHERE invoice_num = ?"
									+ "ORDER BY line_num ASC";
	
							ps = connection.prepareStatement(sqlQuery);
							
							ps.setInt(1, invoice_num);
							
							rs2 = ps.executeQuery();
							
							while (rs2.next()) {
								
								int num = rs2.getInt("line_num");
								
								int item_num = rs2.getInt("item_num");
								Item item = getItemByNum(item_num);
								
								int quantity = rs2.getInt("line_quantity");
								float line_price = rs2.getFloat("line_price");
								int discount = rs2.getInt("line_discount");
								float finalPrice = rs2.getFloat("line_final_price");
								
								invoice.addLine( new InvoiceLine(num, item, quantity, line_price, discount, finalPrice) );
							}
							
						} catch (SQLException e) {
							e.printStackTrace();
						} finally {
							try { rs2.close(); ps.close(); }
							catch (Exception e1) {}
						}
					}
					
					list.add(invoice);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs1.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}

	public ArrayList<Transaction> getAllTransactions() {
		
		ResultSet rs = null;
		ArrayList<Transaction> list = new ArrayList<Transaction>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM transactions "
								+ "ORDER BY transaction_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int trans_num = rs.getInt("transaction_num");
					
					java.sql.Date date = rs.getDate("transaction_date");
					
					int item_num = rs.getInt("item_num");
					Item item = getItemByNum(item_num);
					
					int quantity = rs.getInt("transaction_quantity");
					
					String type = rs.getString("transaction_type");
					
					int wh_num = rs.getInt("wh_num");
					Warehouse wh = getWarehouseByNum(wh_num);
					
					list.add( new Transaction(trans_num, date, item, quantity, type, wh) );
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
	public ArrayList<Transaction> getAllTransactions(Calendar startDate, Calendar endDate) {
		
		ResultSet rs = null;
		ArrayList<Transaction> list = new ArrayList<Transaction>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM transactions "
								+ "WHERE transaction_date BETWEEN ? AND ? "
								+ "ORDER BY transaction_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setDate(1, convertCalendarToSql(startDate));
				ps.setDate(2, convertCalendarToSql(endDate));
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int trans_num = rs.getInt("transaction_num");
					
					java.sql.Date date = rs.getDate("transaction_date");
					
					int item_num = rs.getInt("item_num");
					Item item = getItemByNum(item_num);
					
					int quantity = rs.getInt("transaction_quantity");
					
					String type = rs.getString("transaction_type");
					
					int wh_num = rs.getInt("wh_num");
					Warehouse wh = getWarehouseByNum(wh_num);
					
					list.add( new Transaction(trans_num, date, item, quantity, type, wh) );
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
	public ArrayList<Transaction> getAllTransactions(Item item) {
		
		ResultSet rs = null;
		ArrayList<Transaction> list = new ArrayList<Transaction>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM transactions "
								+ "WHERE item_num = ? "
								+ "ORDER BY transaction_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, item.getNum());
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int trans_num = rs.getInt("transaction_num");
					
					java.sql.Date date = rs.getDate("transaction_date");
					
					int quantity = rs.getInt("transaction_quantity");
					
					String type = rs.getString("transaction_type");
					
					int wh_num = rs.getInt("wh_num");
					Warehouse wh = getWarehouseByNum(wh_num);
					
					list.add( new Transaction(trans_num, date, item, quantity, type, wh) );
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
	public ArrayList<Transaction> getAllTransactions(String type) {
		
		ResultSet rs = null;
		ArrayList<Transaction> list = new ArrayList<Transaction>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM transactions "
								+ "WHERE transaction_type LIKE ? "
								+ "ORDER BY transaction_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setString(1, type);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int trans_num = rs.getInt("transaction_num");
					
					java.sql.Date date = rs.getDate("transaction_date");
					
					int item_num = rs.getInt("item_num");
					Item item = getItemByNum(item_num);
					
					int quantity = rs.getInt("transaction_quantity");
					
					int wh_num = rs.getInt("wh_num");
					Warehouse wh = getWarehouseByNum(wh_num);
					
					list.add( new Transaction(trans_num, date, item, quantity, type, wh) );
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
	public ArrayList<Transaction> getAllTransactions(Warehouse wh) {
		
		ResultSet rs = null;
		ArrayList<Transaction> list = new ArrayList<Transaction>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM transactions "
								+ "WHERE wh_num = ? "
								+ "ORDER BY transaction_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, wh.getNum());
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int trans_num = rs.getInt("transaction_num");
					
					java.sql.Date date = rs.getDate("transaction_date");
					
					int item_num = rs.getInt("item_num");
					Item item = getItemByNum(item_num);
					
					int quantity = rs.getInt("transaction_quantity");
					
					String type = rs.getString("transaction_type");
					
					list.add( new Transaction(trans_num, date, item, quantity, type, wh) );
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
	public ArrayList<Stock> getStock() {
		
		ResultSet rs = null;
		ArrayList<Stock> list = new ArrayList<Stock>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM stock "
								+ "ORDER BY item_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int item_num = rs.getInt("item_num");
					Item item = getItemByNum(item_num);
					
					int wh_num = rs.getInt("wh_num");
					Warehouse wh = getWarehouseByNum(wh_num);
					
					int quantity = rs.getInt("stock_quantity");
					
					list.add( new Stock(item, wh, quantity) );
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}

	public ArrayList<Stock> getStock(Item item) {
		
		ResultSet rs = null;
		ArrayList<Stock> list = new ArrayList<Stock>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM stock "
								+ "WHERE item_num = ? "
								+ "ORDER BY wh_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, item.getNum());
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int wh_num = rs.getInt("wh_num");
					Warehouse wh = getWarehouseByNum(wh_num);
					
					int quantity = rs.getInt("stock_quantity");
					
					list.add( new Stock(item, wh, quantity) );
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
	public ArrayList<Stock> getStock(Warehouse wh) {
		
		ResultSet rs = null;
		ArrayList<Stock> list = new ArrayList<Stock>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM stock "
								+ "WHERE wh_num = ? "
								+ "ORDER BY item_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, wh.getNum());
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int item_num = rs.getInt("item_num");
					Item item = getItemByNum(item_num);
					
					int quantity = rs.getInt("stock_quantity");
					
					list.add( new Stock(item, wh, quantity) );
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
//	public ArrayList<ItemCustomerReportLine> getItemCustomerReport() {
//		
//		ResultSet rs = null;
//		ArrayList<ItemCustomerReportLine> list = new ArrayList<ItemCustomerReportLine>();
//		
//		synchronized (connection) {
//			try {
//				String sqlQuery = "SELECT 	customer_num, invoice_date, invoice.invoice_num, "
//								+ "			line_num, item_num, line_quantity, line_final_price "
//								+ "FROM 	invoice "
//								+ "				INNER JOIN "
//								+ "			invoice_lines "
//								+ "	ON invoice.invoice_num = invoice_lines.invoice_num "
//								+ "ORDER BY customer_num ASC, invoice.invoice_num ASC, line_num ASC";
//
//				ps = connection.prepareStatement(sqlQuery);
//				
//				rs = ps.executeQuery();
//				
//				while (rs.next()) {
//					
//					int customer_num = rs.getInt("customer_num");
//					Customer customer = getCustomerByNum(customer_num);
//					
//					java.sql.Date date = rs.getDate("invoice_date");
//					
//					int item_num = rs.getInt("item_num");
//					Item item = getItemByNum(item_num);
//					
//					int quantity = rs.getInt("transaction_quantity");
//					
//					String type = rs.getString("transaction_type");
//					
//					list.add( new Transaction(trans_num, date, item, quantity, type, wh) );
//				}
//				
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} finally {
//				try { rs.close(); ps.close(); }
//				catch (Exception e1) {}
//			}
//		}
//		
//		return list;
//	}
	
	public int getCurrentOrderNum() {
		
		ResultSet rs = null;
		int max = -1;
		
		synchronized (connection) {
			try {
				String sqlQuery = "select \"ORDERS_SEQ\".currval from dual";

				ps = connection.prepareStatement(sqlQuery);
				
				rs = ps.executeQuery();
				
				rs.next();
				max = rs.getInt(1);
							
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return max;
	}

	public int getCurrentInvoiceNum() {
		
		ResultSet rs = null;
		int max = -1;
		
		synchronized (connection) {
			try {
				String sqlQuery = "select \"INVOICE_SEQ\".currval from dual";

				ps = connection.prepareStatement(sqlQuery);
				
				rs = ps.executeQuery();
				
				rs.next();
				max = rs.getInt(1);
							
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
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

				ps = connection.prepareStatement(sqlQuery);

				ps.setDate(1, getCurrentDate());
				ps.setInt(2, c.getNum());
				ps.setInt(3, 0);
				ps.setString(4, "open");
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
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

				ps = connection.prepareStatement(sqlQuery);

				ps.setDate(1, getCurrentDate());
				ps.setInt(2, c.getNum());
				ps.setInt(3, 0);
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return success;
	}

	public boolean addNewInvoiceFromOrder(Order o) {
		
		boolean success = false;
		
		synchronized (connection) {
			try {
				String sqlQuery = 	"INSERT INTO invoice "
						+ "(order_num, invoice_date, customer_num, invoice_price) "
						+ "VALUES (?,?,?,?)";

				ps = connection.prepareStatement(sqlQuery);

				ps.setInt(1, o.getNum());
				ps.setDate(2, getCurrentDate());
				ps.setInt(3, o.getCustomer().getNum());
				ps.setFloat(4, o.getPrice());
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
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
	
					ps = connection.prepareStatement(sqlQuery);
	
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
				} finally {
					try { ps.close(); }
					catch (Exception e1) {}
				}
			}
		}
		
		return success;
	}

	public boolean deleteOrderLines(Order order) {
		
		boolean success = false;
		
		synchronized (connection) {
			
			try {
				String sqlQuery = "DELETE FROM orders_lines WHERE order_num = ?";

				ps = connection.prepareStatement(sqlQuery);

				ps.setInt(1, order.getNum());
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
			}
		
		}
		
		return success;
	}

	public boolean deleteOrder(Order order) {
		
		boolean success = false;
		
		synchronized (connection) {
			
			try {
				String sqlQuery = "DELETE FROM orders WHERE order_num = ?";

				ps = connection.prepareStatement(sqlQuery);

				ps.setInt(1, order.getNum());
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
			}
		
		}
		
		return success;
	}
	
	public boolean getOrderLines(Order order) {
		
		ResultSet rs = null;
		boolean success = false;
		
		synchronized (connection) {
			
			try {
				String sqlQuery = "SELECT * FROM orders_lines "
						+ "WHERE order_num = ? "
						+ "ORDER BY line_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				ps.setInt(1, order.getNum());
				
				rs = ps.executeQuery();
				
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
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
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
	
					ps = connection.prepareStatement(sqlQuery);
	
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
				} finally {
					try { ps.close(); }
					catch (Exception e1) {}
				}
			}
		}
		
		return success;
	}
	
	public boolean updateInvoicePrice(int invoice_num, float invoice_price) {
		
		boolean success = false;
		
		synchronized (connection) {

			try {
				String sqlQuery = "UPDATE invoice "
						+ "SET invoice_price = ? "
						+ "WHERE invoice_num = ? ";

				ps = connection.prepareStatement(sqlQuery);

				ps.setFloat(1, invoice_price);
				ps.setInt(2, invoice_num);

				ps.executeUpdate();

				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
			}

		}
		
		return success;
	}
	
	public boolean updateOrderPrice(int order_num, float order_price) {
		
		boolean success = false;
		
		synchronized (connection) {

			try {
				String sqlQuery = "UPDATE orders "
						+ "SET order_price = ? "
						+ "WHERE order_num = ? ";

				ps = connection.prepareStatement(sqlQuery);

				ps.setFloat(1, order_price);
				ps.setInt(2, order_num);

				ps.executeUpdate();

				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
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

				ps = connection.prepareStatement(sqlQuery);

				ps.setString(1, "closed");
				ps.setInt(2, order_num);

				ps.executeUpdate();

				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
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

				ps = connection.prepareStatement(sqlQuery);

				ps.setString(1, "open");
				ps.setInt(2, order_num);

				ps.executeUpdate();

				success = true;

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
			}

		}
		
		return success;
	}
	
	public static java.sql.Date getCurrentDate() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Date(today.getTime());
	}
	
	public static java.sql.Date convertCalendarToSql(Calendar date) {
		return new java.sql.Date(date.getTimeInMillis());
	}
	
}

