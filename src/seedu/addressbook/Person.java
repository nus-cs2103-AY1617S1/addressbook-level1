/**
 * 
 */
package seedu.addressbook;

/**
 * @author YiMin
 *
 */
public class Person {
    private static final String PERSON_STRING_REPRESENTATION = "%1$s " // name
            + AddressBook.PERSON_DATA_PREFIX_PHONE + "%2$s " // phone
            + AddressBook.PERSON_DATA_PREFIX_EMAIL + "%3$s"; // email
	
	private final String name_;
	private final String email_;
	private final String phone_;
	
	public Person(String name, String phone, String email){
		name_=name;
		phone_=phone;
		email_=email;
	}

	/**
	 * @return the name_
	 */
	public String getName() {
		return name_;
	}

	/**
	 * @return the email_
	 */
	public String getEmail() {
		return email_;
	}

	/**
	 * @return the phone_
	 */
	public String getPhone() {
		return phone_;
	}
	
    /**
     * Validates the person's data fields
     *
     * @return whether the person has valid data
     */
	public boolean isValid() {
        return isNameValid() && isPhoneValid() && isEmailValid();
    }
	
    /**
     * Validates name as a legal person name
     *
     * @return whether name is a valid person name
     */
    private boolean isNameValid() {
        return name_.matches("(\\w|\\s)+");  // name is nonempty mixture of alphabets and whitespace
        //TODO: implement a more permissive validation
    }
    
    /**
     * Validates phone as a legal person phone number
     *=
     * @return whether phone is a valid person phone number
     */
    private boolean isPhoneValid() {
        return phone_.matches("\\d+");    // phone nonempty sequence of digits
        //TODO: implement a more permissive validation
    }
    
    /**
     * Validates email as a legal person email
     *
     * @return whether email is a valid person email
     */
    private boolean isEmailValid() {
        return email_.matches("\\S+@\\S+\\.\\S+"); // email is [non-whitespace]@[non-whitespace].[non-whitespace]
        //TODO: implement a more permissive validation
    }
	
	@Override
	public String toString(){
        return String.format(PERSON_STRING_REPRESENTATION,
                name_,email_,phone_);
	}
}
