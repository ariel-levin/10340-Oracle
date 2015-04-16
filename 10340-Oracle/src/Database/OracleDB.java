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
import view.utils.DBErrors;


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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				String sqlQuery = "SELECT * FROM customers "
								+ "ORDER BY customer_num ASC";

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
				DBErrors.showError(e);
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
				String sqlQuery = "SELECT * FROM items "
								+ "ORDER BY item_num ASC";

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
				DBErrors.showError(e);
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
				String sqlQuery = "SELECT * FROM warehouses "
								+ "ORDER BY wh_num ASC";

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
				DBErrors.showError(e);
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
	public ArrayList<Stock> getAllStockLines() {
		
		ResultSet rs = null;
		ArrayList<Stock> list = new ArrayList<Stock>();
		
		synchronized (connection) {
			try {
				String sqlQuery = "SELECT * FROM stock "
								+ "ORDER BY item_num ASC, wh_num ASC";

				ps = connection.prepareStatement(sqlQuery);
				
				rs = ps.executeQuery();
				
				while (rs.next()) {
					
					int item_num = rs.getInt("item_num");
					Item item = getItemByNum(item_num);
					
					int wh_num = rs.getInt("wh_num");
					Warehouse wh = getWarehouseByNum(wh_num);
					
					list.add( new Stock( item, wh, rs.getInt("stock_quantity") ));
				}
							
			} catch (SQLException e) {
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				String sqlQuery = "SELECT * FROM orders "
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
				DBErrors.showError(e);
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
				String sqlQuery = null;
				if (withLines)
					sqlQuery = "SELECT * FROM invoice "
							 + "ORDER BY customer_num ASC, invoice_num ASC";
				else
					sqlQuery = "SELECT * FROM invoice "
							 + "ORDER BY invoice_num ASC";

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
							DBErrors.showError(e);
						} finally {
							try { rs2.close(); ps.close(); }
							catch (Exception e1) {}
						}
					}
					
					list.add(invoice);
				}
				
			} catch (SQLException e) {
				DBErrors.showError(e);
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
							DBErrors.showError(e);
						} finally {
							try { rs2.close(); ps.close(); }
							catch (Exception e1) {}
						}
					}
					
					list.add(invoice);
				}
				
			} catch (SQLException e) {
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return list;
	}
	
	public Item checkOrderLinesInStock(Order order) {
		
		ResultSet rs = null;

		synchronized (connection) {
			
			ArrayList<OrderLine> list = order.getLines();
			
			for (OrderLine line : list) {
				try {
					String sqlQuery = "SELECT ARIEL.check_item_stock(?,?) "
									+ "FROM dual";

					ps = connection.prepareStatement(sqlQuery);
					
					ps.setInt(1, line.getItem().getNum());
					ps.setInt(2, line.getQuantity());
					
					rs = ps.executeQuery();
					
					rs.next();

					if (rs.getInt(1) == 0)
						return line.getItem();
	
				} catch (SQLException e) {
					DBErrors.showError(e);
				} finally {
					try { rs.close(); ps.close(); }
					catch (Exception e1) {}
				}
			}
		}
		
		return null;
	}
	
	public Item checkInvoiceLinesInStock(Invoice invoice) {
		
		ResultSet rs = null;

		synchronized (connection) {
			
			ArrayList<InvoiceLine> list = invoice.getLines();
			
			for (InvoiceLine line : list) {
				try {
					String sqlQuery = "SELECT ARIEL.check_item_stock(?,?) "
									+ "FROM dual";

					ps = connection.prepareStatement(sqlQuery);
					
					ps.setInt(1, line.getItem().getNum());
					ps.setInt(2, line.getQuantity());
					
					rs = ps.executeQuery();
					
					rs.next();

					if (rs.getInt(1) == 0)
						return line.getItem();
	
				} catch (SQLException e) {
					DBErrors.showError(e);
				} finally {
					try { rs.close(); ps.close(); }
					catch (Exception e1) {}
				}
			}
		}
		
		return null;
	}
	
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
			} finally {
				try { rs.close(); ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return max;
	}
	
	public boolean addNewCustomer(Customer c) {
		
		boolean success = false;
		
		synchronized (connection) {
			try {
				String sqlQuery = 	"INSERT INTO customers "
						+ "(customer_fname, customer_lname, customer_id, customer_street, "
						+ "customer_city, customer_phone) "
						+ "VALUES (?,?,?,?,?,?)";

				ps = connection.prepareStatement(sqlQuery);

				ps.setString(1, c.getFname());
				ps.setString(2, c.getLname());
				ps.setInt(3, c.getId());
				ps.setString(4, c.getStreet());
				ps.setString(5, c.getCity());
				ps.setString(6, c.getPhone());
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				if (e.getErrorCode() == DBErrors.UNIQUE_CONSTRAINT)
					DBErrors.showError(e, "ID");
				else
					DBErrors.showError(e);
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return success;
	}

	public boolean addNewItem(Item item) {
		
		boolean success = false;
		
		synchronized (connection) {
			try {
				String sqlQuery = 	"INSERT INTO items "
						+ "(item_name, item_desc, item_price, item_img) "
						+ "VALUES (?,?,?,?)";

				ps = connection.prepareStatement(sqlQuery);

				ps.setString(1, item.getName());
				ps.setString(2, item.getDesc());
				ps.setFloat(3, item.getPrice());
				ps.setBlob(4, item.getImg());
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				DBErrors.showError(e);
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return success;
	}
	
	public boolean addNewWarehouse(Warehouse wh) {
		
		boolean success = false;
		
		synchronized (connection) {
			try {
				String sqlQuery = 	"INSERT INTO warehouses "
						+ "(wh_name, wh_street, wh_city, wh_phone) "
						+ "VALUES (?,?,?,?)";

				ps = connection.prepareStatement(sqlQuery);

				ps.setString(1, wh.getName());
				ps.setString(2, wh.getStreet());
				ps.setString(3, wh.getCity());
				ps.setString(4, wh.getPhone());
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				DBErrors.showError(e);
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return success;
	}
	
	public boolean addNewStockLine(Stock stock) {
		
		boolean success = false;
		
		synchronized (connection) {
			try {
				String sqlQuery = 	"INSERT INTO stock "
						+ "(item_num, wh_num, stock_quantity) "
						+ "VALUES (?,?,?)";

				ps = connection.prepareStatement(sqlQuery);

				ps.setInt(1, stock.getItem().getNum());
				ps.setInt(2, stock.getWarehouse().getNum());
				ps.setInt(3, stock.getQuantity());
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				if (e.getErrorCode() == DBErrors.UNIQUE_CONSTRAINT)
					DBErrors.showError(e, "A line with this Item and Warehouse");
				else
					DBErrors.showError(e);
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return success;
	}
	
	public boolean updateStockLine(Stock stock) {
		
		boolean success = false;
		
		synchronized (connection) {
			try {
				String sqlQuery = "UPDATE stock "
						+ "SET stock_quantity = ? "
						+ "WHERE item_num = ? "
						+ "	AND wh_num = ?";

				ps = connection.prepareStatement(sqlQuery);

				ps.setInt(1, stock.getQuantity());
				ps.setInt(2, stock.getItem().getNum());
				ps.setInt(3, stock.getWarehouse().getNum());
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				DBErrors.showError(e);
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
			}
		}
		
		return success;
	}
	
	public boolean addNewOrder(Customer c) {
		
		boolean success = false;
		
		synchronized (connection) {
			try {
				String sqlQuery = 	"INSERT INTO orders "
						+ "(customer_num) VALUES (?)";

				ps = connection.prepareStatement(sqlQuery);

				ps.setInt(1, c.getNum());
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				DBErrors.showError(e);
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
						+ "(customer_num) VALUES (?)";

				ps = connection.prepareStatement(sqlQuery);

				ps.setInt(1, c.getNum());
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				DBErrors.showError(e);
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
						+ "(order_num, customer_num) "
						+ "VALUES (?,?)";

				ps = connection.prepareStatement(sqlQuery);

				ps.setInt(1, o.getNum());
				ps.setInt(2, o.getCustomer().getNum());
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				DBErrors.showError(e);
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
							+ "line_discount) "
							+ "VALUES (?,?,?,?,?,?)";
	
					ps = connection.prepareStatement(sqlQuery);
	
					ps.setInt(1, order.getNum());
					ps.setInt(2, line.getNum());
					ps.setInt(3, line.getItem().getNum());
					ps.setInt(4, line.getQuantity());
					ps.setFloat(5, line.getPrice());
					ps.setInt(6, line.getDiscount());
					
					ps.executeUpdate();
					
					success = true;
	
				} catch (SQLException e) {
					DBErrors.showError(e, line.getItem());
					if (e.getErrorCode() == DBErrors.STOCK_QUANTITY) {
						try { ps.close(); }
						catch (Exception e1) {}
						
						deleteOrderLines(order);	// delete all lines that were added
						return false;
					}
					
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
				DBErrors.showError(e);
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
			}
		
		}
		
		return success;
	}
	
	public boolean deleteInvoiceLines(Invoice invoice) {
		
		boolean success = false;
		
		synchronized (connection) {
			
			try {
				String sqlQuery = "DELETE FROM invoice_lines WHERE invoice_num = ?";

				ps = connection.prepareStatement(sqlQuery);

				ps.setInt(1, invoice.getNum());
				
				ps.executeUpdate();
				
				success = true;

			} catch (SQLException e) {
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
							+ "line_discount) "
							+ "VALUES (?,?,?,?,?,?)";
	
					ps = connection.prepareStatement(sqlQuery);
	
					ps.setInt(1, invoice.getNum());
					ps.setInt(2, line.getNum());
					ps.setInt(3, line.getItem().getNum());
					ps.setInt(4, line.getQuantity());
					ps.setFloat(5, line.getPrice());
					ps.setInt(6, line.getDiscount());
					
					ps.executeUpdate();
					
					success = true;
	
				} catch (SQLException e) {
					DBErrors.showError(e, line.getItem());
					if (e.getErrorCode() == DBErrors.STOCK_QUANTITY) {
						try { ps.close(); }
						catch (Exception e1) {}
						
						deleteInvoiceLines(invoice);	// delete all lines that were added
						return false;
					}
					
				} finally {
					try { ps.close(); }
					catch (Exception e1) {}
				}
			}
		}
		
		return success;
	}
	
	public boolean updateInvoicePrice(int invoice_num) {
		
		boolean success = false;
		
		synchronized (connection) {

			try {
				String sqlQuery = "begin ARIEL.invoice_final_price(?); end;";

				ps = connection.prepareStatement(sqlQuery);

				ps.setInt(1, invoice_num);

				ps.execute();
				
				success = true;

			} catch (SQLException e) {
				DBErrors.showError(e);
			} finally {
				try { ps.close(); }
				catch (Exception e1) {}
			}

		}
		
		return success;
	}
	
	public boolean updateOrderPrice(int order_num) {
		
		boolean success = false;
		
		synchronized (connection) {

			try {
				String sqlQuery = "begin ARIEL.orders_final_price(?); end;";

				ps = connection.prepareStatement(sqlQuery);

				ps.setInt(1, order_num);

				ps.executeUpdate();

				success = true;

			} catch (SQLException e) {
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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
				DBErrors.showError(e);
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

