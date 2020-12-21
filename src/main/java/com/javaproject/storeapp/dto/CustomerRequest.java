package com.javaproject.storeapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.javaproject.storeapp.entities.Pattern.EMAIL_ADDRESS;

public class CustomerRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    @Pattern(regexp = EMAIL_ADDRESS, message = "The e-mail address you entered is not valid.")
    private String mail;

    @NotBlank
    private String address;

    public CustomerRequest(String firstName, String lastName, String mail, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
