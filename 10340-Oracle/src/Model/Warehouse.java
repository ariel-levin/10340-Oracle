package model;


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
	
	public Warehouse(String name, String street, String city,
			String phone) {
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


	@Override
	public String toString() {
		if (city == null)
			return num + " : " + name;
		else
			return num + " : " + name + " : " + city;
	}
	
}
