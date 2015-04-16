package model;

import java.sql.Blob;

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
public class Item {

	private int 		num;
	private String 		name;
	private String 		desc;
	private float 		price;
	private Blob 		img;
	

	public Item(int num, String name, String desc, float price, Blob img) {
		this.num = num;
		this.name = name;
		this.desc = desc;
		this.price = price;
		this.img = img;
	}
	
	public Item(String name, String desc, float price) {
		this.name = name;
		this.desc = desc;
		this.price = price;
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
		this.price = SalePanel.round(price, 2);
	}

	public Blob getImg() {
		return img;
	}

	public void setImg(Blob img) {
		this.img = img;
	}
	
	
	@Override
	public String toString() {
		return num + " : " + name;
	}
	
}
