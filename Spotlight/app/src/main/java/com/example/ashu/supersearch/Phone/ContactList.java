package com.example.ashu.supersearch.Phone;

public class ContactList  {
    String name,phoneNumber;

    public ContactList(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
