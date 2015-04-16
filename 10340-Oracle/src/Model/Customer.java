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
public class Customer {

	private int 	num;
	private String 	fname;
	private String 	lname;
	private int 	id;
	private String	street;
	private	String	city;
	private String	phone;
	

	public Customer(int num, String fname, String lname, int id, String street,
			String city, String phone) {
		this.num = num;
		this.fname = fname;
		this.lname = lname;
		this.id = id;
		this.street = street;
		this.city = city;
		this.phone = phone;
	}
	
	public Customer(String fname, String lname, int id, String street,
			String city, String phone) {
		this.fname = fname;
		this.lname = lname;
		this.id = id;
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

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		if (lname != null)
			return num + " : " + fname + " " + lname;
		else
			return num + " : " + fname;
	}
	
}
