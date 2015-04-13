package model;

import java.sql.Date;


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
public class Transaction {

	private int 		num;
	private Date 		date;
	private Item 		item;
	private int 		quantity;
	private String 		type;
	private Warehouse 	warehouse;
	

	public Transaction(int num) {
		this.num = num;
	}

	public Transaction(int num, Date date, Item item, int quantity,
			String type, Warehouse warehouse) {
		this.num = num;
		this.date = date;
		this.item = item;
		this.quantity = quantity;
		this.type = type;
		this.warehouse = warehouse;
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


	public Item getItem() {
		return item;
	}


	public void setItem(Item item) {
		this.item = item;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Warehouse getWarehouse() {
		return warehouse;
	}


	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	
}
