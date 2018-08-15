package com.example.ashu.supersearch.Phone;

public class ContactList  {
    private final String name;
    private final String phoneNumber;

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
