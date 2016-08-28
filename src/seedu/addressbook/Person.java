package seedu.addressbook;

public class Person {
	private String _name, _phone, _email;
	
	public Person(String name, String phone, String email) {
		this._name = name;
		this._phone = phone;
		this._email = email;
	}
	
    /**
     * @param person whose name you want
     * @return person's name
     */
    
    public String getName() {
        return this._name;
    }
    
    /**
     * @param person whose phone number you want
     * @return person's phone number
     */
    public String getPhone() {
        return this._phone;
    }
    
    /**
     * @param person whose email you want
     * @return person's email
     */
    public String getEmail() {
        return this._email;
    }
    
    public void updateName(String name) {
    	this._name = name;
    }
    
    public void updatePhone(String phone) {
    	this._phone = phone;
    }
    
    public void updateEmail(String email) {
    	this._email = email;
    }
    
}
