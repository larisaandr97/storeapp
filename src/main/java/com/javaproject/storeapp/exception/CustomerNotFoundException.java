package com.javaproject.storeapp.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(int id) {
        super("Customer with Id " + id + " not found.");
    }
}
