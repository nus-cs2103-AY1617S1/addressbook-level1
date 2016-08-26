package seedu.addressbook;

public class Person {
	
	private int datacount;
	private String name;
	private String phone;
	private String email;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getDatacount() {
		return datacount;
	}
	public void setDatacount(int datacount) {
		this.datacount = datacount;
	}
	
}
