package seedu.addressbook;

public class Person {
    private String name, email, phone;

    public Person(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public boolean isValid() {
        return isNameValid() && isPhoneValid() && isEmailValid();
    }

    public boolean isNameValid() {
        return name.matches("(\\w|\\s)+");  // name is nonempty mixture of alphabets and whitespace
    }

    public boolean isPhoneValid() {
        return phone.matches("\\d+");    // phone nonempty sequence of digits
    }

    public boolean isEmailValid() {
        return email.matches("\\S+@\\S+\\.\\S+"); // email is [non-whitespace]@[non-whitespace].[non-whitespace]
    }
}
