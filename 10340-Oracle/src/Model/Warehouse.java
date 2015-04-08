package model;

public class Warehouse {

	private int 	num;
	private String 	name;
	private String 	street;
	private String 	city;
	private String 	phone;
	
	
	public Warehouse(int num, String name, String street, String city,
			String phone) {
		this.num = num;
		this.name = name;
		this.street = street;
		this.city = city;
		this.phone = phone;
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


	public String getStreet() {
		return street;
	}


	public void setStreet(String street) {
		this.street = street;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}	
	
}
