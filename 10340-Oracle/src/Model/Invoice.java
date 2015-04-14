package model;

import java.sql.Date;
import java.util.ArrayList;

import view.panels.SalePanel;


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
public class Invoice {

	private int 					num;
	private Order 					order;
	private Date 					date;
	private Customer 				customer;
	private float 					price;
	private ArrayList<InvoiceLine> 	lines;
	
	
	public Invoice(Customer customer) {
		this.customer = customer;
		this.lines = new ArrayList<InvoiceLine>();
	}

	public Invoice(Order order) {
		this.order = order;
		this.lines = new ArrayList<InvoiceLine>();
	}
	
	public Invoice(int num, Order order, Date date, Customer customer, float price) {
		this.num = num;
		this.order = order;
		this.date = date;
		this.customer = customer;
		this.price = price;
		this.lines = new ArrayList<InvoiceLine>();
	}
	

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = SalePanel.round(price, 2);
	}

	public ArrayList<InvoiceLine> getLines() {
		return lines;
	}

	public void setLines(ArrayList<InvoiceLine> lines) {
		this.lines = lines;
	}
	
	public void addLine(InvoiceLine line) {
		lines.add(line);
	}
	
	public InvoiceLine getLine(int line_num) {
		return lines.get(line_num - 1);
	}
	
	public int getNumOfLines() {
		return lines.size();
	}
	
	public void removeLines() {
		this.lines = new ArrayList<InvoiceLine>();
	}
	
	
	public String toString() {
		return num + " | " + customer + " | " + date + " | " + price + "¤";
	}
	
}
