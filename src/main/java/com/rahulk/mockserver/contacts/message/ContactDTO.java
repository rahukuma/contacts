package com.rahulk.mockserver.contacts.message;

/**
 * Created by rahulk on 13-05-16.
 */
public class ContactDTO {
    private String name;
    private String id;
    private String email;
    private Long phone;
    private String address;

    public ContactDTO(){

    }

    public ContactDTO(String name, String id, String email, Long phone, String address) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
