package org.example.component;


import java.text.MessageFormat;
public class Person {
    private final String name;
    private final String surname;
    private final String phone;
    private final String email;
    public Person(String name, String surname, String phone, String email) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString(){
        return MessageFormat.format("Contact info:\n\tName: {0}\n\tSurname: {1}\n\tPhone: {2}\n\tEmail: {3}",
                getName(),getSurname(),getPhone(),getEmail());
    }
    public String getName() { return name; }

    public String getSurname() { return surname; }

    public String getPhone() { return phone; }

    public String getEmail() { return email; }

}
