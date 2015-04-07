package Model;

import java.sql.Blob;

public class Item {

	private int 		num;
	private String 		name;
	private String 		desc;
	private float 		price;
	private Blob 		img;
	private Warehouse 	warehouse;
	

	public Item(int num, String name, String desc, float price, Blob img,
			Warehouse warehouse) {
		this.num = num;
		this.name = name;
		this.desc = desc;
		this.price = price;
		this.img = img;
		this.warehouse = warehouse;
	}

	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Blob getImg() {
		return img;
	}

	public void setImg(Blob img) {
		this.img = img;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	
	@Override
	public String toString() {
		return num + " : " + name;
	}
	
}
