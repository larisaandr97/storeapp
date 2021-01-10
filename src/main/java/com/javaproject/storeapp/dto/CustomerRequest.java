package com.javaproject.storeapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.javaproject.storeapp.entities.Pattern.EMAIL_ADDRESS;

@Data
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

}
