package Model;

public class Stock {

	private Item 		item;
	private Warehouse 	warehouse;
	private int 		quantity;
	
	
	public Stock(Item item, Warehouse warehouse, int quantity) {
		this.item = item;
		this.warehouse = warehouse;
		this.quantity = quantity;
	}


	public Item getItem() {
		return item;
	}


	public void setItem(Item item) {
		this.item = item;
	}


	public Warehouse getWarehouse() {
		return warehouse;
	}


	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
