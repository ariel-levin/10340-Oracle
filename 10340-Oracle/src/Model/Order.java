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
public class Order {

	private int 					num;
	private Date 					date;
	private Customer 				customer;
	private float 					price;
	private String 					status;
	private ArrayList<OrderLine>	lines;
	
	
	public Order(Customer customer) {
		this.customer = customer;
		this.lines = new ArrayList<OrderLine>();
	}

	public Order(int num, Date date, Customer customer, float price, String status) {
		this.num = num;
		this.date = date;
		this.customer = customer;
		this.price = price;
		this.status = status;
		this.lines = new ArrayList<OrderLine>();
	}
	

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<OrderLine> getLines() {
		return lines;
	}

	public void setLines(ArrayList<OrderLine> lines) {
		this.lines = lines;
	}
	
	public void addLine(OrderLine line) {
		lines.add(line);
	}
	
	public OrderLine getLine(int line_num) {
		return lines.get(line_num - 1);
	}
	
	public int getNumOfLines() {
		return lines.size();
	}
	
	public void removeLines() {
		this.lines = new ArrayList<OrderLine>();
	}
	
	
	@Override
	public String toString() {
		return num + " | " + customer + " | " + date + " | " + price + "¤";
	}
	
}
