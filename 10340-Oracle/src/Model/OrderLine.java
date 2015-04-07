package Model;

public class OrderLine {

	private int		num;
	private Item	item;
	private int		quantity;
	private float	price;
	private int		discount;
	private float	finalPrice;
	
	
	public OrderLine(int num, Item item, int quantity, float price, int discount, float finalPrice) {
		this.num = num;
		this.item = item;
		this.quantity = quantity;
		this.price = price;
		this.discount = discount;
		this.finalPrice = finalPrice;
	}


	public int getNum() {
		return num;
	}


	public void setNum(int num) {
		this.num = num;
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


	public float getPrice() {
		return price;
	}


	public void setPrice(float price) {
		this.price = price;
	}


	public int getDiscount() {
		return discount;
	}


	public void setDiscount(int discount) {
		this.discount = discount;
	}


	public float getFinalPrice() {
		return finalPrice;
	}


	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}
	
}
